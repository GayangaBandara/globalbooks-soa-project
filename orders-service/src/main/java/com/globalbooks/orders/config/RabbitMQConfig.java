package com.globalbooks.orders.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Bean
    public Exchange ordersExchange() {
        return ExchangeBuilder.fanoutExchange("orders_exchange")
                            .durable(true)
                            .build();
    }
    
    @Bean
    public Queue paymentsQueue() {
        return QueueBuilder.durable("payments_queue")
                          .build();
    }
    
    @Bean
    public Queue shippingQueue() {
        return QueueBuilder.durable("shipping_queue")
                          .build();
    }
    
    @Bean
    public Binding paymentsBinding(Queue paymentsQueue, Exchange ordersExchange) {
        return BindingBuilder.bind(paymentsQueue)
                           .to(ordersExchange)
                           .with("*")
                           .noargs();
    }
    
    @Bean
    public Binding shippingBinding(Queue shippingQueue, Exchange ordersExchange) {
        return BindingBuilder.bind(shippingQueue)
                           .to(ordersExchange)
                           .with("*")
                           .noargs();
    }
}
