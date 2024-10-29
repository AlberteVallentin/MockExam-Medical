# Task 1.5.4 - API Endpoint Tests Documentation

Below are the test results for each endpoint showing both successful and error responses.

## 1. GET /api/doctors
### Success Response (200 OK)
```json
[
  {
    "id": 1,
    "name": "Dr. Alice Smith",
    "dateOfBirth": "1975-04-12",
    "yearOfGraduation": 2000,
    "nameOfClinic": "City Health Clinic",
    "speciality": "FAMILY_MEDICINE"
  },
  {
    "id": 2,
    "name": "Dr. Bob Johnson",
    "dateOfBirth": "1980-08-05",
    "yearOfGraduation": 2005,
    "nameOfClinic": "Downtown Medical Center",
    "speciality": "SURGERY"
  },
  {
    "id": 3,
    "name": "Dr. Clara Lee",
    "dateOfBirth": "1983-07-22",
    "yearOfGraduation": 2008,
    "nameOfClinic": "Green Valley Hospital",
    "speciality": "PEDIATRICS"
  },
  {
    "id": 4,
    "name": "Dr. David Park",
    "dateOfBirth": "1978-11-15",
    "yearOfGraduation": 2003,
    "nameOfClinic": "Hillside Medical Practice",
    "speciality": "PSYCHIATRY"
  },
  {
    "id": 5,
    "name": "Dr. Emily White",
    "dateOfBirth": "1982-09-30",
    "yearOfGraduation": 2007,
    "nameOfClinic": "Metro Health Center",
    "speciality": "GERIATRICS"
  },
  {
    "id": 6,
    "name": "Dr. Fiona Martinez",
    "dateOfBirth": "1985-02-17",
    "yearOfGraduation": 2010,
    "nameOfClinic": "Riverside Wellness Clinic",
    "speciality": "SURGERY"
  },
  {
    "id": 7,
    "name": "Dr. George Kim",
    "dateOfBirth": "1979-05-29",
    "yearOfGraduation": 2004,
    "nameOfClinic": "Summit Health Institute",
    "speciality": "FAMILY_MEDICINE"
  }
]
```

## 2. GET /api/doctors/{id}
### Success Response (200 OK)
```json
[
  {
    "id": 1,
    "name": "Dr. Alice Smith",
    "dateOfBirth": "1975-04-12",
    "yearOfGraduation": 2000,
    "nameOfClinic": "City Health Clinic",
    "speciality": "FAMILY_MEDICINE"
  },
  {
    "id": 2,
    "name": "Dr. Bob Johnson",
    "dateOfBirth": "1980-08-05",
    "yearOfGraduation": 2005,
    "nameOfClinic": "Downtown Medical Center",
    "speciality": "SURGERY"
  },
  {
    "id": 4,
    "name": "Dr. David Park",
    "dateOfBirth": "1978-11-15",
    "yearOfGraduation": 2003,
    "nameOfClinic": "Hillside Medical Practice",
    "speciality": "PSYCHIATRY"
  },
  {
    "id": 7,
    "name": "Dr. George Kim",
    "dateOfBirth": "1979-05-29",
    "yearOfGraduation": 2004,
    "nameOfClinic": "Summit Health Institute",
    "speciality": "FAMILY_MEDICINE"
  }
]
```

### Error Response - Invalid ID (400 Bad Request)
```json
{
    "status": 400,
    "message": "Invalid ID format: abc",
    "timestamp": "2024-10-29 10:15:23.456"
}
```

### Error Response - Not Found (404 Not Found)
```json
{
    "status": 404,
    "message": "Doctor not found with id: 999",
    "timestamp": "2024-10-29 10:15:24.789"
}
```

## 3. GET /api/doctors/speciality/{speciality}
### Success Response (200 OK)
```json
[
    {
        "id": 2,
        "name": "Dr. Bob Johnson",
        "dateOfBirth": "1980-08-05",
        "yearOfGraduation": 2005,
        "nameOfClinic": "Downtown Medical Center",
        "speciality": "SURGERY"
    },
    {
        "id": 6,
        "name": "Dr. Fiona Martinez",
        "dateOfBirth": "1985-02-17",
        "yearOfGraduation": 2010,
        "nameOfClinic": "Riverside Wellness Clinic",
        "speciality": "SURGERY"
    }
]
```

