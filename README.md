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

3. **Run the Application:** After building the application, navigate to the `target` folder and run the application
   using the following command:

    ```bash
    cd target
    java -jar flight-manager-0.0.1-SNAPSHOT.jar
    ```
4. **Access the Application:** Once the application is running, you can access it through your web browser at the
   following address:

   ```
   http://localhost:8080
   ```

# API Documentation

The documentation is also available at the following link after launching the application:

   ```
   localhost:8080/swagger-documentation
   ```

## ● Adding a New Flight

### **Endpoint:** `POST` `/flights`

<details>
<summary>Details - click to open</summary>

**Description:**
Adds a new Flight to the database.

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

**Data Types:**

- int number (greater than 0)
- LocalDateTime departure (cannot be past)
- String route (must not be empty)
- int availableSeats (greater or equal 0)

**Response:**

- Status Code: **201 Created**.
- Body: Object representing the added Flight.
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

- Status Code: **400 Bad Request**.
- Incorrect value of any field. Example Response Body of incorrect values for all fields:

```json
{
  "departure": "Must be a future date.",
  "number": "Flight number must be greater than 0.",
  "route": "Route cannot be an empty field.",
  "availableSeats": "Available seats must be not be less than 0."
}
```

-Status Code: **400 Bad Request**.
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
Returns all Flights from database.

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

## ● Search Flights by route, departure and available seats

### **Endpoint:** `GET` `/flights/search`

<details>
<summary>Details - click to open</summary>

**Description:**
Returns all Flights from the database that meet the requirements.

**Possible URL parameters:**

- route (default = "")
- departure (default = LocalDateTime.now())
- availableSeats (default > 0)

**Request Body:**
Empty.

**Response:**

- Status Code: **200 OK**.
- Sample Response Body to `/flights/search?availableSeats=120`:

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

- Status Code: **400 Bad Request**.
- Sample Response Body to `/flights/search?availableSeats=abc`:

```json
{
  "error": "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"abc\""
}
```

</details>

## ● Get Flight by id

### **Endpoint:** `GET` `/flights/{id}`

<details>
<summary>Details - click to open</summary>

**Description:**
Returns Flight by id from the database.

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


- Status Code: **404 Not Found**
- Response Body when flight does not exist:

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
Updates existing Flight.

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
- Body: Object representing the updated Flight.
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

- Status Code: **400 Bad Request**
- Incorrect value of any field. Example Response Body of incorrect values for all fields:

```json
{
  "departure": "Must be a future date.",
  "number": "Flight number must be greater than 0.",
  "route": "Route cannot be an empty field.",
  "availableSeats": "Available seats must be not be less than 0."
}
```

- Status Code: **400 Bad Request**
- Incorrect type of value for flight number:

```json
{
  "error": "JSON parse error: Cannot deserialize value of type `int` from String 'd': not a valid `int` value"
}
```

- Status Code: **404 Not Found**
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
Adds a passenger to the selected Flight.

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
  "departure": "2024-06-01T12:00:00",
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

- Status Code: **404 Not Found**
- Flight not found Response Body:

```json
{
  "error": "Flight with id = 39 not found"
}
```

- Status Code: **404 Not Found**
- Passenger not found Response Body:

```json
{
  "error": "Passenger with id = 124 not found"
}
```

- Status Code: **400 Bad Request**
- Passenger is already added Response Body:

```json
{
  "error": "Passenger with id 2 is already added to flight number LO13."
}
```

- Status Code: **400 Bad Request**
- No available seats Response Body:

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
Removes a passenger from the selected Flight.

**Request Body:**
Empty.

**Response:**

- Status Code: **200 OK**
- Body: Flight with passengers who left.
- Sample Response Body:

