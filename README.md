# 🛍️ Auction Platform

A simple auction web backend built with Spring Boot. Users can create item listings, place bids, and (optionally) register/login using Spring Security. This project was built as a personal portfolio piece to demonstrate backend development skills using Java and Spring Framework.

---

## 📦 Features

- Create auction item listings with a deadline and category
- Place bids on items
- View all listings and current highest bids
- Optional: User registration and login (session-based authentication)
- RESTful API structure, clean separation of concerns
- In-memory H2 database for easy development/testing

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Web (REST APIs)
- Spring Data JPA (Hibernate)
- Spring Security (session-based login)
- H2 Database (can switch to MySQL)
- Lombok (for less boilerplate)

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Gradle 7+
- IDE (IntelliJ, VSCode, etc.)

### 🔧 Running the App

```bash
./gradlew bootRun
```

- API Base URL: `http://localhost:8080/api`
- Auth Endpoints: `http://localhost:8080/auth`
- H2 Console: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: *(leave blank)*

---

## 📂 Project Structure

```
src/
├── controller/       # REST API endpoints
├── service/          # Business logic
├── repository/       # JPA interfaces
├── model/            # Entity classes (Item, Bid, User)
├── config/           # Spring Security config
├── resources/        # application.yml, static files
└── test/             # Unit/integration tests (optional)
```

---

## 📌 Sample Endpoints

- `POST /auth/register` – Create a new user
- `POST /login` – Login with Spring Security form
- `POST /api/items` – Create item listing
- `GET /api/items` – Get all listings
- `POST /api/items/{itemId}/bids` – Place bid on item

Test with Postman or curl. Login is session-based and works with browser cookies.

---

## 🔐 Authentication

- Uses Spring Security form login
- User passwords hashed with BCrypt
- Protected endpoints require authentication

---

## 🧩 To-Do / Next Steps

- [ ] Add Swagger API documentation
- [ ] Add role-based access (e.g., admin)
- [ ] Auto-close listings on deadline
- [ ] Add email notifications (e.g., winning bid)
- [ ] Frontend client (React or Thymeleaf)

---

## 👤 Author

**Heekwon Seo**  
Built to demonstrate backend development skills using Java and Spring.  

---

## 📄 License

This project is open source and free to use under the [MIT License](LICENSE).