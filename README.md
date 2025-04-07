# Knowledge Item API

This project is a RESTful API built to handle knowledge items using a PostgreSQL database. The API allows for CRUD operations on knowledge items, and supports basic validation and indexing. 

## Table of Contents

- [Setup Instructions](#setup-instructions)
- [Tech Stack & Database](#tech-stack-and-database)
- [Running the App and Seeding Script](#running-the-app-and-seeding-script)
- [API Design, Validation, and Indexing](#api-design-validation-and-indexing)

## Setup Instructions

Follow these steps to get the project up and running:

1. **Clone the Repository:**

    ```bash
    https://github.com/sabonn/REST-API.git
    ```
    Then switch to the c8 branch

2. **Install Dependencies:**

    Install both backend and frontend dependencies using npm.

    ```bash
    npm install
    ```

3. **Setup Environment Variables:**

    You may change the `.env` as you wish(NOT MANDATORY), but the Dockerfile and the docker-compose should be update as well.:

    ```ini
    DB_HOST=localhost
    DB_PORT=5432
    DB_NAME=knowledge_items
    DB_USER=your-db-user
    DB_PASSWORD=your-db-password
    ```

4. **Docker Environment:**

    Make sure you have docker installed, the entire project is Docker compatible.
---

## Tech Stack & Database

- **Backend:** Node.js with Express
- **Database:** PostgreSQL
- **API Client:** Axios and Faker for seeding scripts and testing
- **Testing:** Jest (for unit tests and API tests)
- **Docker:** For containerizing the application and the database (using Docker Compose)

---

## Running the App and Seeding Script

### Running the App

To run the app, use Docker Compose to launch both the Node.js application and PostgreSQL database.

1. **Start the Application and Database with Docker Compose:**

    Make sure Docker and Docker Compose are installed on your machine, then run:

    ```bash
    docker-compose up
    ```

    This command will start the application on `http://localhost:3000` and a PostgreSQL database instance.

2. **Stop the Application and Database:**

    To stop both the application and database:

    ```bash
    docker-compose down
    ```

### Running the Seeding Script

The seeding script generates random items using Faker.js and inserts them into the database. It is useful for testing purposes and to populate the database.

1. **Run the Seeding Script:**

    You can run the script by executing the following command:

    ```bash
    npm run seed
    ```

    This will populate the database with a set of 100 items, each with a title, subtitle, content, and tags.

2. **Customizing Seeding Data:**

    The seeding script is designed to generate random data. You can adjust the settings in the seeding script if needed either in the script it self or in the `.env` file.

---

## API Design, Validation, and Indexing

### API Design

The API follows RESTful conventions with the following endpoints:

- `GET /api/items/` - Retrieves all knowledge items.
- `GET /api/items/id` - Retrieves a knowledge item by its `id`.
- `POST /api/items/` - Creates a new knowledge item.
- `PUT /api/items/id` - Updates an existing knowledge item by its `id`.
- `DELETE /api/items/id` - Deletes a knowledge item by its `id`.
- `GET /api/items/tags` - Retrieves items filtered by one or more `tags`.

### Validation

The API performs basic validation for incoming data, such as ensuring that required fields are present and that the values conform to expected types.

- **Title and Subtitle**: These fields must be strings.
- **Content**: This field must be a string with a minimum length of 10 characters.
- **Tags**: Tags are validated to ensure they exist in the predefined set of tags (`cardiology`, `oncology`, `radiology`, `internal medicine`).

For each API endpoint, appropriate HTTP status codes are returned in response to validation failures (e.g., 400 Bad Request).

### Indexing

To optimize the performance of queries, particularly those involving tag lookups, a **GIN (Generalized Inverted Index)** is used on the `tags` field in the PostgreSQL database. This allows for efficient filtering and querying of items based on tags.

- **GIN Index on `tags`**: The `tags` field is indexed with a GIN index to allow fast queries like `SELECT * FROM items WHERE tags @> ARRAY['oncology']`.
  
---

## Example API Usage

### Create an Item

```bash
POST /api/items/

{
  "title": "Understanding Cardiology",
  "subtitle": "An introduction to heart health",
  "content": "Cardiology is a branch of medicine that deals with heart conditions...",
  "tags": ["cardiology"]
}
