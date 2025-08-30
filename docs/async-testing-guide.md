# Testing Guide: Asynchronous Messaging Workflow

## Prerequisites
- RabbitMQ server running (default port 5672)
- RabbitMQ Management UI accessible (http://localhost:15672)
- All services running:
  - catalog-service (Tomcat - port 8080)
  - orders-service (Spring Boot - port 8081)
  - payments-service (Node.js - port 8082)
  - shipping-service (Node.js - port 8083)

## Test Scenario: Order Creation and Processing

### 1. Initial State Verification
1. Open RabbitMQ Management UI (http://localhost:15672)
2. Login with guest/guest credentials
3. Navigate to "Queues and Streams" tab
4. **Document Initial State:**
   - Screenshot the empty queues showing:
     * payments_queue (0 messages)
     * shipping_queue (0 messages)

### 2. Create New Order
1. Use Postman to send POST request to:
   ```
   POST http://localhost:8081/orders
   Content-Type: application/json
   
   {
     "userId": "customer123",
     "items": [
       {
         "isbn": "soa-bk-101",
         "quantity": 1
       }
     ]
   }
   ```
2. **Document Order Creation:**
   - Screenshot the successful order response
   - Note the order ID for reference

### 3. Message Flow Verification

#### A. Exchange Verification
1. In RabbitMQ Management UI:
   - Go to "Exchanges" tab
   - Click on "orders_exchange"
   - **Document Exchange Activity:**
     * Screenshot showing message rate graph
     * Should show spike in publish rate

#### B. Queue Routing Verification
1. Immediately check "Queues and Streams" tab
2. **Document Message Routing:**
   - Screenshot showing:
     * payments_queue (1 message)
     * shipping_queue (1 message)

#### C. Message Consumption Verification
1. Monitor queue statistics:
   - Watch "Ready" message count
   - Should decrease to 0 as services process messages
2. **Document Processing:**
   - Screenshot final state showing:
     * Both queues empty (0 messages)
     * Message rate graphs showing processing activity

## Test Artifacts Checklist

- [ ] Screenshot 1: Initial empty queues
- [ ] Screenshot 2: Successful order creation response
- [ ] Screenshot 3: Exchange message activity
- [ ] Screenshot 4: Messages in both queues
- [ ] Screenshot 5: Final state (processed messages)

## Additional Verification Steps

### Payment Service Verification
1. Check payments-service logs:
   ```
   Message received from payments_queue
   Processing payment for order: [order-id]
   Payment processed successfully
   ```

### Shipping Service Verification
1. Check shipping-service logs:
   ```
   Message received from shipping_queue
   Creating shipment for order: [order-id]
   Shipment created successfully
   ```

## Test Report Requirements

Your test report should include:
1. All screenshots as listed in the checklist
2. Timestamp for each test step
3. Any errors or unexpected behavior observed
4. Message processing times for both services
5. Overall workflow completion time

Note: Store all screenshots in the `/test-artifacts` directory using the following naming convention:
- async_test_1_initial_state.png
- async_test_2_order_creation.png
- async_test_3_exchange_activity.png
- async_test_4_queue_messages.png
- async_test_5_final_state.png
