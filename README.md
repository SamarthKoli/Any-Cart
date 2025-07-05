# 🛒 AnyCart - E-Commerce Backend

**AnyCart** is a full-featured backend for an e-commerce platform built using Spring Boot. It supports user registration, authentication with JWT, product and category management, cart operations, order processing, price alerts, and image uploads via Cloudinary.

---

## 🚀 Features

- 🔐 JWT-based authentication and authorization
- 🧑‍💼 Role-based access (`USER`, `ADMIN`)
- 🛍️ Product CRUD with image upload support
- 🗂️ Category and subcategory hierarchy
- 🛒 Add to cart, view/update/remove items
- 📦 Place orders, view order history
- 📈 Price alerts with scheduled notifications
- 📧 Email notifications (Spring Mail)

---

## 🛠️ Tech Stack

| Layer        | Technology                      |
|--------------|----------------------------------|
| Language     | Java 21                          |
| Framework    | Spring Boot 3.5.0                |
| Security     | Spring Security, JWT             |
| ORM          | Spring Data JPA (Hibernate)      |
| Database     | MySQL                            |
| Image Upload | Cloudinary SDK                   |
| Build Tool   | Maven                            |
| Email        | Spring Boot Starter Mail         |
| Validation   | Jakarta Validation API           |
| Utils        | Lombok, MapStruct (optional)     |

---

## 📁 Project Structure

```
samarthkoli-any-cart/
├── controllers/        # REST Controllers
├── dto/                # Data Transfer Objects
├── dtomapper/          # Entity <-> DTO mapping
├── entities/           # JPA entities
├── repository/         # JPA Repositories
├── services/           # Business Logic
├── security/           # JWT and Filters
├── resources/          # application.yml (not shown)
├── pom.xml             # Maven Dependencies
└── AnycartApplication.java
```

---

## 📦 Key Endpoints

| Module        | Endpoint Prefix             | Access         |
|---------------|-----------------------------|----------------|
| Auth          | `/api/auth/*`               | Public         |
| Products      | `/api/v1/products/*`        | Public/Admin   |
| Categories    | `/api/v1/categories/*`      | Public/Admin   |
| Cart          | `/api/v1/cartItems/*`       | Authenticated  |
| Orders        | `/api/v1/orders/*`          | User/Admin     |
| Price Alerts  | `/api/v1/price-alerts/*`    | Authenticated  |

---

## ⚙️ Getting Started

### ✅ Prerequisites

- Java 21+
- Maven 3.8+
- MySQL
- Cloudinary API Key (for image upload)

### 🔧 Setup Instructions

1. **Clone the repository**

```bash
git clone https://github.com/your-username/anycart.git
cd anycart
```

2. **Configure application properties**

Edit `src/main/resources/application.yml` (or `.properties`) and add:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_db
    username: your_db_user
    password: your_db_pass

cloudinary:
  cloud-name: your_cloud_name
  api-key: your_api_key
  api-secret: your_api_secret

jwt:
  secret: your_secret_key
```

3. **Run the application**

```bash
./mvnw spring-boot:run
```

Server runs at `http://localhost:8080`.

---

## 🔐 Authentication Flow

- **Register**: `POST /api/auth/register`
- **Login**: `POST /api/auth/login`
- **Access secured endpoints** using:

```
Authorization: Bearer <your_token>
```

---

## 🧪 Testing

To run tests:

```bash
./mvnw test
```

---

## 📫 Contact

Developed by **Samarth Koli**  
GitHub: [@samarthkoli](https://github.com/samarthkoli)  
Email: `samarthkoli@protonmail.com`

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
