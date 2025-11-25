import java.util.*;

public class Student {
    private String srCode;
    private String name;
    private String major;
    private Map<String, Map<String, List<Double>>> courseGrades;
    private Map<String, List<String>> assignmentNames;
    
    public Student(String srCode, String name, String major){
        this.srCode = srCode;
        this.name = name;
        this.major = major;
        this.courseGrades = new HashMap<>();
        this.assignmentNames = new HashMap<>();
    }
    
    public void enrollInCourse(String courseCode){
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
    
    public double getCourseGrade(String courseCode, Map<String, Double> weights){
        if(!courseGrades.containsKey(courseCode)){
            return 0.0;
        }
        double totalWeightedGrade = 0.0;
        double totalWeight = 0.0;
        
        for(String assignmentType : weights.keySet()){
            if(courseGrades.get(courseCode).containsKey(assignmentType)) {
                List<Double> grades = courseGrades.get(courseCode).get(assignmentType);
                if(!grades.isEmpty()){
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
    
    public String getSrCode(){
        return srCode; 
    }
    public String getName(){
        return name; 
    }
    public String getMajor(){
        return major; 
    }
    public Map<String, Map<String, List<Double>>> getCourseGrades(){
        return courseGrades;
    }
    public List<String> getEnrolledCourses(){
        return new ArrayList<>(courseGrades.keySet());
    }
}