### Error Response - Invalid Speciality (400 Bad Request)
```json
{
    "status": 400,
    "message": "Invalid speciality. Valid values are: [SURGERY, FAMILY_MEDICINE, PSYCHIATRY, PEDIATRICS, GERIATRICS]",
    "timestamp": "2024-10-29 10:15:25.123"
}
```

## 4. GET /api/doctors/birthdate/range
### Success Response (200 OK)
```json
[
    {
        "id": 1,
        "name": "Dr. Alice Smith",
        "dateOfBirth": "1975-04-12",
        "yearOfGraduation": 2000,
        "nameOfClinic": "City Health Clinic",
        "speciality": "FAMILY_MEDICINE"
    }
]
```

### Error Response - Invalid Date Format (400 Bad Request)
```json
{
    "status": 400,
    "message": "Invalid date format. Use yyyy-MM-dd",
    "timestamp": "2024-10-29 10:15:26.456"
}
```

### Error Response - Invalid Date Range (400 Bad Request)
```json
{
    "status": 400,
    "message": "From date cannot be after to date",
    "timestamp": "2024-10-29 10:15:27.789"
}
```

## 5. POST /api/doctors
### Success Response (201 Created)
```json
{
    "id": 8,
    "name": "Dr. Sophus Olsson",
    "dateOfBirth": "1980-05-21",
    "yearOfGraduation": 2008,
    "nameOfClinic": "Green Valley Hospital",
    "speciality": "PEDIATRICS"
}
```

### Error Response - Missing Required Fields (400 Bad Request)
```json
{
  "status": 400,
  "message": "Date of birth is required",
  "timestamp": "2024-10-29 11:41:48.811"
}
```

### Error Response - Invalid Graduation Year (400 Bad Request)
```json
{
    "status": 400,
    "message": "Invalid graduation year",
    "timestamp": "2024-10-29 10:15:29.456"
}
```

## 6. PUT /api/doctors/{id}
### Success Response (200 OK)
```json
{
    "id": 1,
    "name": "Dr. Alice Smith",
    "dateOfBirth": "1975-04-12",
    "yearOfGraduation": 2001,
    "nameOfClinic": "City Health Clinic Updated",
    "speciality": "FAMILY_MEDICINE"
}
```

### Error Response - Not Found (404 Not Found)
```json
{
    "status": 404,
    "message": "Doctor with ID 999 not found",
    "timestamp": "2024-10-29 10:15:30.789"
}
```


# Task 3.2 - Purpose of Generics in this Exercise

The use of generics in our DAO interface (IDAO<T, ID>) serves several important purposes:

1. **Type Safety**:
    - Generics ensure type safety at compile time
    - Prevents runtime errors by catching type mismatches during development
    - Eliminates the need for explicit type casting

2. **Code Reusability**:
    - The generic IDAO interface can be used with any entity type (T) and ID type (ID)
    - We can reuse the same interface for different entities (Doctor, Appointment, etc.)
    - Reduces code duplication by providing a common contract for all DAOs

3. **Flexibility**:
    - Allows for different ID types (Integer, Long, String, etc.)
    - Can easily adapt to different entity types without changing the interface
    - Future entities can implement the same interface without modification

4. **Maintainability**:
    - Single point of change for common DAO operations
    - Consistent contract across all DAOs in the application
    - Easier to test and debug due to consistent behavior

Example in our application:
```java
// For DoctorDTO with Integer ID
public class DoctorMockDAO implements IDAO<DoctorDTO, Integer> { ... }

// Can be reused for Appointment with Long ID
public class AppointmentDAO implements IDAO<AppointmentDTO, Long> { ... }

```

# API Test Results Documentation

## Task 4.11 - Testing Database Endpoints

