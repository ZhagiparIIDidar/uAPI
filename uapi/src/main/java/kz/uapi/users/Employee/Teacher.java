package kz.uapi.users.Employee;

import java.time.LocalDateTime;
import java.util.List;

import kz.uapi.interfaces.Researcher;
import kz.uapi.abs_class.Employee;
import kz.uapi.enums.TeacherTitle;

public class Teacher extends Employee implements Researcher {

    private TeacherTitle title;
    private boolean isResearcher;
    private double rating;
    private int hIndex;
    private List<String> specializations;

    public Teacher(String login, String password, String firstName, String lastName,
            LocalDateTime createdAt, String department, double salary,
            TeacherTitle title, double rating, int hIndex, List<String> specializations) {
        super(login, password, "TEACHER", firstName, lastName, createdAt, department, salary);
        this.title = title;
        this.isResearcher = (title == TeacherTitle.PROFESSOR);
        this.rating = rating;
        this.hIndex = hIndex;
        this.specializations = specializations;
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    public double getRating() {
        return rating;
    }

    public int getHIndex() {
        return hIndex;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setTitle(TeacherTitle title) {
        this.title = title;
        this.isResearcher = (title == TeacherTitle.PROFESSOR);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setHIndex(int hIndex) {
        this.hIndex = hIndex;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " | " + title + " | Rating: " + rating;
    }
}
