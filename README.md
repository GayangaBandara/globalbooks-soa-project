# GlobalBooks SOA Project

This project is a Service-Oriented Architecture (SOA) implementation for a fictional online bookstore called "GlobalBooks". It is composed of four distinct services that work together to handle the core functionalities of an e-commerce platform for books.

-----

## Services

The project is divided into the following microservices:

## Testing Guide

This guide provides comprehensive instructions for testing all components of the GlobalBooks SOA project, including SOAP services, REST APIs, and asynchronous messaging.

### 1. SOAP UI Testing Guide for catalog-service

#### Prerequisites
- Install SOAP UI (Open Source or Professional version)
- Ensure catalog-service is running on port 8080

#### Steps to Create and Configure Test Case

1. Create New SOAP Project
   - Open SOAP UI
   - Click "Create New SOAP Project"
   - Project Name: `GlobalBooks-Catalog-Service`
   - Initial WSDL: `http://localhost:8080/catalog-service/catalog?wsdl`
   - Click "OK"

2. Create Test Suite
   - Right-click on the `CatalogService` interface
   - Select "Generate Test Suite"
   - Name it "CatalogService-TestSuite"

3. Configure getBook Test Case
   - Expand the test suite and locate the `getBook` test case
   - Double-click to open the request editor
   - Replace the request content with:
     ```xml
     <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cat="http://catalog.globalbooks.com">
        <soapenv:Header/>
        <soapenv:Body>
           <cat:getBook>
              <bookId>soa-bk-101</bookId>
           </cat:getBook>
        </soapenv:Body>
     </soapenv:Envelope>
     ```

4. Add Assertions
   - Right-click on the test step
   - Select "Add Assertion"
   
   a. HTTP Status Assertion:
   - Select "Property Content" → "HTTP Status Code"
   - Enter "200" as the expected value
   
   b. Contains Assertion:
   - Select "Property Content" → "Response Contains"
   - Enter "SOA for Dummies" in the content field
   
   c. XPath Match Assertion:
   - Select "XPath Match"
   - XPath Expression: `//return/price`
   - Expected Value: `39.99`

5. Run the Test
   - Click the green "Play" button to run the test
   - Verify all assertions pass

### 2. Postman Test Scripts for orders-service

#### POST /orders Request Test Script
```javascript
// Assert status code is 201
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

// Verify response has id field and store it
pm.test("Response contains order id", function () {
    const response = pm.response.json();
    pm.expect(response.id).to.exist.and.to.not.be.empty;
    // Store id for next request
    pm.collectionVariables.set("new_order_id", response.id);
});
```

#### GET /orders/{{new_order_id}} Request Test Script
```javascript
// Assert status code is 200
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Verify order id matches stored value
pm.test("Order ID matches stored value", function () {
    const response = pm.response.json();
    const storedId = pm.collectionVariables.get("new_order_id");
    pm.expect(response.id).to.equal(storedId);
});
```

### 3. Asynchronous Testing Guide for Messaging

#### Prerequisites
- RabbitMQ Management UI accessible (default: http://localhost:15672)
- All services running (orders-service, payments-service, shipping-service)
- Default credentials: guest/guest

#### Step-by-Step Testing Guide

1. Initial Setup Verification
   - Log into RabbitMQ Management UI
   - Verify `orders_exchange` exists
   - Confirm both `payments_queue` and `shipping_queue` are bound to `orders_exchange`
   - Note the initial "Ready" message count (should be 0 for both queues)

2. Create Test Order
   - Use Postman to send a POST request to orders-service
   - Keep the RabbitMQ Management UI open in another tab
   - Note: Screenshot the initial state (Step 1) before proceeding

3. Observe Message Flow
   a. Orders Exchange:
   - Navigate to the Exchanges tab
   - Select `orders_exchange`
   - Verify message appears in the exchange (check message rates)
   - Screenshot the exchange metrics showing message activity

   b. Queue Routing:
   - Switch to the Queues tab
   - Observe `payments_queue` and `shipping_queue`
   - Verify both queues receive a message
   - Screenshot showing both queues with messages

4. Message Consumption
   - Monitor the "Ready" message count for both queues
   - Verify counts return to 0 as services process messages
   - Screenshot final state showing processed messages

#### Required Screenshots for Report
1. Initial RabbitMQ state (empty queues)
2. Message activity in orders_exchange
3. Messages present in both queues
4. Final state showing processed messages (0 ready messages)

Note: All screenshots should be stored in a dedicated `/test-artifacts` folder with clear naming conventions for inclusion in the final report.

### 1\. Catalog Service

  * **Description:** Manages the book catalog. This service is responsible for providing information about books, such as title, author, and price.
  * **Technology:** It is a Java-based SOAP web service using JAX-WS.

### 2\. Orders Service

  * **Description:** Handles the creation and management of customer orders.
  * **Technology:** This is a Spring Boot application written in Java. It uses AMQP for asynchronous communication with other services.

### 3\. Payments Service

  * **Description:** Processes payments for the orders.
  * **Technology:** This is a Node.js service that uses `amqplib` for messaging, indicating asynchronous communication with the orders and shipping services.

### 4\. Shipping Service

  * **Description:** Manages the shipping and delivery of book orders.
  * **Technology:** This is a Node.js service that, like the payments service, uses `amqplib` for asynchronous communication.

-----

## Technologies Used

  * **Backend:** Java, Node.js
  * **Frameworks:** Spring Boot, JAX-WS (Metro)
  * **Messaging:** AMQP (likely RabbitMQ)
  * **Build Tools:** Maven (for Java services), npm (for Node.js services)

-----

## Getting Started

To get the project up and running, you will need to start each of the four services.

### Prerequisites

  * Java 17 or later
  * Maven
  * Node.js and npm
  * An AMQP message broker (like RabbitMQ)

### Running the Services

1.  **Catalog Service:**

      * Navigate to the `catalog-service` directory.
      * Run the service using the Jetty Maven plugin:
        ```bash
        mvn jetty:run
        ```
      * The service will be available at `http://localhost:8080/catalog-service`.

2.  **Orders Service:**

      * Navigate to the `orders-service` directory.
      * Run the service using the Spring Boot Maven plugin:
        ```bash
        mvn spring-boot:run
        ```

3.  **Payments Service:**

      * Navigate to the `payments-service` directory.
      * Install the dependencies:
        ```bash
        npm install
        ```
      * Start the service:
        ```bash
        node index.js
        ```

4.  **Shipping Service:**

      * Navigate to the `shipping-service` directory.
      * Install the dependencies:
        ```bash
        npm install
        ```
      * Start the service:
        ```bash
        node index.js
        ```