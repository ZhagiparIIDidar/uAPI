package patterns;

import users.Student;
import users.Employee;
import staff.Teacher;
import staff.Manager;
import staff.Admin;
import academic.Course;
import research.Researcher;
import research.ResearchProject;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStorage Singleton class implementing the Singleton design pattern.
 * Centralized storage for all entities in the university management system.
 * Ensures only one instance exists throughout the application.
 */
public class DataStorage {

    private static DataStorage instance;

    // Все данные хранятся здесь
    private List<Student> students = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Manager> managers = new ArrayList<>();
    private List<Admin> admins = new ArrayList<>();
    private List<Researcher> researchers = new ArrayList<>();
    private List<ResearchProject> projects = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private DataStorage() {}

    /**
     * Get the singleton instance of DataStorage.
     *
     * @return the single instance of DataStorage
     */
    public static DataStorage getInstance() {
        if (instance == null)
            instance = new DataStorage();
        return instance;
    }

    // ===== Students =====
    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id)
                return student;
        }
        return null;
    }

    // ===== Employees =====
    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public Employee findEmployeeById(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id)
                return employee;
        }
        return null;
    }

    // ===== Teachers =====
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
    }

    public Teacher findTeacherById(int id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id)
                return teacher;
        }
        return null;
    }

    // ===== Courses =====
    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public Course findCourseById(int id) {
        for (Course course : courses) {
            if (course.getId() == id)
                return course;
        }
        return null;
    }

    // ===== Managers =====
    public List<Manager> getManagers() {
        return managers;
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    public void removeManager(Manager manager) {
        managers.remove(manager);
    }

    public Manager findManagerById(int id) {
        for (Manager manager : managers) {
            if (manager.getId() == id)
                return manager;
        }
        return null;
    }

    // ===== Admins =====
    public List<Admin> getAdmins() {
        return admins;
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    public void removeAdmin(Admin admin) {
        admins.remove(admin);
    }

    public Admin findAdminById(int id) {
        for (Admin admin : admins) {
            if (admin.getId() == id)
                return admin;
        }
        return null;
    }

    // ===== Researchers =====
    public List<Researcher> getResearchers() {
        return researchers;
    }

    public void addResearcher(Researcher researcher) {
        researchers.add(researcher);
    }

    public void removeResearcher(Researcher researcher) {
        researchers.remove(researcher);
    }

    public Researcher findResearcherById(int id) {
        for (Researcher researcher : researchers) {
            if (researcher.getId() == id)
                return researcher;
        }
        return null;
    }

    // ===== Research Projects =====
    public List<ResearchProject> getProjects() {
        return projects;
    }

    public void addProject(ResearchProject project) {
        projects.add(project);
    }

    public void removeProject(ResearchProject project) {
        projects.remove(project);
    }

    public ResearchProject findProjectById(int id) {
        for (ResearchProject project : projects) {
            if (project.getId() == id)
                return project;
        }
        return null;
    }

    // ===== Utility Methods =====
    /**
     * Clear all data from storage.
     */
    public void clearAllData() {
        students.clear();
        employees.clear();
        teachers.clear();
        courses.clear();
        managers.clear();
        admins.clear();
        researchers.clear();
        projects.clear();
    }

    /**
     * Get total number of all entities.
     *
     * @return total count
     */
    public int getTotalCount() {
        return students.size() + employees.size() + teachers.size() +
               courses.size() + managers.size() + admins.size() +
               researchers.size() + projects.size();
    }
}
