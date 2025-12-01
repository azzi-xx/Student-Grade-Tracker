import java.util.*;
import java.io.*;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String srCode;
    private String name;
    private String major;
    private Map<String, Map<String, List<Double>>> courseGrades;
    private Map<String, List<String>> assignmentNames;
    
    public Student(String srCode, String name, String major) {
        this.srCode = srCode;
        this.name = name;
        this.major = major;
        this.courseGrades = new HashMap<>();
        this.assignmentNames = new HashMap<>();
    }
    
    public void enrollInCourse(String courseCode) {
        courseGrades.putIfAbsent(courseCode, new HashMap<>());
        assignmentNames.putIfAbsent(courseCode, new ArrayList<>());
    }
    
    public boolean addGrade(String courseCode, String assignmentType, String assignmentName, double grade) {
        if(!courseGrades.containsKey(courseCode)) {
            return false;
        }
        
        courseGrades.get(courseCode).putIfAbsent(assignmentType, new ArrayList<>());
        courseGrades.get(courseCode).get(assignmentType).add(grade);
        
        assignmentNames.get(courseCode).add(assignmentName);
        return true;
    }
    
    public double getCourseGrade(String courseCode, Map<String, Double> weights) {
        if(!courseGrades.containsKey(courseCode)) return 0.0;
        
        double totalWeightedGrade = 0.0;
        double totalWeight = 0.0;
        
        for(String assignmentType : weights.keySet()) {
            if(courseGrades.get(courseCode).containsKey(assignmentType)) {
                List<Double> grades = courseGrades.get(courseCode).get(assignmentType);
                if(!grades.isEmpty()) {
                    double average = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    totalWeightedGrade += average * weights.get(assignmentType);
                    totalWeight += weights.get(assignmentType);
                }
            }
        }
        
        return totalWeight > 0 ? totalWeightedGrade / totalWeight : 0.0;
    }
    
    public String getGradeLetter(double grade) {
        if(grade >= 90) return "A";
        else if(grade >= 80) return "B";
        else if(grade >= 70) return "C";
        else if(grade >= 60) return "D";
        else return "F";
    }
    
    // CSV export methods
    public String toCSV() {
        return String.format("%s,%s,%s", srCode, name, major);
    }
    
    public List<String> getGradesCSV() {
        List<String> lines = new ArrayList<>();
        for (String courseCode : courseGrades.keySet()) {
            Map<String, List<Double>> assignments = courseGrades.get(courseCode);
            List<String> names = assignmentNames.get(courseCode);
            
            int assignmentIndex = 0;
            for (String assignmentType : assignments.keySet()) {
                List<Double> grades = assignments.get(assignmentType);
                for (int i = 0; i < grades.size(); i++) {
                    String assignmentName = (assignmentIndex < names.size()) ? names.get(assignmentIndex) : "Unknown";
                    lines.add(String.format("%s,%s,%s,%s,%.2f", 
                        srCode, courseCode, assignmentType, assignmentName, grades.get(i)));
                    assignmentIndex++;
                }
            }
        }
        return lines;
    }
    
    // Getters
    public String getSrCode() { return srCode; }
    public String getName() { return name; }
    public String getMajor() { return major; }
    public Map<String, Map<String, List<Double>>> getCourseGrades() { return courseGrades; }
    public List<String> getEnrolledCourses() { return new ArrayList<>(courseGrades.keySet()); }
}