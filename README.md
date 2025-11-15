# Digital Mall - E-Commerce Application

A comprehensive console-based e-commerce application built in Java with MySQL database integration. This project provides a complete digital shopping mall experience with user management, product catalog, order processing, and administrative features.

## 📋 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Features Overview](#features-overview)
- [Contributing](#contributing)

## ✨ Features

### User Features
- **User Registration & Authentication**
  - Secure registration with email validation (Gmail only)
  - Password validation (requires numbers and special characters)
  - Phone number validation (10 digits)
  - User login system

- **Shopping Experience**
  - Browse products by store name
  - Browse products by product name
  - View product images
  - Add multiple items to cart
  - Real-time stock availability checking
  - View store details and categories

- **Order Management**
  - Place orders with multiple products
  - Track order status (SHIPPED, DELIVERED, CANCELLED)
  - View order history
  - Cancel orders (for orders in transit)
  - Automatic bill generation (saved as text file)

- **Payment System**
  - Payment confirmation interface
  - Order completion workflow

### Admin Features
- **Store Management**
  - Add new stores
  - Update store information
  - Delete stores
  - View all stores

- **Product Management**
  - Add products with images
  - Update product details (name, brand, price, stock)
  - Delete products
  - View all products
  - Manage product inventory

- **System Management**
  - View activity logs
  - Monitor user activities

### System Features
- **Automatic Order Status Updates**
  - Orders automatically marked as "DELIVERED" after 2 days
  - Background thread for continuous status monitoring

- **Activity Logging**
  - Comprehensive logging system
  - Logs user login/logout events
  - Logs payment transactions
  - Daily log files with timestamps

- **Data Structures**
  - Custom linked list implementation for cart management
  - Custom linked list for activity logging

## 🛠 Technologies Used

- **Programming Language**: Java
- **Database**: MySQL
- **Database Connectivity**: JDBC (Java Database Connectivity)
- **GUI Components**: Java Swing (for dialog boxes)
- **File I/O**: Java File I/O for bill generation and logging

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

1. **Java Development Kit (JDK)**
   - Version 8 or higher
   - [Download JDK](https://www.oracle.com/java/technologies/downloads/)

2. **MySQL Server**
   - Version 5.7 or higher
   - [Download MySQL](https://dev.mysql.com/downloads/mysql/)

3. **MySQL JDBC Driver**
   - mysql-connector-java.jar
   - [Download MySQL Connector](https://dev.mysql.com/downloads/connector/j/)

4. **IDE (Optional but Recommended)**
   - IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## 🚀 Installation

1. **Clone or download the repository**
   ```bash
   git clone <repository-url>
   cd SEM-II_Digital_Mall
   ```

2. **Add MySQL JDBC Driver**
   - Download `mysql-connector-java.jar`
   - Place it in the `lib` folder of your project
   - Add it to your project's classpath

3. **Set up the project structure**
   - Ensure the following directories exist:
     - `src/` - Source code files
     - `bin/` - Compiled class files
     - `lib/` - External libraries
     - `PICS/` - Product images storage
   - Create these directories if they don't exist

## 🗄 Database Setup

1. **Create MySQL Database**
   ```sql
   CREATE DATABASE digital_mall;
   USE digital_mall;
   ```

2. **Create Required Tables**

   The application uses the following tables (you'll need to create them based on the code structure):
   - `Users` - User information
   - `stores` - Store details
   - `products` - Product catalog
   - `orders` - Order information
   - `order_item` - Order line items
   - `category` (if used)

3. **Create Stored Procedures**

   The application uses several stored procedures. You'll need to create them in your MySQL database:
   - `getUserIdbyEmail`
   - `displayOrders`
   - `getStoreId`
   - `getStockAmount`
   - `displayStores`
   - `deleteStore`
   - `checkPresenceForRegister`
   - `verifyPassword`
   - `displayHistory`
   - `trackOrder`
   - `getStoreIdByProductId`
   - `getPriceByProductId`

4. **Database Connection**
   - Default connection: `jdbc:mysql://localhost:3306/digital_mall`
   - Default username: `root`
   - Default password: (empty - update in code if needed)

## ⚙️ Configuration

### Update Database Connection

Edit the database connection strings in `src/App.java`:

```java
// Line 219, 812, 1065
con = DriverManager.getConnection("jdbc:mysql://localhost:3306/digital_mall", "root", "YOUR_PASSWORD");
```

### Update File Paths

If your project is located in a different directory, update the file paths in:
- Line 441: Product image path
- Line 480: Product image output path
- Line 1015: Log file directory
- Line 1852: Log file path

## 🎮 Usage

### Compiling the Application

```bash
javac -cp "lib/mysql-connector-java.jar" src/App.java
```

### Running the Application

```bash
java -cp "bin:lib/mysql-connector-java.jar" App
```

Or from your IDE:
- Run `App.java` as the main class

### User Workflow

1. **Start the application**
   - You'll see the welcome screen

2. **Register a new user** (Option 1)
   - Enter your details
   - Email must end with `@gmail.com`
   - Password must contain at least one number and one special character
   - Phone number must be exactly 10 digits

3. **Login** (Option 2)
   - Enter your registered email and password
   - Access the shopping menu

4. **Shopping Options**
   - View store details
   - Shop by store name
   - Shop by product name
   - View product images
   - Add items to cart
   - Complete purchase

5. **Order Management**
   - Track your orders
   - View order history
   - Cancel orders (if in transit)

### Admin Workflow

1. **Admin Login** (Option 3)
   - Login with admin credentials

2. **Admin Panel Options**
   - Manage stores (add, update, delete, view)
   - Manage products (add, update, delete, view)
   - View system logs

## 📁 Project Structure

```
SEM-II_Digital_Mall/
│
├── src/                    # Source code files
│   ├── App.java           # Main application class
│   ├── bill.java          # Bill generation utility
│   ├── TEST.java          # Test file
│   └── h1.html            # HTML file (if used)
│
├── bin/                    # Compiled class files
│   └── *.class            # Compiled Java classes
│
├── lib/                    # External libraries
│   └── mysql-connector-java.jar
│
├── PICS/                   # Product images storage
│   └── pic.png
│
├── LOGS -- DD-MM-YYYY      # Daily log files
│
├── billof*.txt            # Generated bill files
│
└── README.md              # This file
```

## 🔍 Features Overview

### Core Classes

- **App**: Main application class with menu system
- **users**: User registration, login, and authentication
- **products**: Product management and inventory
- **stores**: Store management
- **orders**: Order processing and tracking
- **order_items**: Order line items management
- **linkedlist**: Custom linked list for activity logging
- **linkedList2**: Custom linked list for shopping cart

### Key Functionalities

1. **User Authentication**
   - Email validation
   - Password strength validation
   - Secure login system

2. **Product Catalog**
   - Product browsing
   - Image viewing
   - Stock management
   - Price display

3. **Shopping Cart**
   - Add multiple items
   - Quantity selection
   - Stock validation
   - Cart management using custom linked list

4. **Order Processing**
   - Order creation
   - Automatic stock deduction
   - Bill generation
   - Order status tracking

5. **Admin Panel**
   - Complete CRUD operations for stores and products
   - System monitoring through logs

6. **Logging System**
   - User activity tracking
   - Payment logging
   - Daily log file generation

## 🔒 Security Notes

- **Database Password**: Currently set to empty string. Update before production use.
- **Password Storage**: Passwords are stored in plain text. Consider hashing for production.
- **SQL Injection**: Uses PreparedStatements and CallableStatements for protection.
- **Input Validation**: Includes validation for email, password, and phone numbers.

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure MySQL server is running
   - Verify database name, username, and password
   - Check if JDBC driver is in classpath

2. **Class Not Found Error**
   - Ensure MySQL connector JAR is in the `lib` folder
   - Add it to your classpath

3. **File Path Errors**
   - Update file paths in the code to match your system
   - Ensure directories exist (PICS, LOGS)

4. **Stored Procedure Errors**
   - Ensure all stored procedures are created in the database
   - Verify procedure names match exactly

## 📝 Notes

- The application uses a console-based interface
- Product images should be placed in the `src/` directory
- Bills are generated as text files in the project root
- Logs are created daily with date stamps
- Order status automatically updates to "DELIVERED" after 2 days

## 🤝 Contributing

This appears to be a semester project. If you'd like to contribute:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

This project appears to be an academic project. Please check with the project owner for licensing information.

## 👥 Authors

- Project developed as part of SEM-II coursework

## 🙏 Acknowledgments

- Built using Java and MySQL
- Uses JDBC for database connectivity
- Custom data structures for cart and logging management

---

**Note**: This is a console-based application. For a production e-commerce system, consider adding:
- Web interface
- Payment gateway integration
- Enhanced security measures
- Email notifications
- Product search and filtering
- User reviews and ratings
