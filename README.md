# 📚 Library Management System

A full-featured **Java Swing + MySQL desktop application** for managing books, students, and library operations with role-based access.

---

## ✨ Features

### 👨‍💼 Admin

* Add / Update / Delete books
* Issue & Return books
* View student list with roll numbers
* Track issued books

### 🎓 Student

* View available books
* Search & filter books
* Restricted access (no modifications)

---

## 📁 Project Structure

```
LibraryManagementSystem/
│
├── src/
│   ├── DB.java                ← Database connection
│   ├── LoginGUI.java         ← Login & registration system
│   ├── LibraryGUI.java       ← Main library interface
│
├── lib/
│   └── mysql-connector.jar   ← MySQL driver
│
├── database/
│   ├── schema.sql            ← Database structure
│   └── sample_data.sql       ← Sample data (admin + books)
│
└── README.md
```

## 📸 Screenshots

### 📊 Library Dashboard
> Displays all books with category and issue status.

![Dashboard](screenshots/dashboard.png)

---

### ➕ Add Book
> Simple dialog to add new books into the system.

![Add Book](screenshots/add-book.png)

---

### ✅ Book Added
> Newly added book reflected instantly in the system.

![Book Added](screenshots/book-added.png)

## ⚡ Quick Start

### 1. Setup MySQL Database
Open MySQL and run:

```sql
CREATE DATABASE librarydb;
USE librarydb;

CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50),
    role VARCHAR(20),
    roll_no VARCHAR(20)
);

CREATE TABLE books (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    author VARCHAR(100),
    category VARCHAR(50),
    issued BOOLEAN,
    student_id VARCHAR(20),
    issue_date DATE
);
```
### 2. Add Admin User
```sql
INSERT INTO users VALUES ('admin', '1234', 'ADMIN', NULL);
```

### 3. Run the Application
Compile:
```
javac -cp ".;mysql-connector-j-9.6.0.jar" src/*.java
```
Run:
```
java -cp ".;mysql-connector-j-9.6.0.jar;src" LoginGUI
```
## 🔐 Authentication System
* Admin has full access
* Students are restricted
* Registration auto-assigns **STUDENT role**
* Roll number required during registration

## 🔍 Features Overview
| Feature       | Description                          |
| ------------- | ------------------------------------ |
| Search        | Find books by name, author, category |
| Filter        | Show issued / available books        |
| Issue System  | Assign books to students             |
| Return System | Mark books as returned               |
| Student Panel | View all registered students         |

## 🎨 UI Highlights
* Clean table design
* Alternating row colors
* Action buttons with role control
* Simple and functional layout

## 🛠 Tech Stack
* **Java (Swing)** → GUI
* **MySQL** → Database
* **JDBC** → Connectivity
* 
## ⚙️ Customization
* Change DB credentials in `DB.java`
* Modify UI colors in `LibraryGUI.java`
* Add new features like:
  * Due dates
  * Fine system
  * Reports

## ⚠️ Disclaimer
This project is for educational purposes.
Not optimized for production level security (e.g., passwords are not hashed).

## 🚀 Future Improvements

* 🔐 Password hashing (BCrypt)
* 📊 Dashboard charts
* 📅 Due date + fine system
* 📤 Export data to Excel
* 🌙 Dark mode UI
  
## 👨‍💻 Author
Payal mishra
