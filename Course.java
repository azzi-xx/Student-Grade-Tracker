import java.util.*;
import java.io.*;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String code;
    private String name;
    private int credits;
    private Map<String, Double> assignmentWeights;
    
    public Course(String code, String name, int credits, int weightScheme) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.assignmentWeights = new HashMap<>();
        
        switch(weightScheme) {
            case 1:
                assignmentWeights.put("Homework", 0.20);
                assignmentWeights.put("Quizzes", 0.30);
                assignmentWeights.put("Exams", 0.50);
                break;
            case 2:
                assignmentWeights.put("Projects", 0.40);
                assignmentWeights.put("Exams", 0.60);
                break;
            case 3:
                break;
            default:
                assignmentWeights.put("Assignments", 1.0);
        }
    }
    
    public List<String> getAssignmentTypes() {
        return new ArrayList<>(assignmentWeights.keySet());
    }
    
    public Map<String, Double> getWeights() {
        return assignmentWeights;
    }
    
    public String toCSV() {
        StringBuilder weights = new StringBuilder();
        for (Map.Entry<String, Double> entry : assignmentWeights.entrySet()) {
            if (weights.length() > 0) weights.append(";");
            weights.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return String.format("%s,%s,%d,%s", code, name, credits, weights.toString());
    }
    
    // Getters
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCredits() { return credits; }
}