```json
{
  "id": 2,
  "number": 13,
  "route": "Warsaw - Oslo",
  "departure": "2024-06-01T12:00:00",
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

- Status Code: **404 Not Found**
- Flight not found Response Body:

```json
{
  "error": "Flight with id = 39 not found"
}
```

- Status Code: **404 Not Found**
- Passenger not found Response Body:

```json
{
  "error": "Passenger with id = 124 not found"
}
```

- Status Code: **404 Not Found**
- Passenger not found on selected flight Response Body:

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
Removes a Flight from the database.

**Request Body:**
Empty.

**Response:**

- Status Code: **204 No Content**
- Body: Empty.


- Status Code: **404 Not Found**
- Flight not found Response Body:

```json
{
  "error": "Flight with id = 39 not found"
}
```

</details>

__________________________________________

## ● Adding a New Passenger

### **Endpoint:** `POST` `/passengers`

<details>
<summary>Details - click to open</summary>

**Description:**
Adds a new Passenger to the database.

**Request Body:**

- Format: JSON
- Sample Request Body:

```json
{
   "name": "Leia",
   "surname": "Organa",
   "phone": "789 456 123"
}
```

**Data types:**

- String name (must not be empty)
- String surname (must not be empty)
- String phone (must not be empty)

**Response:**

- Status Code: **201 Created**
- Body: Object representing the added Passenger.
- Sample Response Body:

```json
{
   "id": 2,
   "name": "Leia",
   "surname": "Organa",
   "phone": "789 456 123"
}
```

- Status Code: **400 Bad Request**
- Incorrect value of any field. Example Response Body of incorrect values for all fields:

```json
{
   "phone": "Phone cannot be an empty field.",
   "surname": "Surname cannot be an empty field.",
   "name": "Name cannot be an empty field."
}
```

</details>

## ● Get all Passengers

### **Endpoint:** `GET` `/passengers`

<details>
<summary>Details - click to open</summary>

**Description:**
Returns all Passengers from database.

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
```

</details>

## ● Get Passenger by id

### **Endpoint:** `GET` `/passengers/{id}`

<details>
<summary>Details - click to open</summary>

**Description:**
Returns Passenger by id from database.

**Request Body:**
Empty.

**Response:**

- Status Code: **200 OK**
- Sample Response Body:

```json
{
   "id": 2,
   "name": "Leia",
   "surname": "Organa",
   "phone": "789 456 123"
}
```

- Status Code: **404 Not Found**
- Response Body when Passenger does not exist:

```json
{
   "error": "Passenger with id = 13 not found"
}
```

</details>

## ● Updates existing Passenger

### **Endpoint:** `PUT` `/passengers/{id}`

<details>
<summary>Details - click to open</summary>

**Description:**
Updates existing Passenger.

**Request Body:**

- Format: JSON
- Sample Request Body:

```json
{
   "name": "Luke",
   "surname": "Skywalker",
   "phone": "111 222 333"
}
```

**Data types:**

- String name (must not be empty)
- String surname (must not be empty)
- String phone (must not be empty)

**Response:**

- Status Code: **200 OK**
- Body: Object representing the updated Passenger.
- Sample Response Body:

```json
{
   "id": 1,
   "name": "Luke",
   "surname": "Skywalker",
   "phone": "111 222 333"
}
```

- Status Code: **400 Bad Request**
- Incorrect value of any field. Example Response Body of incorrect values for all fields:

```json
{
   "phone": "Phone cannot be an empty field.",
   "surname": "Surname cannot be an empty field.",
   "name": "Name cannot be an empty field."
}
```

- Status Code: **404 Not Found**
- Passenger not found Response Body:

```json
{
   "error": "Passenger with id = 13 not found"
}
```

</details>

## ● Removes Passenger from database

### **Endpoint:** `DELETE` `passengers/{id}`

<details>
<summary>Details - click to open</summary>

**Description:**
Removes a Passenger from database.

**Request Body:**
Empty.

**Response:**

- Status Code: **204 No Content**
- Body: Empty.
- Sample Response Body:


- Status Code: **404 Not Found**
- Flight not found Response Body:

```json
{
  "error": "Flight with id = 39 not found"
}
```

</details>

# Contact

- Email: cezary.wozakowski@gmail.com