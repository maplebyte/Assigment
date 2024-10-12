# User Management API

## Overview

This is a RESTful API for managing users, built using Java 17 and Spring Boot. It allows for the creation, retrieval, updating, and deletion of users. The service stores the following user information:
- Username
- First Name
- Last Name
- Email (should be unique)
- Phone Number

The application is designed with two database profiles: **PostgreSQL** and **H2**. PostgreSQL is used in production environments, while H2 is an in-memory database for testing purposes. PostgreSQL is configured as the default profile and integrated into the Docker Compose setup.

## Features

- **Create User:** Add a new user with specified details.
- **List Users:** Retrieve all users stored in the database.
- **Get User by ID:** Fetch details of a specific user by their ID.
- **Update User:** Modify details of an existing user.
- **Delete User:** Remove a user from the system.

## API Endpoints

### 1. Create a new user
- **URL:** `POST /users`
- **Request Body Example:**
    ```json
    {
      "username": "johndoe",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phoneNumber": "+123456789"
    }
    ```
- **Response Example:**
    ```json
    {
      "status": 201,
      "message": "User created successfully",
      "data": <user_id>
    }
    ```
  
### 2. List all users
- **URL:** `GET /users`
- **Response Example:**
    ```json
    {
      "status": 200,
      "message": "Users retrieved successfully",
      "data": [
        {
          "id": 1,
          "username": "johndoe",
          "firstName": "John",
          "lastName": "Doe",
          "email": "john.doe@example.com",
          "phoneNumber": "+123456789"
        }
      ]
    }
    ```

### 3. Get user by ID
- **URL:** `GET /users/{id}`
- **Response Example:**
    ```json
    {
      "status": 200,
      "message": "User retrieved successfully",
      "data": {
        "id": 1,
        "username": "johndoe",
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "+123456789"
      }
    }
    ```

### 4. Update an existing user
- **URL:** `PUT /users/{id}`
- **Request Body Example:**
    ```json
    {
      "username": "johnnydoe",
      "firstName": "Johnny",
      "lastName": "Doe",
      "email": "johnny.doe@example.com",
      "phoneNumber": "+987654321"
    }
    ```
- **Response Example:**
    ```json
    {
      "status": 200,
      "message": "User updated successfully",
      "data": {
        "id": 1,
        "username": "johnnydoe",
        "firstName": "Johnny",
        "lastName": "Doe",
        "email": "johnny.doe@example.com",
        "phoneNumber": "+987654321"
      }
    }
    ```

### 5. Delete a user
- **URL:** `DELETE /users/{id}`
- **Response Example:**
    ```json
    {
      "status": 204,
      "message": "User deleted successfully"
    }
    ```

## Technologies Used

- **Java 17**: The application is built using the latest long-term support version of Java.
- **Spring Boot**: Framework used for creating stand-alone, production-grade Spring-based applications.
- **PostgreSQL**: Default database used in production and Docker environments.
- **H2**: In-memory database used for testing purposes.
- **Docker**: Used to containerize the application and simplify deployment.

## Profiles

### PostgreSQL Profile (Default)
- The application uses PostgreSQL as the primary database in production and Docker environments.
- **Docker Compose** is configured to start a PostgreSQL container alongside the application.

### H2 Profile
- H2 is included for testing purposes. It's an in-memory database that doesn't require external configuration, making it easy to run the application locally without needing a full database setup.

## Running the Application

### With Docker Compose (PostgreSQL Profile)

To run the application with Docker Compose (using PostgreSQL):

1. **Ensure Docker is installed and running on your machine.**
2. **Navigate to the root of the project directory.**
3. Run the following command to start the application along with the PostgreSQL database:

   ```bash
   docker-compose up
   ```
   This will start the application on port `28852` and a PostgreSQL container on port `5432`. The API will be accessible at `http://localhost:28852`.
4. To stop the containers without losing any data, run:
   ```bash
   docker-compose stop
   ```
### Running Locally (H2 Profile)

To run the application locally using H2:

1. **Ensure Java 17 is installed on your machine.**

2. **Navigate to the root of the project directory.**

3. Open the `application.properties` file located in the `src/main/resources` directory, and change the active profile to H2 by adding or updating the following line:

    ```properties
    spring.profiles.active=h2
    ```

4. After saving the changes, run the following command to start the application:

    ```bash
    mvn spring-boot:run
    ```

   This will start the application on port `28852`, and the in-memory H2 database will be used. The API will be accessible at:

    ```bash
    http://localhost:28852
    ```

5. You can access the H2 database console at the following URL:

    ```bash
    http://localhost:28852/h2-console
    ```

### Testing

The application includes unit tests. To run the tests:

```bash
./mvnw test
```

###  Notes
* Ensure Docker is installed for using the PostgreSQL profile.
* The default profile is PostgreSQL, so it will automatically try to connect to a PostgreSQL instance unless the profile is explicitly switched to H2.
