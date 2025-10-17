Streamoid Product Service

This project is a backend service built with Java and Spring Boot that allows users to upload a product catalog in CSV format. The service validates each product, stores the valid entries in a database, and provides REST APIs to list and search for the stored products.

✨ Features

CSV Upload: A POST endpoint to upload a product catalog in CSV format.

Robust Data Validation: Each row from the CSV is validated against a set of rules:

Required fields (sku, name, brand, mrp, price) must be present.

price must be less than or equal to mrp.

quantity must be a non-negative number.

Handles rows with incorrect column counts or invalid number formats.

Database Storage: Valid products are saved to a persistent H2 file-based database using Spring Data JPA.

Paginated Product Listing: A GET endpoint to fetch all products with pagination support (page and limit).

Advanced Search: A GET endpoint to search and filter products by brand, color, and a price range (minPrice, maxPrice).

Detailed Error Reporting: The upload response includes a list of rows that failed validation, along with the row number and the specific error message.

🛠️ Tech Stack

Framework: Spring Boot

Language: Java 17

Data Persistence: Spring Data JPA, Hibernate

Database: H2 Database (File-based)

API: Spring Web (RESTful APIs)

CSV Parsing: OpenCSV

Build Tool: Apache Maven

Utilities: Lombok

🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites

Git

Java Development Kit (JDK) 17 or later

Apache Maven (Optional, as the project includes a Maven wrapper)

Installation & Running

Clone the repository:

git clone [https://github.com/your-username/Streamoid-Product-Service.git](https://github.com/your-username/Streamoid-Product-Service.git)
cd Streamoid-Product-Service


Run the application using the Maven Wrapper:

On macOS/Linux:

./mvnw spring-boot:run


On Windows:

mvnw.cmd spring-boot:run


The application will start on port 8000. You can access it at http://localhost:8000.

📖 API Documentation

The following are the available API endpoints.

1. Upload a Product CSV

Uploads a CSV file, validates each row, and stores the valid products in the database.

Endpoint: POST /upload

Request: multipart/form-data

Form Field: file (The CSV file to be uploaded)

Example cURL Request:

curl -X POST -F "file=@products.csv" http://localhost:8000/upload


(Ensure you have a products.csv file in the directory where you run the command)

Success Response (200 OK):

Returns a JSON object detailing the number of products stored and a list of any rows that failed validation.

{
  "stored": 4,
  "failed": []
}


2. List All Products

Retrieves a paginated list of all products stored in the database.

Endpoint: GET /products

Query Parameters:

page (optional, default: 0): The page number to retrieve.

limit (optional, default: 10): The number of products per page.

Example Request:

http://localhost:8000/products?page=0&limit=5


Success Response (200 OK):

{
    "content": [
        {
            "sku": "TSHIRT-RED-001",
            "name": "Classic Cotton T-Shirt",
            "brand": "Stream Threads",
            "color": "Red",
            "size": "M",
            "mrp": 799.0,
            "price": 499.0,
            "quantity": 20
        }
    ],
    "pageable": { "...": "..." },
    "totalPages": 1,
    "totalElements": 4,
    "...": "..."
}


3. Search Products

Searches for products based on various filter criteria. All parameters are optional.

Endpoint: GET /products/search

Query Parameters:

brand: Filter by brand name (e.g., DenimWorks).

color: Filter by color (e.g., Blue).

minPrice: Filter for products with a price greater than or equal to this value.

maxPrice: Filter for products with a price less than or equal to this value.

Example Requests:

By brand: http://localhost:8000/products/search?brand=BloomWear

By price range: http://localhost:8000/products/search?minPrice=1000&maxPrice=2500

Success Response (200 OK):

[
    {
        "sku": "JEANS-BLU-032",
        "name": "Slim Fit Jeans",
        "brand": "DenimWorks",
        "color": "Blue",
        "size": "32",
        "mrp": 1999.0,
        "price": 1599.0,
        "quantity": 15
    }
]


🗃️ Database

The application uses a persistent file-based H2 Database.

The database file is located at /data/productsdb.mv.db in the project root.

You can access the H2 console in your browser to view the database content:

URL: http://localhost:8000/h2-console

JDBC URL: jdbc:h2:file:./data/productsdb

Username: sa

Password: password

📁 Project Structure

Streamoid-Product-Service
├── data/
│   └── productsdb.mv.db      # H2 Database file
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/StreamoidProductService/streamoid/product/service/
│   │   │       ├── controller/       # REST API controllers
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       ├── model/            # JPA entities
│   │   │       ├── repository/       # Spring Data repositories
│   │   │       ├── service/          # Business logic
│   │   │       └── StreamoidProductServiceApplication.java
│   │   └── resources/
│   │       └── application.properties # Spring Boot configuration
│   └── test/
├── mvnw                        # Maven wrapper for Linux/macOS
├── mvnw.cmd                    # Maven wrapper for Windows
└── pom.xml                     # Maven project configuration
