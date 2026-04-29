import java.io.*;
import java.util.*;
package academic;
/**
 * Represents a student's mark in a course.
 * Total = firstAttestation (30%) + secondAttestation (30%) + finalExam (40%)
 */
public class Mark {

    private double firstAttestation;   
    private double secondAttestation;  
    private double finalExam;          
    private double totalScore;

   
    private Course course;
    private Student student;

    public Mark() {}

    public Mark(Student student, Course course,
                double firstAttestation, double secondAttestation, double finalExam) {
        this.student = student;
        this.course = course;
        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
        this.totalScore = calculateTotal();
    }

    
    public double getFirstAttestation()              { return firstAttestation; }
    public void setFirstAttestation(double v)        { this.firstAttestation = v; }
    public double getSecondAttestation()             { return secondAttestation; }
    public void setSecondAttestation(double v)       { this.secondAttestation = v; }
    public double getFinalExam()                     { return finalExam; }
    public void setFinalExam(double v)               { this.finalExam = v; }
    public double getTotalScore()                    { return totalScore; }
    public Course getCourse()                        { return course; }
    public void setCourse(Course course)             { this.course = course; }
    public Student getStudent()                      { return student; }

    
    
    public double calculateTotal() {
        totalScore = firstAttestation + secondAttestation + finalExam;
        return totalScore;
    }

    
    public String getLetterGrade() {
        double t = calculateTotal();
        if (t >= 90) return "A";
        else if (t >= 80) return "B";
        else if (t >= 70) return "C";
        else if (t >= 60) return "D";
        else return "F";
    }

    @Override
    public String toString() {
        return String.format("Mark{course=%s, att1=%.1f, att2=%.1f, final=%.1f, total=%.1f (%s)}",
                course != null ? course.getName() : "N/A",
                firstAttestation, secondAttestation, finalExam,
                calculateTotal(), getLetterGrade());
    }
}
