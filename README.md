# Coffee Shop Backend API

A complete Spring Boot backend application for a Coffee Shop management system built with Java 21, Spring Boot 3.5.0, and MySQL.

## 🚀 Live Demo

This project is part of UAS (Final Semester Exam) for Object-Oriented Programming course.

**Repository**: [Project-UAS-PBO](https://github.com/FelixStillLearning/Project-UAS-PBO)

## Features

- **User Authentication & Authorization**
  - Role-based access control (Admin/Customer)
  - Secure password encryption
  - JWT-ready security configuration

- **Product Management**
  - Categories, Products, Customizations
  - CRUD operations with validation
  - Search and filtering capabilities

- **Order Management**
  - Complete order processing
  - Order status tracking
  - Detailed order items with customizations
  - Automatic price calculation

- **Payment Methods**
  - Multiple payment method support
  - Admin-managed payment options

## Technology Stack

- **Java 21** - Programming language
- **Spring Boot 3.5.0** - Application framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data persistence
- **MySQL** - Database
- **Lombok** - Code generation
- **Gradle** - Build tool
- **Jakarta Validation** - Input validation

## Team Setup & Database Configuration

### 1. Clone the Repository
```bash
git clone https://github.com/FelixStillLearning/Project-UAS-PBO.git
cd Project-UAS-PBO
```

### 2. Environment Setup
Copy the environment template and configure your local settings:
```bash
cp .env.example .env
```

Edit `.env` file with your local database configuration:
```properties
DB_USERNAME=root
DB_PASSWORD=your_password
```

### 3. Database Setup
Create the database in your local MySQL:
```sql
CREATE DATABASE coffeeshop_db;
```

**Note**: The application uses environment variables for database configuration. Default values are set for Laragon users (empty password).

## Running the Application

1. **Prerequisites**
   - Java 21 or higher
   - MySQL 8.0+
   - Gradle (included via wrapper)

2. **Database Setup**
   ```sql
   CREATE DATABASE coffee_shop;
   ```

3. **Build and Run**
   ```bash
   # Build the project
   gradlew build -x test
   
   # Run the application
   gradlew bootRun
   ```

4. **Access the Application**
   - Application runs on: http://localhost:8080
   - Default admin credentials: `admin` / `admin123`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new customer
- `POST /api/auth/login` - User login
- `GET /api/auth/me` - Get current user info

### Categories (Admin only)
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search?name={name}` - Search products by name
- `GET /api/products/category/{categoryId}` - Get products by category
- `GET /api/products/price-range?min={min}&max={max}` - Filter by price range
- `POST /api/products` - Create product (Admin only)
- `PUT /api/products/{id}` - Update product (Admin only)
- `DELETE /api/products/{id}` - Delete product (Admin only)

### Customizations (Admin only)
- `GET /api/customizations` - Get all customizations
- `GET /api/customizations/{id}` - Get customization by ID
- `GET /api/customizations/type/{type}` - Get customizations by type
- `GET /api/customizations/free` - Get free customizations
- `GET /api/customizations/paid` - Get paid customizations
- `GET /api/customizations/search?name={name}` - Search customizations
- `POST /api/customizations` - Create customization
- `PUT /api/customizations/{id}` - Update customization
- `DELETE /api/customizations/{id}` - Delete customization

### Payment Methods (Admin only)
- `GET /api/payment-methods` - Get all payment methods
- `GET /api/payment-methods/{id}` - Get payment method by ID
- `POST /api/payment-methods` - Create payment method
- `PUT /api/payment-methods/{id}` - Update payment method
- `DELETE /api/payment-methods/{id}` - Delete payment method

### Orders
- `GET /api/orders` - Get orders (Admin: all orders, Customer: own orders)
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/customer/{customerId}` - Get orders by customer (Admin only)
- `GET /api/orders/status/{status}` - Get orders by status (Admin only)
- `POST /api/orders` - Create new order (Customer only)
- `PUT /api/orders/{id}/status` - Update order status (Admin only)
- `DELETE /api/orders/{id}` - Cancel order

## Request/Response Examples

### Register Customer
```json
POST /api/auth/register
{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "phoneNumber": "081234567890",
    "address": "123 Main St"
}
```

### Create Order
```json
POST /api/orders
{
    "paymentMethodId": 1,
    "customerNotes": "Extra hot, please",
    "orderDetails": [
        {
            "productId": 1,
            "quantity": 2,
            "customizations": [
                {
                    "customizationId": 1
                },
                {
                    "customizationId": 5
                }
            ]
        }
    ]
}
```

## Initial Data

The application automatically creates initial data on first run:

- **Admin User**: username `admin`, password `admin123`
- **Categories**: Coffee, Tea, Pastry, Snacks, Cold Drinks
- **Payment Methods**: Cash, Credit Card, Debit Card, Digital Wallet, Bank Transfer
- **Products**: Various coffee, tea, pastries, and other items
- **Customizations**: Size options, sugar levels, temperature, add-ons

## Error Handling

The API uses standard HTTP status codes and returns error responses in the following format:

```json
{
    "timestamp": "2025-05-25T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "path": "/api/products"
}
```

## Security

- Passwords are encrypted using BCrypt
- Role-based access control implemented
- Input validation on all endpoints
- SQL injection protection via JPA

## Development

### Project Structure
```
src/main/java/com/proyek/coffeeshop/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/             # Data Transfer Objects
├── exception/       # Exception handling
├── model/           # JPA entities and enums
├── repository/      # Data repositories
├── security/        # Security configuration
└── service/         # Business logic
```

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## Team Collaboration

### Getting Started for Team Members

1. **Clone the Repository**
   ```bash
   git clone https://github.com/FelixStillLearning/Project-UAS-PBO.git
   cd Project-UAS-PBO
   ```

2. **Environment Setup**
   - Copy `.env.example` to `.env`
   - Configure your local database settings
   - Default configuration works with Laragon (MySQL with empty password)

3. **Database Setup**
   ```sql
   CREATE DATABASE coffeeshop_db;
   ```

4. **Run the Application**
   ```bash
   gradlew bootRun
   ```

### Development Workflow

1. **Create Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make Changes and Test**
   - Write your code
   - Test with Postman or Thunder Client
   - Ensure all tests pass

3. **Commit and Push**
   ```bash
   git add .
   git commit -m "Add: your feature description"
   git push origin feature/your-feature-name
   ```

4. **Create Pull Request**
   - Go to GitHub repository
   - Create Pull Request from your feature branch
   - Request code review from team members

## License

This project is licensed under the MIT License.
