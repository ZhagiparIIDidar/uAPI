package kz.uapi.abs_class;

import java.time.LocalDateTime;

public abstract class Employee extends User {

    private String department;
    private double salary;

    public Employee(String login, String password, String role,
            String firstName, String lastName, LocalDateTime createdAt,
            String department, double salary) {
        super(login, password, role, firstName, lastName, createdAt);
        this.department = department;
        this.salary = salary;

    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " | " + department;
    }
}