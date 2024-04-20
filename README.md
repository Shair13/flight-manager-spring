# Flight Manager REST API

## Description

**A simple application that allows you to manage passengers and flights**

## How to Run the Application

### Steps:

1. **Clone the Repository:** Clone this repository to your local machine using the following command in your terminal:

   **HTTPS**
    ```
    git clone https://github.com/Shair13/flight-manager.git
    ```
   **SSH**
   ```
    git clone git@github.com:Shair13/flight-manager.git
    ```

2. **Build the Application:** Navigate to the repository folder and use the Maven command to build the application:

    ```bash
    mvn package
    ```

3. **Run the Application:** After building the application, navigate to the `target` folder and run the application using the following command:

    ```bash
    cd target
    java -jar flight-manager-0.0.1-SNAPSHOT.jar
    ```

# API Documentation

## ● Adding a New Flight

### **Endpoint:** `POST` `/flights`

<details>
<summary>Details - click to open</summary>

**Description:**
Adds a new flight to the system.

**Request Body:**
- Format: JSON
- Sample Request Body:
```json
{
  "number": "3",
  "departure": "2024-07-23T08:00:00",
  "route": "Warsaw - Chicago",
  "availableSeats": 120
}
```
**Data types:**
- int number (greater than 0)
- LocalDateTime departure (cannot be past)
- String route (must not be empty)
- int availableSeats (greater or equal 0)

**Response:**
- Status Code: **201 Created**
- Body: Object representing the added flight.
- Sample Response Body:

```json
{
  "id": 1,
  "number": 3,
  "route": "Warsaw - London",
  "departure": "2024-07-23T08:00:00",
  "availableSeats": 96,
  "passengers": null
}
```

**Errors:**

- Incorrect value of any field. Example of incorrect values for all fields:

```json
{
    "departure": "Must be a future date.",
    "number": "Flight number must be greater than 0.",
    "route": "Route cannot be an empty field.",
    "availableSeats": "Available seats must be not be less than 0."
}
```

- Incorrect type of value for flight number:

```json
{
"error": "JSON parse error: Cannot deserialize value of type `int` from String 'd': not a valid `int` value"
}
```

</details>

## ● Get all Flights

### **Endpoint:** `GET` `/flights`
<details>
<summary>Details - click to open</summary>

**Description:**
Returns all flights from database.

**Possible URL parameters:**
- sort
- page
- size

**Request Body:**
Empty.

**Response:**
- Status Code: **200 OK**
- Sample Response Body:

```json
[
   {
      "id": 1,
      "number": 10,
      "route": "Warsaw - Berlin",
      "departure": "2024-04-30T12:35:00",
      "availableSeats": 140,
      "passengers": []
   },
   {
      "id": 2,
      "number": 27,
      "route": "Palermo - Warsaw",
      "departure": "2024-05-01T17:00:00",
      "availableSeats": 130,
      "passengers": []
   },
   {
      "id": 3,
      "number": 13,
      "route": "Warsaw - Oslo",
      "departure": "2024-06-01T12:00:00",
      "availableSeats": 129,
      "passengers": [
         {
            "id": 2,
            "name": "Han",
            "surname": "Solo",
            "phone": "123 456 789"
         }
      ]
   }
]
```
</details>

## ● Get Flight by id

### **Endpoint:** `GET` `/flights/{id}`
<details>
<summary>Details - click to open</summary>

**Description:**
Returns flight by id from database.

**Request Body:**
Empty.

**Response:**
- Status Code: **200 OK**
- Sample Response Body:

```json
{
   "id": 3,
   "number": 13,
   "route": "Warsaw - Oslo",
   "departure": "2024-06-01T12:00:00",
   "availableSeats": 129,
   "passengers": [
      {
         "id": 2,
         "name": "Han",
         "surname": "Solo",
         "phone": "123 456 789"
      }
   ]
}
```

**Errors:**

- When flight does not exist:
```json
{
   "error": "Flight with id = 14 not found"
}
```
</details>

## ● Updates existing Flight

### **Endpoint:** `PUT` `/flights/{id}`

<details>
<summary>Details - click to open</summary>

