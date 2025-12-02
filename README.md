# Project Title: 🎓 Student Grade Tracker System

# 📋 Description/Overview
The Student Grade Tracker System is a console-based Java application designed to manage and monitor student academic performance. It provides teachers and administrators with a digital solution to record, analyze, and report student grades across multiple courses. The system supports weighted grading, grade predictions, and data persistence, making it an essential tool for efficient academic management.

# 🧮 OOP Concepts Applied

### 🔒 Encapsulation
Encapsulation was implemented by making all instance variables private and providing public getter methods. In the `Student` class, the `srCode`, `name`, `major`, and grades Map are private, ensuring data integrity and controlled access.

### 🎭 Abstraction
Abstraction was achieved through simplified interfaces like the menu system in `Main.java`. Users interact with options like "Add Grade" without needing to know the complex calculations happening in the `GradeTracker` class.

### 👨‍👦 Inheritance
Inheritance was not extensively used in this project, but the structure allows for future extensions. For example, different types of students or courses could inherit from base classes in later versions.

### 🔄 Polymorphism
Polymorphism was implemented through method overloading in the `addGrade()` methods and the use of Java Collections. The system uses `List<Double>` and `Map<String, Student>` to handle different data types flexibly.

# 🏗️ Program Structure

## Main Classes and Their Roles

## **1. 🖥️ Main.java**
The main entry point of the application that handles user interaction. It displays the menu system, captures user input, and calls the appropriate methods from the GradeTracker class. This class acts as the user interface layer.

## **2. ⚙️ GradeTracker.java**
The controller class that manages all core operations. It handles student and course management, grade calculations, data persistence (save/load), and reporting functionality. This class contains the main business logic of the application.

## **3. 👨‍🎓 Student.java**
The data model representing individual students. It stores student information (SR Code, name, major) and manages their grades using nested data structures. This class also calculates weighted averages for each course.

## **4. 📚 Course.java**
The data model representing academic courses. It stores course details (code, name, credits) and defines the grading weight system for different assignment types (homework, quizzes, exams, projects).

# 🚀 How to Run the Program

## Prerequisites
- ✅ Java Development Kit (JDK) 8 or higher installed
- ✅ Command line/terminal access
- ✅ Project files organized in a folder structure

## **Step-by-Step Instructions**

### **Step 1: Organize The Files**
Create a folder structure with your Java files:

StudentGradeTracker/
-Main.java
-GradeTracker.java
-Student.java
-Course.java
  

## **Step 2: Open Command Line**
- **Windows**: Open Command Prompt or PowerShell
- **Mac/Linux**: Open Terminal

## **Step 3: Navigate to Your Project Folder**
```bash
cd path/to/your/StudentGradeTracker
```
## **Step 4: Compile All Java Files**
javac *.java

## **Run the Program**
java Main

# 📊 Sample Output of the Program 

<img width="523" height="353" alt="image" src="https://github.com/user-attachments/assets/24ff6fe3-849b-4108-8543-9b826438cceb" />

## 👥Contributors

### Development Team
| Name | Link To Github Profile |
|------|---------------|
| Asi Eric Daniel | [[GitHub Profile Link](https://github.com/azzi-xx)] |
| De Villa Matt Ervin | [[GitHub Profile Link](https://github.com/mattyyy06)] |
| Jun Andrei Manalo | [[GitHub Profile Link](https://github.com/itsmekurt652)] |










