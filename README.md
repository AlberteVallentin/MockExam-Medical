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

