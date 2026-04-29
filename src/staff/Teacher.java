package staff;

import java.util.*;
import enums.TeacherTitle;
import users.User;

/**
 * import users.*;
 * 
 * Represents a teacher in the university system.
 */
public class Teacher extends User {

    private TeacherTitle title;
    private String department;
    private double salary;
    private double rating;
    private int ratingsCount;
    private List<String> courses;

    // ── Constructors ──────────────────────────────────────────

    public Teacher() {
        super();
        this.courses = new ArrayList<>();
    }

    public Teacher(int id, String name, String email, String password,
            TeacherTitle title, String department) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.title = title;
        this.department = department;
    }

    // ── Getters ───────────────────────────────────────────────

    public TeacherTitle getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public List<String> getCourses() {
        return courses;
    }

    // ── Setters ───────────────────────────────────────────────

    public void setTitle(TeacherTitle title) {
        this.title = title;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // ── Methods ───────────────────────────────────────────────

    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }
        this.rating = (this.rating * ratingsCount + rating) / (ratingsCount + 1);
        ratingsCount++;
    }

    public void addCourse(String course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
        }
    }
}