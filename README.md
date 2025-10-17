# **Streamoid Product Service**

A backend service built with Java and Spring Boot to manage e-commerce product catalogs. This application allows users to upload product data via a CSV file, validates the information, stores it in a database, and provides RESTful APIs for easy retrieval and filtering.

## **✨ Features**

* **📄 CSV Bulk Upload**: Easily upload an entire product catalog using a single CSV file.  
* **🔒 Robust Data Validation**: Ensures data integrity by validating each product against a set of business rules (e.g., price constraints, required fields).  
* **💾 Persistent Storage**: Valid product data is saved to a persistent H2 file-based database using Spring Data JPA.  
* **📖 Paginated Listing**: Fetch products in manageable chunks using a paginated API endpoint.  
* **🔍 Advanced Search & Filtering**: Dynamically search for products by brand, color, or a specified price range.  
* **❗ Detailed Error Reporting**: Receive a clear summary of which rows, if any, failed validation during an upload, complete with error messages.

## **🛠️ Tech Stack**

| Category | Technology |
| :---- | :---- |
| **Language** | Java 17 |
| **Framework** | Spring Boot |
| **Data Persistence** | Spring Data JPA, Hibernate |
| **Database** | H2 Database (File-based) |
| **API** | Spring Web (RESTful APIs) |
| **CSV Parsing** | OpenCSV |
| **Build Tool** | Apache Maven |
| **Utilities** | Lombok |

## **🚀 Getting Started**

Follow these instructions to get the project up and running on your local machine.

### **Prerequisites**

* Git  
* Java Development Kit (JDK) 17 or later  
* Apache Maven (Optional, as the project includes the Maven wrapper)

### **Installation & Running**

1. **Clone the repository:**  
   git clone \[https://github.com/your-username/Streamoid-Product-Service.git\](https://github.com/your-username/Streamoid-Product-Service.git)  
   cd Streamoid-Product-Service

2. Run the application using the Maven Wrapper:  
   This command will compile the code and start the web server.  
   * On macOS/Linux:  
     ./mvnw spring-boot:run

   * On Windows:  
     mvnw.cmd spring-boot:run

3. The application will start on port 8000\. You can access it at http://localhost:8000.

## **📖 API Documentation**

The following REST endpoints are available.

### **1\. Upload a Product CSV**

Uploads a CSV file, validates each row, and stores valid products.

* **Endpoint**: POST /upload  
* **Request Type**: multipart/form-data  
* **Form Field**: file (The products.csv file)

#### **Example Request:**

curl \-X POST \-F "file=@/path/to/your/products.csv" http://localhost:8000/upload

#### **Success Response (200 OK):**

Returns a summary of the upload process.

{  
  "stored": 20,  
  "failed": \[\]  
}

### **2\. List All Products**

Retrieves a paginated list of all products.

* **Endpoint**: GET /products  
* **Query Parameters**:  
  * page (optional, default: 0): The page number to retrieve.  
  * limit (optional, default: 10): The number of products per page.

#### **Example Request:**

http://localhost:8000/products?page=0\&limit=5

#### **Success Response (200 OK):**

{  
    "content": \[  
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
    \],  
    "pageable": { "...": "..." },  
    "totalPages": 2,  
    "totalElements": 20,  
    "...": "..."  
}

### **3\. Search Products**

Searches for products based on filter criteria. All parameters are optional.

* **Endpoint**: GET /products/search  
* **Query Parameters**:  
  * brand (e.g., DenimWorks)  
  * color (e.g., Blue)  
  * minPrice (e.g., 1000\)  
  * maxPrice (e.g., 2500\)

#### **Example Request:**

http://localhost:8000/products/search?brand=DenimWorks\&maxPrice=2000

#### **Success Response (200 OK):**

\[  
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
\]

## **🗃️ Database Access**

The application uses a persistent file-based **H2 Database**. You can access the H2 console in your browser to view the database content directly.

* **Console URL**: http://localhost:8000/h2-console  
* **JDBC URL**: jdbc:h2:file:./data/productsdb  
* **Username**: sa  
* **Password**: password

## **📁 Project Structure**

Here is an overview of the project's directory structure:

Streamoid-Product-Service/  
│  
├── 📁 data/  
│   └── 📄 productsdb.mv.db      \# H2 Database file for persistent storage  
│  
├── 📁 .mvn/  
│   └── ...                     \# Maven Wrapper configuration files  
│  
├── 📁 src/  
│   ├── 📁 main/  
│   │   ├── 📁 java/  
│   │   │   └── 📁 com/StreamoidProductService/streamoid/product/service/  
│   │   │       ├── 📁 controller/       \# Contains REST API controllers (e.g., ProductController)  
│   │   │       ├── 📁 dto/              \# Data Transfer Objects (e.g., UploadResponse)  
│   │   │       ├── 📁 model/            \# JPA entity classes (e.g., Product)  
│   │   │       ├── 📁 repository/       \# Spring Data JPA repositories  
│   │   │       ├── 📁 service/          \# Contains the core business logic (e.g., ProductService)  
│   │   │       └── 📄 StreamoidProductServiceApplication.java \# Main Spring Boot application class  
│   │   │  
│   │   └── 📁 resources/  
│   │       └── 📄 application.properties \# Configuration for the application, database, and server  
│   │  
│   └── 📁 test/  
│       └── ...                     \# Unit and integration tests  
│  
├── 📄 .gitignore                  \# Specifies files to be ignored by Git  
├── 📄 mvnw                        \# Maven wrapper executable for Linux/macOS  
├── 📄 mvnw.cmd                    \# Maven wrapper batch script for Windows  
└── 📄 pom.xml                     \# Maven project configuration (dependencies, build settings)  
