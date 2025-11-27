import java.util.*;
import java.io.*;
import java.nio.file.*;

public class GradeTracker {
    private Map<String, Student> students;
    private Map<String, Course> courses;
    private Map<String, List<String>> courseEnrollments;
    
    public GradeTracker() {
        this.students = new HashMap<>();
        this.courses = new HashMap<>();
        this.courseEnrollments = new HashMap<>();
    }
    
    public boolean addStudent(String srCode, String name, String major) {
        if(students.containsKey(srCode)) {
            return false;
        }
        students.put(srCode, new Student(srCode, name, major));
        return true;
    }
    
    public void addCourse(String code, String name, int credits, int weightScheme) {
        courses.put(code, new Course(code, name, credits, weightScheme));
        courseEnrollments.putIfAbsent(code, new ArrayList<>());
    }
    
    public boolean enrollStudent(String studentId, String courseCode) {
        if(!students.containsKey(studentId) || !courses.containsKey(courseCode)) {
            return false;
        }
        
        students.get(studentId).enrollInCourse(courseCode);
        if(!courseEnrollments.get(courseCode).contains(studentId)) {
            courseEnrollments.get(courseCode).add(studentId);
        }
        return true;
    }
    
    public boolean addGrade(String studentId, String courseCode, String assignmentType, 
                           String assignmentName, double grade) {
        if(!students.containsKey(studentId) || !courses.containsKey(courseCode)) {
            return false;
        }
        
        if(grade < 0 || grade > 100) {
            System.out.println("ERROR: Grade must be between 0 and 100!");
            return false;
        }
        
        return students.get(studentId).addGrade(courseCode, assignmentType, assignmentName, grade);
    }
    
    public List<String> getAssignmentTypes(String courseCode) {
        if(courses.containsKey(courseCode)) {
            return courses.get(courseCode).getAssignmentTypes();
        }
        return new ArrayList<>();
    }
    
    public void displayStudentReport(String studentId) {
        if(!students.containsKey(studentId)) {
            System.out.println("ERROR: Student not found!");
            return;
        }
        
        Student student = students.get(studentId);
        System.out.println("\nSTUDENT REPORT: " + student.getName() + " (" + student.getSrCode() + ")");
        System.out.println("Major: " + student.getMajor());
        System.out.println("=" .repeat(50));
        
        List<String> enrolledCourses = student.getEnrolledCourses();
        if(enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled.");
            return;
        }
        
        for(String courseCode : enrolledCourses) {
            Course course = courses.get(courseCode);
            if(course != null) {
                double grade = student.getCourseGrade(courseCode, course.getWeights());
                String letterGrade = student.getGradeLetter(grade);
                
                System.out.printf("\n%s - %s (%.2f%% %s)%n", 
                    courseCode, course.getName(), grade, letterGrade);
                
                Map<String, List<Double>> assignments = student.getCourseGrades().get(courseCode);
                for(String assignmentType : assignments.keySet()) {
                    List<Double> grades = assignments.get(assignmentType);
                    double avg = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    System.out.printf("   %s: %.2f%% (%d assignments)%n", 
                        assignmentType, avg, grades.size());
                }
            }
        }
    }
    
    public void displayCourseReport(String courseCode) {
        if(!courses.containsKey(courseCode)) {
            System.out.println("ERROR: Course not found!");
            return;
        }
        
        Course course = courses.get(courseCode);
        System.out.println("\nCOURSE REPORT: " + course.getCode() + " - " + course.getName());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Enrolled Students: " + courseEnrollments.get(courseCode).size());
        System.out.println("=" .repeat(50));
        
        List<Double> allGrades = new ArrayList<>();
        for(String studentId : courseEnrollments.get(courseCode)) {
            Student student = students.get(studentId);
            double grade = student.getCourseGrade(courseCode, course.getWeights());
            allGrades.add(grade);
            
            System.out.printf("%s - %s: %.2f%% (%s)%n", 
                student.getSrCode(), student.getName(), grade, student.getGradeLetter(grade));
        }
        
        if(!allGrades.isEmpty()) {
            double average = allGrades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double max = allGrades.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            double min = allGrades.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            
            System.out.println("\nCOURSE STATISTICS:");
            System.out.printf("Average: %.2f%% | Highest: %.2f%% | Lowest: %.2f%%%n", average, max, min);
        }
    }
    
