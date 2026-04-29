import users.*;
import staff.*;

import java.util.Scanner;

public class AuthService {

    private static AuthService instance;
    private DataBase db = DataBase.getInstance();
    private Scanner scanner = new Scanner(System.in);
    private User currentUser; // текущий пользователь

    private AuthService() {
    }

    public static AuthService getInstance() {
        if (instance == null)
            instance = new AuthService();
        return instance;
    }

    // ── Sign In ───────────────────────────────────────────────

    public User signIn() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Проверяем всех пользователей в базе
        for (Student s : db.getStudents())
            if (s.getEmail().equals(email) && s.getPassword().equals(password)) {
                currentUser = s;
                return s;
            }

        for (Teacher t : db.getTeachers())
            if (t.getEmail().equals(email) && t.getPassword().equals(password)) {
                currentUser = t;
                return t;
            }

        for (Admin a : db.getAdmins())
            if (a.getEmail().equals(email) && a.getPassword().equals(password)) {
                currentUser = a;
                return a;
            }

        for (Manager m : db.getManagers())
            if (m.getEmail().equals(email) && m.getPassword().equals(password)) {
                currentUser = m;
                return m;
            }

        System.out.println("User not found!");
        return null;
    }

    // ── Register (только Student) ─────────────────────────────

    public Student register() {
        System.out.println("=== Registration ===");

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        // Проверяем что email не занят
        if (emailExists(email)) {
            System.out.println("Email already exists!");
            return null;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Major: ");
        String major = scanner.nextLine().trim();/////////////////////////////

        System.out.print("Year (1-4): ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        // Создаём нового студента
        int id = generateId();
        Student student = new Student(id, name, email, password, year, major);
        // добаление в базу данных
        db.getStudents().add(student);

        System.out.println("Registration successful! Welcome, " + name + "!");
        return student;
    }

    public void signOut() {
        currentUser = null;
    }
    // ── Helpers ───────────────────────────────────────────────

    private boolean emailExists(String email) {
        for (Student s : db.getStudents())
            if (s.getEmail().equals(email))
                return true;
        for (Teacher t : db.getTeachers())
            if (t.getEmail().equals(email))
                return true;
        for (Admin a : db.getAdmins())
            if (a.getEmail().equals(email))
                return true;
        for (Manager m : db.getManagers())
            if (m.getEmail().equals(email))
                return true;
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private int generateId() {
        return db.getTotalCount() + 1;
    }
}