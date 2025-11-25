import java.util.*;

public class GradeTracker {
    private Map<String, Student> students;
    private Map<String, Course> courses;
    private Map<String, List<String>> courseEnrollments;
    
    public GradeTracker(){
        this.students = new HashMap<>();
        this.courses = new HashMap<>();
        this.courseEnrollments = new HashMap<>();
    }
    
    public boolean addStudent(String srCode, String name, String major) {
        if(students.containsKey(srCode)){
            return false;
        }
        students.put(srCode, new Student(srCode, name, major));
        return true;
    }
    
    public void addCourse(String code, String name, int credits, int weightScheme){
        courses.put(code, new Course(code, name, credits, weightScheme));
        courseEnrollments.putIfAbsent(code, new ArrayList<>());
    }
    
    public boolean enrollStudent(String studentId, String courseCode){
        if(!students.containsKey(studentId) || !courses.containsKey(courseCode)){
            return false;
        }
        
        students.get(studentId).enrollInCourse(courseCode);
        if(!courseEnrollments.get(courseCode).contains(studentId)){
            courseEnrollments.get(courseCode).add(studentId);
        }
        return true;
    }
    
    public boolean addGrade(String studentId, String courseCode, String assignmentType, 
                           String assignmentName, double grade){
        if(!students.containsKey(studentId) || !courses.containsKey(courseCode)){
            return false;
        }
        
        // Validate grade range
        if(grade < 0 || grade > 100){
            System.out.println("Grade must be between 0 and 100!");
            return false;
        }
        
        return students.get(studentId).addGrade(courseCode, assignmentType, assignmentName, grade);
    }
    
    public List<String> getAssignmentTypes(String courseCode){
        if(courses.containsKey(courseCode)) {
            return courses.get(courseCode).getAssignmentTypes();
        }
        return new ArrayList<>();
    }
    
    public void displayStudentReport(String studentId) {
        if(!students.containsKey(studentId)) {
            System.out.println("Student not found!");
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
        
        for(String courseCode : enrolledCourses){
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
            System.out.println("Course not found!");
            return;
        }
        
        Course course = courses.get(courseCode);
        System.out.println("\n📈 COURSE REPORT: " + course.getCode() + " - " + course.getName());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Enrolled Students: " + courseEnrollments.get(courseCode).size());
        System.out.println("=" .repeat(50));
        
        List<Double> allGrades = new ArrayList<>();
        for(String studentId : courseEnrollments.get(courseCode)){
            Student student = students.get(studentId);
            double grade = student.getCourseGrade(courseCode, course.getWeights());
            allGrades.add(grade);
            
            System.out.printf("%s - %s: %.2f%% (%s)%n", 
                student.getSrCode(), student.getName(), grade, student.getGradeLetter(grade));
        }

        if(!allGrades.isEmpty()){
            double average = allGrades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double max = allGrades.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            double min = allGrades.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            
            System.out.println("\n📊 COURSE STATISTICS:");
            System.out.printf("Average: %.2f%% | Highest: %.2f%% | Lowest: %.2f%%%n", average, max, min);
        }
    }
    
    public void showGradePredictor(String studentId, String courseCode) {
        if(!students.containsKey(studentId) || !courses.containsKey(courseCode)) {
            System.out.println("Student or course not found!");
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
        
        if(neededGrade > 100){
            System.out.println("This target may not be achievable!");
        } else if(neededGrade < 0){
            System.out.println("You've already achieved this grade!");
        }
    }
}