    public void showGradePredictor(String studentId, String courseCode) {
        if(!students.containsKey(studentId) || !courses.containsKey(courseCode)) {
            System.out.println("ERROR: Student or course not found!");
            return;
        }
        
        Student student = students.get(studentId);
        Course course = courses.get(courseCode);
        double currentGrade = student.getCourseGrade(courseCode, course.getWeights());
        
        System.out.println("\nGRADE PREDICTOR");
        System.out.printf("Current Grade: %.2f%% (%s)%n", currentGrade, student.getGradeLetter(currentGrade));
        
        System.out.print("Enter final exam weight (as decimal, e.g., 0.3 for 30%): ");
        Scanner sc = new Scanner(System.in);
        double finalWeight = sc.nextDouble();
        
        System.out.print("Enter desired final course grade: ");
        double desiredGrade = sc.nextDouble();
        
        double neededGrade = (desiredGrade - (currentGrade * (1 - finalWeight))) / finalWeight;
        
        System.out.printf("\nTo get %.1f%% in the course, you need: %.1f%% on the final%n", 
            desiredGrade, neededGrade);
        
        if(neededGrade > 100) {
            System.out.println("WARNING: This target may not be achievable!");
        } else if(neededGrade < 0) {
            System.out.println("SUCCESS: You've already achieved this grade!");
        }
    }
    
    // Database viewing methods
    public void displayAllCourses() {
        System.out.println("\n=== ALL COURSES IN DATABASE ===");
        
        if (courses.isEmpty()) {
            System.out.println("No courses found in database.");
            return;
        }
        
        System.out.printf("%-12s %-25s %-8s %-15s%n", 
            "Course Code", "Course Name", "Credits", "Enrolled Students");
        System.out.println("-".repeat(65));
        
        for (Course course : courses.values()) {
            int enrolledCount = courseEnrollments.getOrDefault(course.getCode(), new ArrayList<>()).size();
            System.out.printf("%-12s %-25s %-8d %-15d%n",
                course.getCode(), course.getName(), course.getCredits(), enrolledCount);
        }
        
        System.out.print("\nShow detailed course information? (y/n): ");
        Scanner sc = new Scanner(System.in);
        if (sc.nextLine().equalsIgnoreCase("y")) {
            for (Course course : courses.values()) {
                displayCourseDetails(course);
            }
        }
    }
    
