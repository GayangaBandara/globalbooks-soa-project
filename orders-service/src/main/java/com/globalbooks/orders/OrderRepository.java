package com.globalbooks.orders;

import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository {
    private final ConcurrentHashMap<String, Order> orders = new ConcurrentHashMap<>();
    
    public Order save(Order order) {
        orders.put(order.getOrderId(), order);
        return order;
    }
    
    public Order findById(String orderId) {
        return orders.get(orderId);
    }
}