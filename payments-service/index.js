const amqp = require('amqplib');

let connection;
let channel;
let isConnected = false;

async function connectWithRetry() {
    const maxRetries = 10;
    const retryInterval = 5000;

    for (let i = 0; i < maxRetries; i++) {
        try {
            console.log(`Connection attempt ${i + 1}/${maxRetries}`);
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
            // Only assert the payments queue in the payments service
            await channel.assertQueue('orders.payments', { durable: true });
            
            console.log('Connected to RabbitMQ successfully');
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

async function processPayments() {
    try {
        await connectWithRetry();
        
        console.log('PaymentsService waiting for messages...');
        
        channel.consume('orders.payments', (message) => {
            if (message !== null) {
                try {
                    const order = JSON.parse(message.content.toString());
                    console.log(`Processing payment for order: ${order.orderId}, Amount: $${order.totalAmount}`);
                    
                    // Simulate payment processing
                    setTimeout(() => {
                        console.log(`Payment processed for order: ${order.orderId}`);
                        channel.ack(message);
                        
                        // Send to shipping queue
                        if (isConnected) {
                            channel.sendToQueue('orders.shipping', 
                                Buffer.from(JSON.stringify(order)),
                                { persistent: true });
                        }
                    }, 2000);
                } catch (parseError) {
                    console.error('Error parsing message:', parseError);
                    channel.nack(message);
                }
            }
        }, { noAck: false });
        
    } catch (error) {
        console.error('Fatal error:', error);
        process.exit(1);
    }
}

// Handle graceful shutdown
process.on('SIGINT', async () => {
    console.log('Shutting down...');
    if (channel) await channel.close();
    if (connection) await connection.close();
    process.exit(0);
});

processPayments();