import java.util.*;

public class Main{
    private static GradeTracker tracker = new GradeTracker();
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args){
        System.out.println("=== ENHANCED GRADE TRACKER ===");
        
        while(true) {
            showMainMenu();
            int choice = getIntInput("Choose option: ");
            
            switch(choice) {
                case 1: addStudent();
                        break;
                case 2: addCourse();
                        break;
                case 3: enrollStudentInCourse(); 
                        break;
                case 4: addGrade(); 
                        break;
                case 5: viewStudentReport(); 
                        break;
                case 6: viewCourseReport(); 
                        break;
                case 7: showGradePredictor(); 
                        break;
                case 8: System.out.println("Goodbye!");
                        return;
                default:System.out.println("Invalid option!");
            }
        }
    }
    
    private static void showMainMenu(){
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Add Student");
        System.out.println("2. Add Course");
        System.out.println("3. Enroll Student in Course");
        System.out.println("4. Add Grade");
        System.out.println("5. View Student Report");
        System.out.println("6. View Course Report");
        System.out.println("7. Grade Predictor");
        System.out.println("8. Exit");
    }
    
    private static void addStudent(){
        System.out.println("\n--- ADD STUDENT ---");
        String id = getStringInput("SR-Code: ");
        String name = getStringInput("Name: ");
        String major = getStringInput("Major: ");
        
        if(tracker.addStudent(id, name, major)){
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Student already exists!");
        }
    }
    
    private static void addCourse(){
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
        System.out.println("Course added successfully!");
    }
    
    private static void enrollStudentInCourse(){
        System.out.println("\n--- ENROLL STUDENT ---");
        String studentId = getStringInput("Student SR-Code: ");
        String courseCode = getStringInput("Course Code: ");
        
        if(tracker.enrollStudent(studentId, courseCode)) {
            System.out.println("Enrollment successful!");
        } else {
            System.out.println("Enrollment failed! Check if student/course exists.");
        }
    }
    
    private static void addGrade(){
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
            System.out.println("Invalid choice!");
            return;
        }
        
        String assignmentType = types.get(typeChoice);
        double grade = getDoubleInput("Grade (0-100): ");
        String assignmentName = getStringInput("Assignment Name: ");
        
        if(tracker.addGrade(studentId, courseCode, assignmentType, assignmentName, grade)) {
            System.out.println("Grade added successfully!");
        } else {
            System.out.println("Failed to add grade!");
        }
    }
    
    private static void viewStudentReport(){
        System.out.println("\n--- STUDENT REPORT ---");
        String studentId = getStringInput("Student SR-Code: ");
        tracker.displayStudentReport(studentId);
    }
    
    private static void viewCourseReport(){
        System.out.println("\n--- COURSE REPORT ---");
        String courseCode = getStringInput("Course Code: ");
        tracker.displayCourseReport(courseCode);
    }
    
    private static void showGradePredictor(){
        System.out.println("\n--- GRADE PREDICTOR ---");
        String studentId = getStringInput("Student SR-Code: ");
        String courseCode = getStringInput("Course Code: ");
        tracker.showGradePredictor(studentId, courseCode);
    }

    private static int getIntInput(String prompt){
        System.out.print(prompt);
        while(!sc.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            sc.next();
            System.out.print(prompt);
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }
    
    private static double getDoubleInput(String prompt){
        System.out.print(prompt);
        while(!sc.hasNextDouble()){
            System.out.println("Please enter a valid number!");
            sc.next();
            System.out.print(prompt);
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }
    
    private static String getStringInput(String prompt){
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
}