    public void displayAllStudents() {
        System.out.println("\n=== ALL STUDENTS IN DATABASE ===");
        
        if (students.isEmpty()) {
            System.out.println("No students found in database.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-15s %-15s%n", 
            "SR Code", "Name", "Major", "Enrolled Courses");
        System.out.println("-".repeat(65));
        
        for (Student student : students.values()) {
            int courseCount = student.getEnrolledCourses().size();
            System.out.printf("%-10s %-20s %-15s %-15d%n",
                student.getSrCode(), student.getName(), student.getMajor(), courseCount);
        }
        
        System.out.print("\nShow detailed student information? (y/n): ");
        Scanner sc = new Scanner(System.in);
        if (sc.nextLine().equalsIgnoreCase("y")) {
            for (Student student : students.values()) {
                displayStudentSummary(student);
            }
        }
    }
    
    public void displayDatabaseOverview() {
        System.out.println("\n=== DATABASE OVERVIEW ===");
        System.out.println("Statistics:");
        System.out.printf("  Total Students: %d%n", students.size());
        System.out.printf("  Total Courses: %d%n", courses.size());
        
        int totalEnrollments = courseEnrollments.values().stream()
            .mapToInt(List::size).sum();
        System.out.printf("  Total Course Enrollments: %d%n", totalEnrollments);
        
        double avgCourses = students.isEmpty() ? 0 : (double) totalEnrollments / students.size();
        System.out.printf("  Average Courses per Student: %.1f%n", avgCourses);
        
        System.out.println("\nMost Popular Courses:");
        if (!courseEnrollments.isEmpty()) {
            courseEnrollments.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .limit(3)
                .forEach(entry -> {
                    Course course = courses.get(entry.getKey());
                    if (course != null) {
                        System.out.printf("  %s - %s (%d students)%n",
                            course.getCode(), course.getName(), entry.getValue().size());
                    }
                });
        }
    }
    
    private void displayCourseDetails(Course course) {
        System.out.println("\n" + course.getCode() + " - " + course.getName());
        System.out.println("  Credits: " + course.getCredits());
        System.out.println("  Enrollment: " + 
            courseEnrollments.getOrDefault(course.getCode(), new ArrayList<>()).size() + " students");
        
        System.out.println("  Grading Weights:");
        for (Map.Entry<String, Double> weight : course.getWeights().entrySet()) {
            System.out.printf("    %s: %.0f%%%n", weight.getKey(), weight.getValue() * 100);
        }
        
        List<String> enrolledStudents = courseEnrollments.get(course.getCode());
        if (enrolledStudents != null && !enrolledStudents.isEmpty()) {
            System.out.println("  Enrolled Students:");
            for (String studentId : enrolledStudents) {
                Student student = students.get(studentId);
                if (student != null) {
                    System.out.printf("    %s - %s%n", student.getSrCode(), student.getName());
                }
            }
        }
    }
    
    private void displayStudentSummary(Student student) {
        System.out.println("\n" + student.getSrCode() + " - " + student.getName());
        System.out.println("  Major: " + student.getMajor());
        System.out.println("  Enrolled in " + student.getEnrolledCourses().size() + " courses:");
        
        for (String courseCode : student.getEnrolledCourses()) {
            Course course = courses.get(courseCode);
            if (course != null) {
                double grade = student.getCourseGrade(courseCode, course.getWeights());
                String letterGrade = student.getGradeLetter(grade);
                System.out.printf("    %s: %.2f%% (%s)%n", 
                    courseCode, grade, letterGrade);
            }
        }
    }
    
    public void searchDatabase() {
        System.out.println("\n=== SEARCH DATABASE ===");
        System.out.println("1. Search Courses");
        System.out.println("2. Search Students");
        System.out.print("Choose: ");
        
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.nextLine();
        
        System.out.print("Enter search term: ");
        String searchTerm = sc.nextLine().toLowerCase();
        
        switch (choice) {
            case 1: searchCourses(searchTerm); break;
            case 2: searchStudents(searchTerm); break;
            default: System.out.println("ERROR: Invalid choice!");
        }
    }
    
    private void searchCourses(String searchTerm) {
        System.out.println("\nSEARCH RESULTS FOR COURSES:");
        boolean found = false;
        
        for (Course course : courses.values()) {
            if (course.getCode().toLowerCase().contains(searchTerm) || 
                course.getName().toLowerCase().contains(searchTerm)) {
                System.out.printf("  %s - %s (%d credits)%n",
                    course.getCode(), course.getName(), course.getCredits());
                found = true;
            }
        }
        
        if (!found) System.out.println("No courses found matching: " + searchTerm);
    }
    
    private void searchStudents(String searchTerm) {
        System.out.println("\nSEARCH RESULTS FOR STUDENTS:");
        boolean found = false;
        
        for (Student student : students.values()) {
            if (student.getSrCode().toLowerCase().contains(searchTerm) || 
                student.getName().toLowerCase().contains(searchTerm) ||
                student.getMajor().toLowerCase().contains(searchTerm)) {
                System.out.printf("  %s - %s (%s) - %d courses%n",
                    student.getSrCode(), student.getName(), student.getMajor(),
                    student.getEnrolledCourses().size());
                found = true;
            }
        }
        
        if (!found) System.out.println("No students found matching: " + searchTerm);
    }
    
    // Save and load methods
    public boolean saveAllData() {
        String baseDir = "data";
        createDirectoryIfNotExists(baseDir);
        
        try {
            saveStudentsToFile(baseDir + "/students.dat");
            saveCoursesToFile(baseDir + "/courses.dat");
            saveEnrollmentsToFile(baseDir + "/enrollments.csv");
            saveGradesToCSV(baseDir + "/grades.csv");
            saveCoursesToCSV(baseDir + "/courses.csv");
            return true;
        } catch (IOException e) {
            System.out.println("ERROR saving data: " + e.getMessage());
            return false;
        }
    }
    
    public boolean loadAllData() {
        String baseDir = "data";
        
        try {
            if (Files.exists(Paths.get(baseDir + "/students.dat"))) {
                loadStudentsFromFile(baseDir + "/students.dat");
                loadCoursesFromFile(baseDir + "/courses.dat");
                loadEnrollmentsFromFile(baseDir + "/enrollments.csv");
                return true;
            } else {
                System.out.println("No existing data found. Starting fresh.");
                return false;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERROR loading data: " + e.getMessage());
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadStudentsFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            students = (Map<String, Student>) ois.readObject();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadCoursesFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            courses = (Map<String, Course>) ois.readObject();
        }
    }
    
    private void loadEnrollmentsFromFile(String filename) throws IOException {
        courseEnrollments = new HashMap<>();
        if (!Files.exists(Paths.get(filename))) return;
        
        List<String> lines = Files.readAllLines(Paths.get(filename));
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 2) {
                String studentId = parts[0], courseCode = parts[1];
                courseEnrollments.putIfAbsent(courseCode, new ArrayList<>());
                if (!courseEnrollments.get(courseCode).contains(studentId)) {
                    courseEnrollments.get(courseCode).add(studentId);
                }
                if (students.containsKey(studentId)) {
                    students.get(studentId).enrollInCourse(courseCode);
                }
            }
        }
    }
    