**Description:**
Updates existing flight.

**Request Body:**
- Format: JSON
- Sample Request Body:
```json
{
   "number": "4",
   "departure": "2024-07-23T08:00:00",
   "route": "Warsaw - Chicago",
   "availableSeats": 120
}
```
**Data types:**
- int number (greater than 0)
- LocalDateTime departure (cannot be past)
- String route (must not be empty)
- int availableSeats (greater or equal 0)

**Response:**
- Status Code: **200 OK**
- Body: Object representing the updated flight.
- Sample Response Body:

```json
{
   "id": 3,
   "number": 4,
   "route": "Warsaw - Chicago",
   "departure": "2024-07-23T08:00:00",
   "availableSeats": 120,
   "passengers": [
      {
         "id": 2,
         "name": "Han",
         "surname": "Solo",
         "phone": "123 456 789"
      }
   ]
}
```

**Errors:**

- Incorrect value of any field. Example of incorrect values for all fields:

```json
{
    "departure": "Must be a future date.",
    "number": "Flight number must be greater than 0.",
    "route": "Route cannot be an empty field.",
    "availableSeats": "Available seats must be not be less than 0."
}
```

- Incorrect type of value for flight number:

```json
{
"error": "JSON parse error: Cannot deserialize value of type `int` from String 'd': not a valid `int` value"
}
```
- Flight not found:

```json
{
    "error": "Flight with id = 39 not found"
}
```

</details>

## ● Adds Passenger to Flight

### **Endpoint:** `PATCH` `/flights/add/{flightId}/{passengerId}`

<details>
<summary>Details - click to open</summary>

**Description:**
Adds a passenger to the selected flight.

**Request Body:**
Empty.

**Response:**
- Status Code: **200 OK**
- Body: Object representing the flight with added passengers.
- Sample Response Body:

```json
{
   "id": 2,
   "number": 13,
   "route": "Warsaw - Oslo",
   "date": "2024-06-01T12:00:00",
   "availableSeats": 127,
   "passengers": [
      {
         "id": 1,
         "name": "Han",
         "surname": "Solo",
         "phone": "123 456 789"
      },
      {
         "id": 2,
         "name": "Leia",
         "surname": "Organa",
         "phone": "789 456 123"
      }
   ]
}
```

**Errors:**

- Flight not found:

```json
{
    "error": "Flight with id = 39 not found"
}
```

- Passenger not found:

```json
{
   "error": "Passenger with id = 124 not found"
}
```

- Passenger is already added:

```json
{
   "error": "Passenger with id 2 is already added to flight number LO13."
}
```

- No available seats:

```json
{
   "error": "No available seats on flight number LO13."
}
```

</details>

## ● Removes Passenger from Flight

### **Endpoint:** `PATCH` `/flights/delete/{flightId}/{passengerId}`

<details>
<summary>Details - click to open</summary>

**Description:**
Removes a passenger from the selected flight.

**Request Body:**
Empty.

**Response:**
- Status Code: **200 OK**
- Body: Object representing the flight with passengers who left.
- Sample Response Body:

```json
{
   "id": 2,
   "number": 13,
   "route": "Warsaw - Oslo",
   "date": "2024-06-01T12:00:00",
   "availableSeats": 128,
   "passengers": [
      {
         "id": 2,
         "name": "Leia",
         "surname": "Organa",
         "phone": "789 456 123"
      }
   ]
}
```

**Errors:**

- Flight not found:

```json
{
    "error": "Flight with id = 39 not found"
}
```

- Passenger not found:

```json
{
   "error": "Passenger with id = 124 not found"
}
```

- Passenger not found on selected flight:

```json
{
   "error": "Passenger with id = 1 not found on flight number LO13."
}
```

</details>

## ● Removes Flight from database

### **Endpoint:** `DELETE` `flights/{id}`

<details>
<summary>Details - click to open</summary>

**Description:**
Removes a Flight from database.

**Request Body:**
Empty.

**Response:**
- Status Code: **204 No Content**
- Body: Empty.
- Sample Response Body:

**Errors:**

- Flight not found:

```json
{
    "error": "Flight with id = 39 not found"
}
```

</details>