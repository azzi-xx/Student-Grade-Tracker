# Student Grade Tracker System

## 1. Project Title
**Student Grade Tracker System** - A Comprehensive Academic Management Console Application

## 2. Description/Overview
The Student Grade Tracker System is a console-based Java application designed to manage and track student academic performance. It serves as a digital replacement for traditional grade books, providing teachers and academic administrators with a powerful tool to record, analyze, and report on student grades across multiple courses.

**Key Features:**
- Student and course management with unique identifiers
- Weighted grading systems (homework, quizzes, exams, projects)
- Grade prediction and "what-if" scenario analysis
- Database persistence with automatic save/load functionality
- Comprehensive reporting and search capabilities
- CSV export for compatibility with spreadsheet software

**Problem Solved:**
This system eliminates the manual calculation errors, disorganization, and data loss risks associated with paper-based or spreadsheet grade tracking. It provides a structured, reliable way to manage academic data throughout an entire semester or academic year.

## 3. OOP Concepts Applied

### Encapsulation
- **Student Class**: Bundles student data (SR Code, name, major) with grade management methods
- **Course Class**: Encapsulates course details and grading weights with controlled access
- **GradeTracker Class**: Manages the overall system while hiding internal data structures
- **Private fields** with public getter methods maintain data integrity

### Abstraction
- **GradeTracker** provides a simplified interface for complex operations like weighted grade calculation
- **Student** class abstracts the complexity of grade storage and averaging
- Users interact with high-level menu options without needing to understand internal algorithms

### Inheritance
- While not extensively used in this basic version, the structure allows for easy extension:
  - Potential for `UndergraduateStudent` and `GraduateStudent` subclasses
  - Possible `ElectiveCourse` and `CoreCourse` hierarchy

### Polymorphism
- **Method Overloading**: Multiple `addGrade()` methods handle different parameter sets
- **Collections Framework**: Uses polymorphic collections (List<>, Map<>) for flexible data storage
- **Stream Operations**: Utilizes Java streams for operations like average calculation

### Composition
- **GradeTracker** is composed of `Map<String, Student>` and `Map<String, Course>`
- **Student** contains `Map<String, Map<String, List<Double>>>` for grade organization
- This "has-a" relationship allows for complex data structures while maintaining separation of concerns

## 4. Program Structure

### Class Relationships:
