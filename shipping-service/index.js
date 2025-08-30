const amqp = require('amqplib');

let connection;
let channel;
let isConnected = false;

async function connectWithRetry() {
    const maxRetries = 10;
    const retryInterval = 5000;

    for (let i = 0; i < maxRetries; i++) {
        try {
            console.log(`ShippingService - Connection attempt ${i + 1}/${maxRetries}`);
            connection = await amqp.connect('amqp://guest:guest@localhost:5672');
            
            connection.on('error', (err) => {
                console.error('RabbitMQ connection error:', err.message);
                isConnected = false;
            });

            connection.on('close', () => {
                console.log('RabbitMQ connection closed. Reconnecting...');
                isConnected = false;
                setTimeout(connectWithRetry, retryInterval);
            });

            channel = await connection.createChannel();
            await channel.assertQueue('orders.shipping', { 
                durable: true
            });
            
            console.log('ShippingService connected to RabbitMQ successfully');
            isConnected = true;
            return;
            
        } catch (err) {
            console.error(`Connection failed: ${err.message}`);
            if (i < maxRetries - 1) {
                console.log(`Retrying in ${retryInterval/1000} seconds...`);
                await new Promise(resolve => setTimeout(resolve, retryInterval));
            }
        }
    }
    
    throw new Error(`Failed to connect after ${maxRetries} attempts`);
}

async function processShipping() {
    try {
        await connectWithRetry();
        
        console.log('ShippingService waiting for messages...');
        
        // Set prefetch to avoid overloading
        await channel.prefetch(1);
        
        channel.consume('orders.shipping', async (message) => {
            if (message !== null) {
                try {
                    const order = JSON.parse(message.content.toString());
                    console.log(`Preparing shipment for order: ${order.orderId}`);
                    
                    // Simulate shipping process with error handling
                    setTimeout(() => {
                        try {
                            console.log(`Order ${order.orderId} shipped successfully!`);
                            channel.ack(message);
                            
                            // Optional: Send confirmation to another queue
                            if (isConnected) {
                                channel.sendToQueue('orders.completed', 
                                    Buffer.from(JSON.stringify({
                                        ...order,
                                        status: 'shipped',
                                        shippedAt: new Date().toISOString()
                                    })),
                                    { persistent: true }
                                );
                            }
                            
                        } catch (processError) {
                            console.error('Error processing shipment:', processError);
                            // Negative acknowledgement - message will be requeued
                            channel.nack(message, false, true);
                        }
                    }, 3000);
                    
                } catch (parseError) {
                    console.error('Error parsing message:', parseError);
                    // Reject message without requeue (send to DLQ if configured)
                    channel.nack(message, false, false);
                }
            }
        }, { 
            noAck: false // Manual acknowledgement
        });
        
    } catch (error) {
        console.error('Fatal error in shipping service:', error);
        process.exit(1);
    }
}

// Health check endpoint (optional)
const express = require('express');
const app = express();
const port = 3002;

app.get('/health', (req, res) => {
    res.json({ 
        status: isConnected ? 'healthy' : 'unhealthy',
        service: 'shipping-service',
        rabbitmq: isConnected ? 'connected' : 'disconnected'
    });
});

app.listen(port, () => {
    console.log(`Shipping service health check available at http://localhost:${port}/health`);
});

// Handle graceful shutdown
process.on('SIGINT', async () => {
    console.log('Shutting down shipping service...');
    try {
        if (channel) await channel.close();
        if (connection) await connection.close();
        console.log('Shipping service shut down gracefully');
        process.exit(0);
    } catch (shutdownError) {
        console.error('Error during shutdown:', shutdownError);
        process.exit(1);
    }
});

// Handle uncaught exceptions
process.on('uncaughtException', (error) => {
    console.error('Uncaught exception:', error);
    process.exit(1);
});

process.on('unhandledRejection', (reason, promise) => {
    console.error('Unhandled promise rejection:', reason);
    process.exit(1);
});

processShipping();