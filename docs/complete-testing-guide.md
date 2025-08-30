# GlobalBooks SOA Project - Complete Testing Guide

## Table of Contents
1. [Environment Setup](#1-environment-setup)
2. [Testing Prerequisites](#2-testing-prerequisites)
3. [SOAP UI Testing (CatalogService)](#3-soap-ui-testing-catalogservice)
4. [Postman Testing (OrdersService)](#4-postman-testing-ordersservice)
5. [Asynchronous Testing (RabbitMQ)](#5-asynchronous-testing-rabbitmq)
6. [End-to-End Testing](#6-end-to-end-testing)
7. [Security Testing](#7-security-testing)
8. [Test Documentation](#8-test-documentation)

## 1. Environment Setup

### 1.1 Required Software
- Java JDK 17
- Apache Tomcat 9.x
- Node.js 18.x or later
- SOAP UI (Open Source or Professional)
- Postman
- RabbitMQ Server with Management Plugin
- Apache ODE (for BPEL workflow)

### 1.2 Service Ports
```
CatalogService  : 8080 (Tomcat)
OrdersService   : 8081 (Spring Boot)
PaymentsService : 8082 (Node.js)
ShippingService : 8083 (Node.js)
RabbitMQ       : 5672 (AMQP) / 15672 (Management)
```

### 1.3 Initial Setup
1. Start all required servers:
```powershell
# Start RabbitMQ
net start RabbitMQ

# Start Tomcat (CatalogService)
cd %CATALINA_HOME%\bin
startup.bat

# Start OrdersService
cd orders-service
mvn spring-boot:run

# Start PaymentsService
cd payments-service
npm start

# Start ShippingService
cd shipping-service
npm start
```

2. Verify services are running:
```
CatalogService  : http://localhost:8080/catalog-service/catalog?wsdl
OrdersService   : http://localhost:8081/actuator/health
RabbitMQ UI    : http://localhost:15672 (guest/guest)
```

## 2. Testing Prerequisites

### 2.1 Test Data Setup
Create test books in catalog:
```xml
<book>
    <id>soa-bk-101</id>
    <title>SOA for Dummies</title>
    <price>39.99</price>
    <author>John Smith</author>
</book>
```

### 2.2 Import Test Collections
1. SOAP UI Project:
   - Open SOAP UI
   - Import `catalog-service-tests.xml`

2. Postman Collection:
   - Open Postman
   - Import `postman-collection.json`

## 3. SOAP UI Testing (CatalogService)

### 3.1 WSDL Verification
1. Load WSDL:
   - Create New SOAP Project
   - Enter WSDL URL: `http://localhost:8080/catalog-service/catalog?wsdl`
   - Verify operations: `getBook`

### 3.2 Test Case: Get Book Details
1. Configure Request:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:cat="http://catalog.globalbooks.com">
   <soapenv:Header/>
   <soapenv:Body>
      <cat:getBook>
         <bookId>soa-bk-101</bookId>
      </cat:getBook>
   </soapenv:Body>
</soapenv:Envelope>
```

2. Add Assertions:
   - HTTP Status: 200
   - Contains: "SOA for Dummies"
   - XPath Match: `//return/price` equals "39.99"

3. Run Test:
   - Execute test case
   - Verify all assertions pass
   - Save test results

## 4. Postman Testing (OrdersService)

### 4.1 Environment Setup
Create environment variables:
```json
{
  "baseUrl": "http://localhost:8081",
  "new_order_id": ""
}
```

### 4.2 Test Case: Create Order
1. Request:
```http
POST {{baseUrl}}/orders
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

2. Test Script:
```javascript
// Verify status code
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

// Verify response has id
pm.test("Response contains order id", function () {
    const response = pm.response.json();
    pm.expect(response.id).to.exist.and.to.not.be.empty;
    pm.collectionVariables.set("new_order_id", response.id);
});
```

### 4.3 Test Case: Get Order
1. Request:
```http
GET {{baseUrl}}/orders/{{new_order_id}}
```

2. Test Script:
```javascript
// Verify status code
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Verify order id
pm.test("Order ID matches", function () {
    const response = pm.response.json();
    const storedId = pm.collectionVariables.get("new_order_id");
    pm.expect(response.id).to.equal(storedId);
});
```

## 5. Asynchronous Testing (RabbitMQ)

### 5.1 Pre-test Setup
1. Clear all queues:
   - Open RabbitMQ Management UI
   - Navigate to Queues
   - Purge both `payments_queue` and `shipping_queue`

### 5.2 Test Sequence
1. Initial State Verification:
   - Screenshot empty queues
   - Record initial message counts

2. Create Test Order:
   - Use Postman POST /orders request
   - Save order ID and response

3. Monitor Message Flow:
   a. Exchange Check:
      - View `orders_exchange`
      - Verify message publication
      - Screenshot message rates

   b. Queue Check:
      - Verify message in `payments_queue`
      - Verify message in `shipping_queue`
      - Screenshot queue states

   c. Consumption Check:
      - Monitor queue counts
      - Verify messages processed
      - Screenshot final state

### 5.3 Service Log Verification
1. PaymentsService Logs:
```
Processing payment for order: [order-id]
Payment processed successfully
```

2. ShippingService Logs:
```
Creating shipment for order: [order-id]
Shipment created successfully
```

## 6. End-to-End Testing

### 6.1 Complete Order Flow
1. Get book details (SOAP)
2. Create order (REST)
3. Verify payment processing (RabbitMQ)
4. Verify shipping creation (RabbitMQ)
5. Check final order status (REST)

### 6.2 Error Scenarios
1. Invalid book ID
2. Insufficient stock
3. Payment failure
4. Shipping failure

## 7. Security Testing

### 7.1 WS-Security (CatalogService)
1. Verify security headers
2. Test without security token
3. Test with invalid token
4. Test with expired token

### 7.2 OAuth2 (OrdersService)
1. Test without token
2. Test with invalid token
3. Test with expired token
4. Test with insufficient scope

## 8. Test Documentation

### 8.1 Required Screenshots
1. Initial state
2. Order creation
3. Message exchange activity
4. Queue states
5. Final processed state

### 8.2 Test Report Structure
1. Test Environment
2. Test Cases Executed
3. Test Results Summary
4. Screenshots
5. Error Logs (if any)
6. Performance Metrics

### 8.3 File Organization
```
/test-artifacts/
  ├── soap-ui/
  │   ├── catalog-test-results.xml
  │   └── screenshots/
  ├── postman/
  │   ├── order-test-results.json
  │   └── screenshots/
  └── async/
      ├── rabbitmq-test-results.md
      └── screenshots/
```

## Test Execution Checklist

- [ ] Environment setup verified
- [ ] All services running
- [ ] SOAP UI tests completed
- [ ] Postman tests completed
- [ ] Async messaging tests completed
- [ ] Security tests completed
- [ ] Screenshots captured
- [ ] Test results documented
- [ ] Error scenarios tested
- [ ] Performance metrics collected

## Notes

1. Always run tests in order:
   - SOAP tests first
   - REST tests second
   - Async tests last

2. Clean up after testing:
   - Purge all queues
   - Reset test data
   - Stop all services

3. Document any deviations from expected behavior

4. Keep all test artifacts organized by date and test run

For detailed execution instructions of each test component, refer to their respective sections above.
