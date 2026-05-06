package kz.uapi.users.Employee;

import java.time.LocalDateTime;

import kz.uapi.abs_class.Employee;

public class Admin extends Employee {

    private int accessLevel;

    public Admin(String login, String password, String firstName, String lastName,
            LocalDateTime createdAt, String department, double salary,
            int accessLevel) {
        super(login, password, "ADMIN", firstName, lastName, createdAt, department, salary);
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " | Admin | Access Level: " + accessLevel;
    }
}
