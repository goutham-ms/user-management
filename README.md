This project provides a RESTful API for managing users within an organization.
It supports creating, updating, deleting, retrieving, and exporting user data, with validation and error handling.

Overview:

API Version: v0

Specification: OpenAPI 3.0.4

Base URL: http://localhost:8080

This API is designed to handle user-related operations such as creation, retrieval, filtering, update, deletion, and exporting user details to an Excel file.

Endpoints
User Endpoints
1. Create a New User

URL: POST /user/create-user

Description: Creates a new user in the system.

Request Body:

{
"userId": "U12345",
"userName": "John Doe",
"age": 30,
"department": "ENGINEERING",
"dateOfBirth": "1995-06-15",
"registeredDate": "2024-05-01",
"updatedDate": "2024-06-01"
}


Responses:

200 OK – User created successfully

400 Bad Request – Invalid input

409 Conflict – Duplicate user

500 Internal Server Error – Server issue

2. Update a User

URL: PUT /user/update-user/{id}

Description: Updates an existing user's information.

Path Parameter:

id (string, required) – The ID of the user to update

Request Body: Same as user creation body

Responses:

200 OK – User updated successfully

404 Not Found – User not found

400 / 409 / 500 – Error responses

3. Get All Users

URL: GET /user/get-users

Description: Retrieves a list of all registered users.

Response Example:

[
{
"userId": "U12345",
"userName": "Jane Doe",
"age": 28,
"department": "MANAGEMENT",
"dateOfBirth": "1997-01-10",
"registeredDate": "2024-03-12",
"updatedDate": "2024-04-20"
}
]


Responses:

200 OK – Returns a list of users

400 / 404 / 409 / 500 – Error responses

4. Find Users (Filtered Search)

URL: GET /user/find-user

Description: Retrieves users based on filters such as age, department, or date range.

Query Parameters:

Name	Type	Required	Description
searchParam	string	No	Search by name or ID
ageParam	integer	No	Filter by age
departmentParam	string	No	Filter by department
fromParam	string	No	Filter by start date
toParam	string	No	Filter by end date

Responses:

200 OK – Returns filtered results

400 / 404 / 409 / 500 – Error responses

5. Delete a User

URL: DELETE /user/delete-user/{id}

Description: Deletes a user by their ID.

Path Parameter:

id (string, required) – ID of the user to delete

Responses:

200 OK – User deleted successfully

404 Not Found – User not found

400 / 409 / 500 – Error responses

6. Export Users (Excel)

URL: GET /user/export-excel

Description: Exports user details as an Excel file.

Query Parameters: Same as /user/find-user

Responses:

200 OK – Excel file generated

400 / 404 / 409 / 500 – Error responses


Validation Rules

User ID and Date of Birth are mandatory.

Department must match one of:

ENGINEERING

MANAGEMENT

SALES

Age must be between 10 and 150.

Running the API

Clone the repository:

git clone https://github.com/goutham-ms/user-management.git


Navigate to the project directory:

cd user-management


Start the backend service:

mvn spring-boot:run


Access Swagger UI:

http://localhost:8080/swagger-ui.html

 Example cURL Commands

Create User

curl -X POST "http://localhost:8080/user/create-user" \
-H "Content-Type: application/json" \
-d '{"userId":"U123","userName":"John","age":25,"department":"ENGINEERING","dateOfBirth":"1999-03-01"}'


Get All Users

curl -X GET "http://localhost:8080/user/get-users"


Delete User

curl -X DELETE "http://localhost:8080/user/delete-user/U123"