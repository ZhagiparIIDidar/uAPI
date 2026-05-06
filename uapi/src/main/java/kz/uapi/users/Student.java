package kz.uapi.users;

import kz.uapi.abs_class.User;

public class Student extends User {

    private double gpa;
    private int year;
    private String major;
    private int credits;
    private boolean isResearcher;
    private String supervisorLogin;

    public Student(String login, String password, String firstName, String lastName,
            java.time.LocalDateTime createdAt, double gpa, int year,
            String major, int credits, boolean isResearcher, String supervisorLogin) {
        super(login, password, "STUDENT", firstName, lastName, createdAt);
        this.gpa = gpa;
        this.year = year;
        this.major = major;
        this.credits = credits;
        this.isResearcher = isResearcher;
        this.supervisorLogin = supervisorLogin;
    }

    public double getGpa() {
        return gpa;
    }

    public int getYear() {
        return year;
    }

    public String getMajor() {
        return major;
    }

    public int getCredits() {
        return credits;
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    public String getSupervisorLogin() {
        return supervisorLogin;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setResearcher(boolean isResearcher) {
        this.isResearcher = isResearcher;
    }

    public void setSupervisorLogin(String supervisorLogin) {
        this.supervisorLogin = supervisorLogin;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " | GPA: " + gpa + " | Year: " + year + " | Major: " + major;
    }

}