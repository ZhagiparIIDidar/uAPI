package staff;

import enums.ManagerType;
import users.*;

/**
 * Represents a manager in the university system.
 */
public class Manager extends User {

    private ManagerType managerType;
    private String department;
    private double salary;
    private String officeLocation;

    // ── Constructors ──────────────────────────────────────────

    public Manager() {
        super();
    }

    public Manager(int id, String name, String email, String password,
            ManagerType managerType, String department) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.managerType = managerType;
        this.department = department;
    }

    // ── Getters ───────────────────────────────────────────────

    public ManagerType getManagerType() {
        return managerType;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    // ── Setters ───────────────────────────────────────────────

    public void setManagerType(ManagerType managerType) {
        this.managerType = managerType;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }
}