# Filters SB3 - Dynamic Filter Management System

A Spring Boot 3 application for managing dynamic filters with support for multiple criteria types including text, date, amount, and selection-based filtering.

## 🚀 Features

- **Dynamic Filter Creation**: Create and manage filters with multiple criteria
- **Multiple Criteria Types**:
  - Text filters (contains, equals, starts with, ends with, etc.)
  - Date filters (before, after, between, etc.)
  - Amount filters (equals, greater than, less than, between, etc.)
  - Selection filters (single/multiple selection)
- **RESTful API**: Easy-to-use REST endpoints for filter management
- **Persistence**: JPA-based data persistence with Liquibase migrations
- **In-Memory Database**: H2 database for quick setup and testing

## 📋 Prerequisites

- Java 21 or higher
- Gradle 8.x (wrapper included)

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Java Version**: 21
- **Build Tool**: Gradle
- **ORM**: Spring Data JPA
- **Database**: H2 (in-memory)
- **Migration**: Liquibase
- **Mapping**: ModelMapper
- **Utilities**: Lombok

## 🚦 Getting Started

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd filters-sb3
   ```

2. **Build the project**
   ```bash
   ./gradlew clean build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

### Accessing H2 Console

The H2 console is available at: `http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: _(empty)_

## 📚 API Documentation

### Base URL

- [http://localhost:8080/api/filters]

### Endpoints

#### 1. Create a Filter

**POST** `/api/filters`

Creates a new filter with multiple criteria.

**Request Body:**

```json
{
  "name": "Filter Name",
  "criteria": [
    {
      "type": "TEXT",
      "key": "text-criteria-key",
      "operator": "CONTAINS",
      "value": "text-criteria-value"
    }
]}
```
**Response:** `201 Created`

#### 2. Get All Filters

**GET** `/api/filters`

Retrieves all filters.

**Response:** `200 OK`

## 🧪 Running Tests

Run the test suite:

- `./gradlew test`

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🐛 Known Issues

- H2 database is for development only; configure a production database for deployment
- API currently has no authentication/authorization

## 📞 Support

For issues and questions, please open an issue in the repository.

## 🗺️ Roadmap

- [ ] Add API authentication and authorization
- [ ] Add Swagger/OpenAPI documentation
- [ ] Implement pagination for filter lists
- [ ] Add filter execution/application logic
- [ ] Support for more complex query operators (OR, nested conditions)
- [ ] Export/import filter definitions
- [ ] Filter templates and presets

---

**Version:** 0.0.1-SNAPSHOT  
**Spring Boot:** 3.5.7  
**Java:** 21
