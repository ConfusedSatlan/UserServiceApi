# UserServiceApi
Welcome to my test task of creating api from Clear Solutions company
# User Service API

This repository contains a Spring Boot application for managing user data through a RESTful API.

## Requirements

1. **Model**: The application uses a User model with the following fields:
   - Email (required, validated against email pattern)
   - First name (required)
   - Last name (required)
   - Birth date (required, must be earlier than the current date)
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

## API Endpoints

### Create User
- **Method**: POST
- **Endpoint**: `/v1/users`
- **Request Body**:
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
### Update User

- **Method**: PUT
- **Endpoint**: `/v1/users/{userId}`
- **Request Body**:
  ```json
    {
      "address": "456 Elm Street"
    }
   ```
- **Response Body**:
  ```json
    {
    "email": "example@example.com",
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
```json
    [
      {
        "email": "example1@example.com",
        "firstName": "Alice",
        "lastName": "Smith",
        "birthDate": "1990-05-15",
        "address": "789 Maple Avenue",
        "phoneNumber": "9876543210"
      },
      {
        "email": "example2@example.com",
        "firstName": "Bob",
        "lastName": "Johnson",
        "birthDate": "1995-10-20",
        "address": null,
        "phoneNumber": null
      }
    ]
 ```
### Setting Up
1. Clone this repository to your local machine.
2. Navigate to the project directory.
3. Open the project in your preferred IDE.
4. Build and run the application.

### Testing
Unit tests can be executed using Maven:
```bash
    mvn test
 ```

  
