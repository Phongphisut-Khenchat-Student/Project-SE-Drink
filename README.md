# 🥤 SE BUU Drink System

> A Java-based beverage vending machine management system with user authentication and drink ordering capabilities.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Files](https://img.shields.io/badge/Files-4-success?style=for-the-badge)

## 🎯 Features

- 🛒 Drink ordering system
- 🔐 PIN verification system
- 📊 Popular drink analytics
- 🤖 Virtual machine simulation
- 👤 User authentication
- 📝 User management system
- 📈 Machine statistics

## 🚀 System Features

### 1. Main Menu Options
- Ordering your drink
- PIN Check
- Most popular drink analysis
- Virtual machine access
- Admin login
- Exit system

### 2. Popular Drink Analytics
- For men
- For women
- For all customers
- Statistical analysis

### 3. Admin Features
- 🔍 Machine details with sorting options:
  - Sort by balance (DESC)
  - Sort by city (ASC)
- 👥 User management:
  - Add new users
  - Edit existing users

## 💾 File Structure

The system uses several text files for data storage:
```
src/
│
├── .gitattributes          # Git configuration settings
├── README.md               # Project documentation
│
├── DrinkRecord.java        # Code for recording drink data
├── LoginRecord.java        # Code for recording login data
├── Main.java               # Main code of the project
├── Menu.java               # Code for managing the menu
├── Order.java              # Code for managing orders
│
├── Login.txt               # File for login data
├── Machine.txt             # File for machine-related data (possibly for an automatic vending machine)
├── Menu.txt                # File for menu data
├── Order.txt               # File for order data
```

## 🛠️ Technical Implementation

### Classes
1. `Main.java`: Program entry point and main menu handling
2. `Order`: Order management and PIN verification
3. `Menu`: Drink menu and popularity tracking
4. `LoginRecord`: User authentication and management
5. `DrinkRecord`: Virtual machine operations

### Security Features
- Maximum 3 login attempts
- Password protection
- Masked telephone numbers
- PIN verification for orders

## 🚦 Getting Started

1. Ensure Java is installed on your system
2. Clone the repository:
```bash
git clone https://github.com/Phongphisut-Khenchat-Student/Project-SE-Drink.git
```
3. Compile the Java files:
```bash
javac Main.java
```
4. Run the program:
```bash
java Main
```

## 📋 Prerequisites

- Java Development Kit (JDK) 8 or higher
- Text files with proper read/write permissions
- Sufficient disk space for data storage

## 💡 Usage

### Customer Functions
1. Order drinks through the main menu
2. Verify orders using PIN
3. Check popular drink statistics

### Admin Functions
1. Login with credentials
2. View machine details with sorting options
3. Manage user accounts
4. Monitor system statistics

## 🔒 Security Notes

- Passwords are stored securely
- User session management implemented
- Data validation on all inputs
- File access permissions required

## 👥 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

Made with ☕ by SE BUU Students