    private void saveStudentsToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(students);
        }
    }
    
    private void saveCoursesToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(courses);
        }
    }
    
    private void saveEnrollmentsToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("StudentID,CourseCode");
            for (String courseCode : courseEnrollments.keySet()) {
                for (String studentId : courseEnrollments.get(courseCode)) {
                    writer.println(studentId + "," + courseCode);
                }
            }
        }
    }
    
    private void saveGradesToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("StudentID,CourseCode,AssignmentType,AssignmentName,Grade");
            for (Student student : students.values()) {
                for (String line : student.getGradesCSV()) {
                    writer.println(line);
                }
            }
        }
    }
    
    private void saveCoursesToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("CourseCode,CourseName,Credits,Weights");
            for (Course course : courses.values()) {
                writer.println(course.toCSV());
            }
        }
    }
    
    private void createDirectoryIfNotExists(String dirName) {
        File directory = new File(dirName);
        if (!directory.exists()) directory.mkdirs();
    }
    
    public void exportStudentReport(String studentId, String filename) {
        if (!students.containsKey(studentId)) {
            System.out.println("ERROR: Student not found!");
            return;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            Student student = students.get(studentId);
            writer.println("STUDENT REPORT");
            writer.println("==============");
            writer.println("Name: " + student.getName());
            writer.println("SR Code: " + student.getSrCode());
            writer.println("Major: " + student.getMajor());
            writer.println();
            writer.println("COURSES AND GRADES:");
            writer.println("===================");
            
            for (String courseCode : student.getEnrolledCourses()) {
                Course course = courses.get(courseCode);
                if (course != null) {
                    double grade = student.getCourseGrade(courseCode, course.getWeights());
                    String letterGrade = student.getGradeLetter(grade);
                    writer.printf("%s - %s: %.2f%% (%s)%n", 
                        courseCode, course.getName(), grade, letterGrade);
                    
                    Map<String, List<Double>> assignments = student.getCourseGrades().get(courseCode);
                    for (String assignmentType : assignments.keySet()) {
                        List<Double> grades = assignments.get(assignmentType);
                        double avg = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                        writer.printf("  %s: %.2f%% (%d assignments)%n", 
                            assignmentType, avg, grades.size());
                    }
                    writer.println();
                }
            }
            System.out.println("SUCCESS: Report exported to: " + filename);
        } catch (IOException e) {
            System.out.println("ERROR exporting report: " + e.getMessage());
        }
    }
}