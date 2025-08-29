const amqp = require('amqplib');

async function processPayments() {
    try {
        const connection = await amqp.connect('amqp://localhost');
        const channel = await connection.createChannel();
        
        const queue = 'orders.payments';
        await channel.assertQueue(queue, { durable: true });
        
        console.log('PaymentsService waiting for messages...');
        
        channel.consume(queue, (message) => {
            if (message !== null) {
                const order = JSON.parse(message.content.toString());
                console.log(`Processing payment for order: ${order.orderId}, Amount: $${order.totalAmount}`);
                
                // Simulate payment processing
                setTimeout(() => {
                    console.log(`Payment processed for order: ${order.orderId}`);
                    channel.ack(message);
                    
                    // Send to shipping queue
                    channel.sendToQueue('orders.shipping', 
                        Buffer.from(JSON.stringify(order)));
                }, 2000);
            }
        });
    } catch (error) {
        console.error('Error:', error);
    }
}

processPayments();