import java.util.*;

public class Main {
    private static GradeTracker tracker = new GradeTracker();
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Loading existing data...");
        tracker.loadAllData();
        
        while(true) {
            showMainMenu();
            int choice = getIntInput("Choose option: ");
            
            switch(choice) {
                case 1: addStudent(); break;
                case 2: addCourse(); break;
                case 3: enrollStudentInCourse(); break;
                case 4: addGrade(); break;
                case 5: viewStudentReport(); break;
                case 6: viewCourseReport(); break;
                case 7: showGradePredictor(); break;
                case 8: viewDatabase(); break;
                case 9: searchDatabase(); break;
                case 10: saveData(); break;
                case 11: loadData(); break;
                case 12: exportStudentReport(); break;
                case 13: 
                    tracker.saveAllData();
                    System.out.println("Goodbye!");
                    return;
                default: 
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("                   MAIN MENU");
        System.out.println("=".repeat(50));
        
        // First column (1-7)
        System.out.printf("%-2s. %-25s", "1", "Add Student");
        System.out.printf("%-2s. %-25s%n", "8", "View Database");
        
        System.out.printf("%-2s. %-25s", "2", "Add Course");
        System.out.printf("%-2s. %-25s%n", "9", "Search Database");
        
        System.out.printf("%-2s. %-25s", "3", "Enroll Student");
        System.out.printf("%-2s. %-25s%n", "10", "Save Data");
        
        System.out.printf("%-2s. %-25s", "4", "Add Grade");
        System.out.printf("%-2s. %-25s%n", "11", "Load Data");
        
        System.out.printf("%-2s. %-25s", "5", "View Student Report");
        System.out.printf("%-2s. %-25s%n", "12", "Export Report");
        
        System.out.printf("%-2s. %-25s", "6", "View Course Report");
        System.out.printf("%-2s. %-25s%n", "13", "Exit");
        
        System.out.printf("%-2s. %-25s%n", "7", "Grade Predictor");
        System.out.println("-".repeat(50));
    }
    
    private static void addStudent() {
        System.out.println("\n--- ADD STUDENT ---");
        String id = getStringInput("SR-Code: ");
        String name = getStringInput("Name: ");
        String major = getStringInput("Major: ");
        
        if(tracker.addStudent(id, name, major)) {
            System.out.println("SUCCESS: Student added successfully!");
        } else {
            System.out.println("ERROR: Student already exists!");
        }
    }
    
    private static void addCourse() {
        System.out.println("\n--- ADD COURSE ---");
        String code = getStringInput("Course Code: ");
        String name = getStringInput("Course Name: ");
        int credits = getIntInput("Credits: ");
        
        System.out.println("\nAssignment Categories:");
        System.out.println("1. Homework (20%) + Quizzes (30%) + Exams (50%)");
        System.out.println("2. Projects (40%) + Exams (60%)");
        System.out.println("3. Custom weights");
        
        int weightChoice = getIntInput("Choose weight scheme: ");
        tracker.addCourse(code, name, credits, weightChoice);
        System.out.println("SUCCESS: Course added successfully!");
    }
    
    private static void enrollStudentInCourse() {
        System.out.println("\n--- ENROLL STUDENT ---");
        String studentId = getStringInput("Student SR-Code: ");
        String courseCode = getStringInput("Course Code: ");
        
        if(tracker.enrollStudent(studentId, courseCode)) {
            System.out.println("SUCCESS: Enrollment successful!");
        } else {
            System.out.println("ERROR: Enrollment failed! Check if student/course exists.");
        }
    }
    
    private static void addGrade() {
        System.out.println("\n--- ADD GRADE ---");
        String studentId = getStringInput("Student SR-Code: ");
        String courseCode = getStringInput("Course Code: ");
        
        System.out.println("Assignment Types:");
        List<String> types = tracker.getAssignmentTypes(courseCode);
        for(int i = 0; i < types.size(); i++) {
            System.out.println((i+1) + ". " + types.get(i));
        }
        
        int typeChoice = getIntInput("Choose assignment type: ") - 1;
        if(typeChoice < 0 || typeChoice >= types.size()) {
            System.out.println("ERROR: Invalid choice!");
            return;
        }
        
        String assignmentType = types.get(typeChoice);
        double grade = getDoubleInput("Grade (0-100): ");
        String assignmentName = getStringInput("Assignment Name: ");
        
        if(tracker.addGrade(studentId, courseCode, assignmentType, assignmentName, grade)) {
            System.out.println("SUCCESS: Grade added successfully!");
        } else {
            System.out.println("ERROR: Failed to add grade!");
        }
    }
    
    private static void viewStudentReport() {
        System.out.println("\n--- STUDENT REPORT ---");
        String studentId = getStringInput("Student SR-Code: ");
        tracker.displayStudentReport(studentId);
    }
    
    private static void viewCourseReport() {
        System.out.println("\n--- COURSE REPORT ---");
        String courseCode = getStringInput("Course Code: ");
        tracker.displayCourseReport(courseCode);
    }
    
    private static void showGradePredictor() {
        System.out.println("\n--- GRADE PREDICTOR ---");
        String studentId = getStringInput("Student SR-Code: ");
        String courseCode = getStringInput("Course Code: ");
        tracker.showGradePredictor(studentId, courseCode);
    }
    
    private static void viewDatabase() {
        System.out.println("\n--- DATABASE VIEWER ---");
        System.out.println("1. View All Courses");
        System.out.println("2. View All Students");
        System.out.println("3. Database Overview");
        System.out.println("4. Back to Main Menu");
        
        int choice = getIntInput("Choose: ");
        
        switch (choice) {
            case 1: tracker.displayAllCourses(); break;
            case 2: tracker.displayAllStudents(); break;
            case 3: tracker.displayDatabaseOverview(); break;
            case 4: return;
            default: System.out.println("ERROR: Invalid choice!");
        }
    }
    
    private static void searchDatabase() {
        tracker.searchDatabase();
    }
    
    private static void saveData() {
        System.out.println("\n--- SAVE DATA ---");
        if (tracker.saveAllData()) {
            System.out.println("SUCCESS: All data saved successfully!");
        } else {
            System.out.println("ERROR: Failed to save data!");
        }
    }
    
    private static void loadData() {
        System.out.println("\n--- LOAD DATA ---");
        if (tracker.loadAllData()) {
            System.out.println("SUCCESS: Data loaded successfully!");
        } else {
            System.out.println("ERROR: No data found or error loading!");
        }
    }
    
    private static void exportStudentReport() {
        System.out.println("\n--- EXPORT STUDENT REPORT ---");
        String studentId = getStringInput("Student SR-Code: ");
        String filename = getStringInput("Export filename (e.g., report.txt): ");
        tracker.exportStudentReport(studentId, filename);
    }
    
    // Helper methods
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextInt()) {
            System.out.println("ERROR: Please enter a valid number!");
            sc.next();
            System.out.print(prompt);
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }
    
    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextDouble()) {
            System.out.println("ERROR: Please enter a valid number!");
            sc.next();
            System.out.print(prompt);
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
}