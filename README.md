# GlobalBooks SOA Project

This project is a Service-Oriented Architecture (SOA) implementation for a fictional online bookstore called "GlobalBooks". It is composed of four distinct services that work together to handle the core functionalities of an e-commerce platform for books.

-----

## Services

The project is divided into the following microservices:

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