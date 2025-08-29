package com.globalbooks.orders;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Calculate total (in real app, call CatalogService)
        double total = order.getItems().stream()
                .mapToInt(item -> item.getQuantity() * 29.99) // Mock price
                .sum();
        order.setTotalAmount(total);
        
        Order savedOrder = orderRepository.save(order);
        
        // Send to RabbitMQ
        rabbitTemplate.convertAndSend("orders.payments", savedOrder);
        
        return ResponseEntity.ok(savedOrder);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
}