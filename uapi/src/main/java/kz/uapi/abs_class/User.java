package kz.uapi.abs_class;

import java.time.LocalDateTime;

public abstract class User {

    private String login;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;

    public User(String login, String password, String role,
            String firstName, String lastName, LocalDateTime createdAt) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + role + ")";
    }
}