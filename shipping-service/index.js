const amqp = require('amqplib');

async function processShipping() {
    try {
        const connection = await amqp.connect('amqp://localhost');
        const channel = await connection.createChannel();
        
        const queue = 'orders.shipping';
        await channel.assertQueue(queue, { durable: true });
        
        console.log('ShippingService waiting for messages...');
        
        channel.consume(queue, (message) => {
            if (message !== null) {
                const order = JSON.parse(message.content.toString());
                console.log(`Preparing shipment for order: ${order.orderId}`);
                
                // Simulate shipping process
                setTimeout(() => {
                    console.log(`Order ${order.orderId} shipped!`);
                    channel.ack(message);
                }, 3000);
            }
        });
    } catch (error) {
        console.error('Error:', error);
    }
}

processShipping();