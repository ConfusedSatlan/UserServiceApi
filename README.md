# UserServiceApi
Welcome to my test task of creating api from Clear Solutions company
### Setting Up
1. Clone this repository to your local machine.
2. Navigate to the project directory.
3. Open the project in your preferred IDE.
4. Build and run the application.

## Accessing the API

You can access the API using the following base URL:

[Base URL: http://localhost:8080/api/**](http://localhost:8080/api/v1/users)

[SwaggerApi URL: http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html#/)

Below are the available endpoints:

- **Create User**: `POST /v1/users`
- **Update User**: `PATCH /v1/users/{userId}`
- **Update All User Fields**: `PUT /v1/users/{userId}`
- **Delete User**: `DELETE /v1/users/{userId}`
- **Search Users by Birth Date Range**: `GET /v1/users/search?fromDate={startDate}&toDate={endDate}`


This repository contains a Spring Boot application for managing user data through a RESTful API.

## Requirements

1. **Model**: The application uses a User model with the following fields:
   - Email (required, validated against email pattern)
   - First name (required)
   - Last name (required)
   - Birth date (required, must be earlier than the current date, more than [18] years old(taken from properties file))
   - Address (optional)
   - Phone number (optional)

2. **Functionality**: The API provides the following functionality:
   - Create user
   - Update user fields
   - Delete user
   - Search users by birth date range

3. **Unit Tests**: The code is covered by unit tests using Spring.

4. **Error Handling**: The API has error handling for RESTful requests.

5. **API Responses**: Responses are in JSON format.

6. **Database**: Data persistence layer is not required.

## 1. Model
- **DTO**
  - input: CreateRequestUserDto.java
  - output: ResponseErrorDto.java
  - output(input): UserDto.java
  
- **Entity**
  - User.java


## 2. API Endpoints (Functionality)

### Create User
- **Method**: POST
- **Endpoint**: `/v1/users`
- **Request Body**:
  - CreateRequestUserDto.java
    ```json
    {
      "email": "example@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "birthDate": "2000-01-01",
      "address": "123 Main Street",
      "phoneNumber": "1234567890"
    }
    ```
 - **Response Body**:
   - UserDto.java
      ```json
        {
          "id" : "1",
          "email": "example@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "birthDate": "2000-01-01",
          "address": "123 Main Street",
          "phoneNumber": "1234567890"
        }
       ```
### Update User

- **Method**: PATCH
- **Endpoint**: `/v1/users/{userId}`
- **Request Body**:
  - UserDto.java
    ```json
      {
        "address": "456 Elm Street"
      }
     ```
- **Response Body**:
  - UserDto.java
    ```json
      {
      "id" : "1",
      "email": "example@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "birthDate": "2000-01-01",
      "address": "456 Elm Street",
      "phoneNumber": "1234567890"
      }
     ```
### Update All User Fields

- **Method**: PUT
- **Endpoint**: `/v1/users/{userId}`
- **Request Body**: Must contain all fields
  - CreateRequestUserDto.java
    ```json
    {
      "email": "updated@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "birthDate": "2000-01-01",
      "address": "456 Elm Street",
      "phoneNumber": "1234567890"
    }
     ```
- **Response Body**:
  - UserDto.java
    ```json
    {
    "id" : "1",
    "email": "updated@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "2000-01-01",
    "address": "456 Elm Street",
    "phoneNumber": "1234567890"
    }  
    ```

### Delete User

- **Method**: DELETE
- **Endpoint**: `/v1/users/{userId}`
- **Response**: 
  - message: String
      ```json
          "User with ID {userId} has been deleted successfully."
       ```
### Search Users by Birth Date Range

- **Method**: GET
- **Endpoint**: `/v1/users/search`
- **Query Parameters**:
- `fromDate`: Start date of the birth date range (format: YYYY-MM-DD)
- `toDate`: End date of the birth date range (format: YYYY-MM-DD)

- **Response**: 
  - List of UserDto.java
      ```json
          [
            {
              "id" : "1",
              "email": "example1@example.com",
              "firstName": "Alice",
              "lastName": "Smith",
              "birthDate": "1990-05-15",
              "address": "789 Maple Avenue",
              "phoneNumber": "9876543210"
            },
            {
              "id" : "2",
              "email": "example2@example.com",
              "firstName": "Bob",
              "lastName": "Johnson",
              "birthDate": "1995-10-20",
              "address": null,
              "phoneNumber": null
            }
          ]
       ```
### 3. Testing
Unit tests can be executed using Maven:
```bash
    mvn test
 ```
### 4. Code has error handling for REST

Error handling for REST endpoints has been implemented using a centralized exception handler. The `CommonExceptionHandler` class extends `ResponseEntityExceptionHandler` to handle various types of exceptions. Here's an overview of the implemented error handling:

- **Exception Handling for Generic Exceptions**: Any uncaught exceptions are handled by the `handleNotFound` method, which returns an internal server error response.
- **Exception Handling for NullPointerException**: This handler specifically deals with `NullPointerException` and returns a response with a 404 status code indicating that the resource was not found.
- **Exception Handling for UserServiceApiException**: Custom exceptions of type `UserServiceApiException` are handled separately. The exception code is used to determine the HTTP status code of the response.
- **Method Argument Validation**: Validation errors in request payloads are handled by the `handleMethodArgumentNotValid` method, which returns a response with a 400 status code and a list of validation error messages.

### 5. API responses are in JSON format

The API responses are formatted in JSON format to ensure consistency and interoperability. All endpoints return JSON data structures, making it easy for clients to parse and process the responses.

### 6. Database

A simple in-memory repository (`UserRepository`) has been implemented to simulate data storage without the need for an actual database. This repository stores `User` entities in a list and provides basic CRUD operations for managing user data. The repository is injected into the service layer to facilitate data manipulation.