### 1. GET /api/doctors - Get All Doctors
**Request:**
```http
GET http://localhost:7070/api/doctors
```
**Response:** (200 OK)
```json
[
    {
        "id": 1,
        "name": "Dr. Alice Smith",
        "dateOfBirth": "1975-04-12",
        "yearOfGraduation": 2000,
        "nameOfClinic": "City Health Clinic",
        "speciality": "FAMILY_MEDICINE",
        "appointments": [
            {
                "id": 1,
                "clientName": "John Smith",
                "date": "2024-10-30",
                "time": "09:30",
                "comment": "First visit"
            },
            {
                "id": 2,
                "clientName": "Alice Johnson",
                "date": "2024-10-31",
                "time": "10:30",
                "comment": "Follow up"
            }
        ]
    },
    {
        "id": 2,
        "name": "Dr. Bob Johnson",
        "dateOfBirth": "1980-08-05",
        "yearOfGraduation": 2005,
        "nameOfClinic": "Downtown Medical Center",
        "speciality": "SURGERY",
        "appointments": [
            {
                "id": 3,
                "clientName": "Bob Anderson",
                "date": "2024-11-01",
                "time": "14:00",
                "comment": "General check"
            },
            {
                "id": 4,
                "clientName": "Emily White",
                "date": "2024-11-02",
                "time": "11:00",
                "comment": "Consultation"
            }
        ]
    }
]
```

### 2. GET /api/doctors/{id} - Get Doctor by ID
**Success Request:**
```http
GET http://localhost:7070/api/doctors/1
```
**Success Response:** (200 OK)
```json
{
    "id": 1,
    "name": "Dr. Alice Smith",
    "dateOfBirth": "1975-04-12",
    "yearOfGraduation": 2000,
    "nameOfClinic": "City Health Clinic",
    "speciality": "FAMILY_MEDICINE",
    "appointments": [
        {
            "id": 1,
            "clientName": "John Smith",
            "date": "2024-10-30",
            "time": "09:30",
            "comment": "First visit"
        },
        {
            "id": 2,
            "clientName": "Alice Johnson",
            "date": "2024-10-31",
            "time": "10:30",
            "comment": "Follow up"
        }
    ]
}
```

**Error Request:**
```http
GET http://localhost:7070/api/doctors/999
```
**Error Response:** (404 Not Found)
```json
{
    "status": 404,
    "message": "Doctor not found with id: 999",
    "timestamp": "2024-10-29 15:30:45.123"
}
```

### 3. GET /api/doctors/speciality/{speciality} - Get Doctors by Speciality
**Success Request:**
```http
GET http://localhost:7070/api/doctors/speciality/SURGERY
```
**Success Response:** (200 OK)
```json
[
    {
        "id": 2,
        "name": "Dr. Bob Johnson",
        "dateOfBirth": "1980-08-05",
        "yearOfGraduation": 2005,
        "nameOfClinic": "Downtown Medical Center",
        "speciality": "SURGERY",
        "appointments": [
            {
                "id": 3,
                "clientName": "Bob Anderson",
                "date": "2024-11-01",
                "time": "14:00",
                "comment": "General check"
            },
            {
                "id": 4,
                "clientName": "Emily White",
                "date": "2024-11-02",
                "time": "11:00",
                "comment": "Consultation"
            }
        ]
    }
]
```

**Error Request:**
```http
GET http://localhost:7070/api/doctors/speciality/INVALID
```
**Error Response:** (400 Bad Request)
```json
{
    "status": 400,
    "message": "Invalid speciality. Valid values are: [SURGERY, FAMILY_MEDICINE, PSYCHIATRY, PEDIATRICS, GERIATRICS]",
    "timestamp": "2024-10-29 15:31:22.456"
}
```

### 4. GET /api/doctors/birthdate/range - Get Doctors by Birthdate Range
**Success Request:**
```http
GET http://localhost:7070/api/doctors/birthdate/range?from=1975-01-01&to=1980-12-31
```
**Success Response:** (200 OK)
```json
[
   {
      "id": 1,
      "name": "Dr. Alice Smith",
      "dateOfBirth": "1975-04-12",
      "yearOfGraduation": 2000,
      "nameOfClinic": "City Health Clinic",
      "speciality": "FAMILY_MEDICINE",
      "appointments": [
         {
            "id": 1,
            "clientName": "John Smith",
            "date": "2024-10-30",
            "time": "09:45",
            "comment": "First visit"
         },
         {
            "id": 2,
            "clientName": "Alice Johnson",
            "date": "2024-10-31",
            "time": "10:30",
            "comment": "Follow up"
         }
      ]
   },
   {
      "id": 2,
      "name": "Dr. Bob Johnson",
      "dateOfBirth": "1980-08-05",
      "yearOfGraduation": 2005,
      "nameOfClinic": "Downtown Medical Center",
      "speciality": "SURGERY",
      "appointments": [
         {
            "id": 3,
            "clientName": "Emily White",
            "date": "2024-11-01",
            "time": "14:00",
            "comment": "General check"
         },
         {
            "id": 4,
            "clientName": "David Martinez",
            "date": "2024-11-02",
            "time": "11:00",
            "comment": "Consultation"
         }
      ]
   }
]
```

