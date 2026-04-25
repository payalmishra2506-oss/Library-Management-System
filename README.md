# Library-Management-System
A Java Swing and MySQL based desktop application designed to manage library operations efficiently. This system provides a user-friendly graphical interface for handling books, users, and transactions such as issuing and returning books.

LibraryManagementSystem/
│
├── src/
│   ├── DB.java                 ← Database connection
│   ├── LoginGUI.java          ← Login & Register screen
│   ├── LibraryGUI.java        ← Main system (books, actions)
│   ├── Dashboard.java         
│
├── lib/
│   └── mysql-connector-j.jar  ← MySQL driver
│
├── database/
│   ├── schema.sql             ← Tables (users, books)
│   └── sample_data.sql        ← Sample records
│
├── README.md                  ← Project description
└── .gitignore                 ← Ignore compiled files

---
## Features
* Login system with database authentication
* Add, update, and delete books
* Search books by name or author
* Issue and return books
* Dashboard displaying total books, issued books, and students
* Real-time data refresh

---
## Technologies Used
* Java (Swing) for GUI development
* MySQL for database management
* JDBC for database connectivity


