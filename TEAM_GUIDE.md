# Coffee Shop Backend - Team Development Guide

## Quick Start for Team Members

### 1. Setup Environment
```bash
# Clone repository
git clone https://github.com/FelixStillLearning/Project-UAS-PBO.git
cd Project-UAS-PBO

# Setup environment file
copy .env.example .env

# Edit .env with your database settings
# For Laragon users: DB_USERNAME=root, DB_PASSWORD=(empty)
```

### 2. Database Setup
```sql
CREATE DATABASE coffeeshop_db;
```

### 3. Run Application
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

## Development Workflow

### Branch Strategy
- `master` - Production ready code
- `develop` - Integration branch
- `feature/[feature-name]` - Feature development
- `bugfix/[bug-name]` - Bug fixes

### Code Standards
- Follow Spring Boot best practices
- Use Lombok annotations for entities
- Write meaningful commit messages
- Add validation annotations on DTOs
- Include error handling in services

### Testing Endpoints
Use Postman collection or test individually:

**Default Admin Credentials:**
- Username: `admin`
- Password: `admin`

**Base URL:** `http://localhost:8080/api`

### Common Issues & Solutions

1. **Database Connection Error**
   - Check if MySQL is running
   - Verify database name: `coffeeshop_db`
   - Check .env file configuration

2. **Authentication Issues**
   - Use Basic Auth with admin/admin
   - Check Authorization header in requests

3. **Build Errors**
   - Run `gradlew clean build`
   - Check Java version (requires Java 21+)

### Team Communication
- Create issues for bugs or feature requests
- Use Pull Requests for code review
- Tag team members for urgent issues
- Update this documentation when needed

## API Testing Examples

### Register New Customer
```json
POST /api/auth/register
{
    "username": "customer1",
    "email": "customer1@example.com",
    "password": "password123",
    "fullName": "Customer One",
    "phoneNumber": "081234567890",
    "address": "Customer Address"
}
```

### Create Category (Admin)
```json
POST /api/categories
Authorization: Basic YWRtaW46YWRtaW4=
{
    "name": "Hot Beverages",
    "description": "All hot drinks"
}
```

### Create Product (Admin)
```json
POST /api/products
Authorization: Basic YWRtaW46YWRtaW4=
{
    "name": "Espresso",
    "description": "Strong coffee shot",
    "price": 25000,
    "categoryId": 1,
    "available": true
}
```

## Database Schema

The application uses the following main entities:
- `User` - System users (Admin/Customer)
- `Customer` - Customer details
- `Category` - Product categories
- `Product` - Coffee shop products
- `PaymentMethod` - Payment options
- `Order` - Customer orders
- `OrderDetail` - Order line items
- `Customization` - Product customizations
- `OrderDetailCustomization` - Order customizations

Refer to the ERD diagram for detailed relationships.
