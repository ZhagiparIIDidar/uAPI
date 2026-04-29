import users.Student;
import users.User;
import staff.Teacher;
import staff.Manager;
import staff.Admin;
import staff.SuperAdmin;
import academic.Course;
import research.Researcher;
import research.ResearchProject;
import exceptions.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStorage Singleton class implementing the Singleton design pattern.
 * Centralized storage for all entities in the university management system.
 * Ensures only one instance exists throughout the application.
 */
public class DataBase {

    private static DataBase instance;

    // Супер пользовотель
    private Admin superAdmin = new Admin();

    // Все данные хранятся здесь
    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Manager> managers = new ArrayList<>();
    private List<Admin> admins = new ArrayList<>();
    private List<Researcher> researchers = new ArrayList<>();
    private List<ResearchProject> projects = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private DataBase() {
    }

    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase();
        return instance;
    }

    // ===== Students =====
    public List<Student> getStudents() {
        return students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public List<Researcher> getResearchers() {
        return researchers;
    }

    public List<ResearchProject> getProjects() {
        return projects;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addUser(User user) throws AccessDeniedException {
        User currentUser = AuthService.getInstance().getCurrentUser();

        if (user instanceof Teacher) {
            if (!(currentUser instanceof Manager || currentUser instanceof Admin)) {
                throw new AccessDeniedException("Only Manager or Admin can add Teachers!");
            }
            teachers.add((Teacher) user);

        } else if (user instanceof Admin) {
            if (!(currentUser instanceof Admin)) {
                throw new AccessDeniedException("Only Admin can add Admins!");
            }
            admins.add((Admin) user);

        } else if (user instanceof Manager) {
            if (!(currentUser instanceof Admin)) {
                throw new AccessDeniedException("Only Admin can add Managers!");
            }
            managers.add((Manager) user);

        } else if (user instanceof Student) {
            students.add((Student) user);
        }

        System.out.println(user.getName() + " added successfully!");
    }

    public void removeUser(User user) throws AccessDeniedException {
        User currentUser = AuthService.getInstance().getCurrentUser();

        if (user instanceof Teacher) {
            if (!(currentUser instanceof Manager || currentUser instanceof Admin))
                throw new AccessDeniedException("Only Manager or Admin can remove Teachers!");
            teachers.remove(user);

        } else if (user instanceof Admin) {
            if (!(currentUser instanceof Admin))
                throw new AccessDeniedException("Only Admin can remove Admins!");
            admins.remove(user);

        } else if (user instanceof Manager) {
            if (!(currentUser instanceof Admin))
                throw new AccessDeniedException("Only Admin can remove Managers!");
            managers.remove(user);

        } else if (user instanceof Student) {
            students.remove(user);
        }

        System.out.println(user.getName() + " removed successfully!");
    }

    public User findUserById(int id) {
        for (Student s : students)
            if (s.getId() == id)
                return s;
        for (Teacher t : teachers)
            if (t.getId() == id)
                return t;
        for (Admin a : admins)
            if (a.getId() == id)
                return a;
        for (Manager m : managers)
            if (m.getId() == id)
                return m;
        return null;
    }

    /**
     * Get total number of all entities.
     *
     * @return total count
     */

    public int getTotalCount() {
        return students.size() + teachers.size() +
                courses.size() + managers.size() + admins.size() +
                researchers.size() + projects.size();
    }
}