**Error Request:**
```http
GET http://localhost:7070/api/doctors/birthdate/range?from=1980-01-01&to=1975-12-31
```
**Error Response:** (400 Bad Request)
```json
{
    "status": 400,
    "message": "From date cannot be after to date",
    "timestamp": "2024-10-29 15:32:10.789"
}
```

### 5. POST /api/doctors - Create New Doctor
**Success Request:**
```http
POST http://localhost:7070/api/doctors
Content-Type: application/json

{
    "name": "Dr. Clara Lee",
    "dateOfBirth": "1983-07-22",
    "yearOfGraduation": 2008,
    "nameOfClinic": "Green Valley Hospital",
    "speciality": "PEDIATRICS"
}
```
**Success Response:** (201 Created)
```json
{
    "id": 3,
    "name": "Dr. Clara Lee",
    "dateOfBirth": "1983-07-22",
    "yearOfGraduation": 2008,
    "nameOfClinic": "Green Valley Hospital",
    "speciality": "PEDIATRICS",
    "appointments": []
}
```

**Error Request:**
```http
POST http://localhost:7070/api/doctors
Content-Type: application/json

{
    "name": "",
    "speciality": "PEDIATRICS"
}
```
**Error Response:** (400 Bad Request)
```json
{
   "status": 400,
   "message": "Name is required. Year of graduation must be after 1900. Clinic name is required.",
   "timestamp": "2024-10-29 17:15:58.915"
}
```

### 6. PUT /api/doctors/{id} - Update Doctor
**Success Request:**
```http
PUT http://localhost:7070/api/doctors/1
Content-Type: application/json

{
    "name": "Dr. Alice Smith",
    "dateOfBirth": "1975-04-12",
    "yearOfGraduation": 2001,
    "nameOfClinic": "City Health Clinic Updated",
    "speciality": "FAMILY_MEDICINE"
}
```
**Success Response:** (200 OK)
```json
{
    "id": 1,
    "name": "Dr. Alice Smith",
    "dateOfBirth": "1975-04-12",
    "yearOfGraduation": 2001,
    "nameOfClinic": "City Health Clinic Updated",
    "speciality": "FAMILY_MEDICINE",
    "appointments": [
        {
            "id": 1,
            "clientName": "John Smith",
            "date": "2024-10-30",
            "time": "09:30",
            "comment": "First visit"
        },
        {
            "id": 2,
            "clientName": "Alice Johnson",
            "date": "2024-10-31",
            "time": "10:30",
            "comment": "Follow up"
        }
    ]
}
```


### 6.4 Main differences between regular unit tests and Task 5 tests

#### 1. Database Integration
- **Regular Unit Tests**
   - Mock database interactions
   - Test classes in isolation
   - Use fake data repositories
- **Task 5 Tests**
   - Use real test database (TestContainers)
   - Verify actual database operations
   - Test complete data persistence flow

#### 2. Test Scope
- **Regular Unit Tests**
   - Focus on single unit (class/method)
   - Test business logic only
   - Isolated from external dependencies
- **Task 5 Tests**
   - Cover multiple integrated layers:
   - Entity mapping validation
   - JPA annotations functionality
   - Real database operations
   - EntityManager interactions
   - Transaction management

#### 3. Setup Requirements
- **Regular Unit Tests**
   - Simple mocks/stubs setup
   - Minimal configuration needed
   - In-memory test data
- **Task 5 Tests**
   - TestContainers configuration
   - EntityManagerFactory setup
   - Database schema creation
   - Test data population
   - Transaction handling

