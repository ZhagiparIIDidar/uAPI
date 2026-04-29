package staff;

import users.*;

/**
 * Represents an administrator in the university system.
 */
public class Admin extends User {

    private String position;
    private String department;
    private double salary;
    private String hireDate;

    // ── Constructors ──────────────────────────────────────────

    public Admin() {
        super();
    }

    public Admin(int id, String name, String email, String password,
            String position, String department) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.position = position;
        this.department = department;
    }

    // ── Getters ───────────────────────────────────────────────

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String getHireDate() {
        return hireDate;
    }

    // ── Setters ───────────────────────────────────────────────

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
}