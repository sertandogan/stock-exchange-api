# Stock Exchange API Documentation

Welcome to the Stock Exchange API documentation! This API allows you to manage stock exchanges, stocks, user authentication, and perform related operations.

## Getting Started

1. Clone the repository: `git clone <repository-url>`
2. Configure your database settings in `application.yml`.

## Build and Run

To build and run the application, follow these steps:

1. Navigate to the project directory: `cd stock-exchange-api`
2. Build the application: `mvn clean install`
3. Run the application: `mvn spring-boot:run`

## Using Swagger for API Documentation

1. Once the application is running, you can access the Swagger UI documentation at: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
2. Use Swagger UI to explore and test the various endpoints provided by the API.
3. Authenticate using the `/api/auth/authenticate` endpoint to obtain an access token.
4. Use the provided access token in the `Authorize` button on Swagger UI to make authenticated requests.