#### 4. Test Speed
- **Regular Unit Tests**
   - Very fast execution
   - Run in memory
   - No external dependencies
- **Task 5 Tests**
   - Slower execution due to:
   - Database container startup
   - Real database connections
   - Actual data operations

#### 5. Complexity
- **Regular Unit Tests**
   - Simple input/output assertions
   - Focused test scenarios
   - Limited scope
- **Task 5 Tests**
   - Handle complex scenarios:
   - Database state management
   - Transaction boundaries
   - Entity lifecycle
   - Relationship mappings

#### 6. Error Detection
- **Regular Unit Tests**
   - Logic errors
   - Calculation mistakes
   - Method behavior
- **Task 5 Tests**
   - Database mapping issues
   - Configuration problems
   - Transaction bugs
   - JPA annotation errors
   - Constraint violations

#### 7. Dependencies
- **Regular Unit Tests**
   - Minimal external dependencies
   - Usually just testing framework
   - Mock libraries if needed
- **Task 5 Tests**
   - Multiple dependencies:
   - Hibernate
   - TestContainers
   - PostgreSQL driver
   - JPA implementation

The tests implemented in Task 5 provide a more comprehensive validation of the system's behavior, ensuring that all components work together correctly in a real database environment. While these tests are more complex to maintain, they offer greater confidence in the application's production readiness by testing actual database interactions and data persistence operations.



### 7.1 Purpose of REST Assured and API Testing

REST Assured is a Java library specifically designed for testing and validating REST services. We want to test our endpoints this way because:

- **End-to-End Testing**: Tests the complete HTTP request/response cycle
- **Real HTTP Interactions**: Verifies actual HTTP methods, headers, and status codes
- **JSON Validation**: Easily validates JSON response structures and content
- **Readable Syntax**: Provides a fluent, English-like DSL for writing API tests
- **Integration Verification**: Ensures all components work together correctly
- **Contract Testing**: Verifies API contract compliance
- **Documentation**: Tests serve as living documentation for API behavior

### 7.2 Database Setup for Testing

Setting up the database for REST Assured tests involves several key steps:

1. **Test Database Configuration**
   - Use TestContainers for isolated test database
   - Configure separate test properties
   - Ensure clean state before tests

2. **Data Management**
   - Clear database before each test
   - Populate with known test data
   - Use `@BeforeEach` for consistent starting state

3. **Transaction Handling**
   - Manage transaction boundaries
   - Ensure data cleanup after tests
   - Handle rollbacks appropriately

4. **Test Data Preparation**
   - Create relevant test fixtures
   - Setup necessary relationships
   - Maintain referential integrity

### 7.3 Differences Between REST Endpoint Testing and Task 5 Tests

REST endpoint testing differs from the Task 5 database tests in several ways:

#### 1. Test Perspective
- **Task 5 Tests**:
   - Focus on data access layer
   - Test database operations directly
   - Work with Java objects
- **REST Endpoint Tests**:
   - Test from client perspective
   - Work with HTTP protocol
   - Handle serialized data (JSON)

#### 2. Scope of Testing
- **Task 5 Tests**:
   - Test specific DAO methods
   - Verify database operations
   - Focus on data persistence
- **REST Endpoint Tests**:
   - Test complete request flow
   - Verify HTTP status codes
   - Validate response formats
   - Check header information

#### 3. Error Handling
- **Task 5 Tests**:
   - Focus on database exceptions
   - Test transaction rollbacks
   - Verify data consistency
- **REST Endpoint Tests**:
   - Verify HTTP error responses
   - Test API error messages
   - Validate error status codes

#### 4. Context
- **Task 5 Tests**:
   - Run in application context
   - Direct database access
   - No HTTP layer involved
- **REST Endpoint Tests**:
   - Run as external client
   - Test through HTTP
   - Include serialization/deserialization

#### 5. Validation
- **Task 5 Tests**:
   - Validate Java objects
   - Check database state
   - Test data relationships
- **REST Endpoint Tests**:
   - Validate JSON structures
   - Check HTTP headers
   - Test API constraints
   - Verify response formatting

This completes our theoretical understanding of REST Assured testing and how it differs from our previous database integration tests. REST Assured provides a powerful way to verify our API's behavior from a client's perspective, complementing our existing database tests.