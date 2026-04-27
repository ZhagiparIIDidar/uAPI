package patterns;

import users.User;
import users.Student;
import users.Employee;
import staff.Teacher;
import staff.Manager;
import staff.Admin;
import research.Researcher;

/**
 * Factory interface for creating different types of users.
 * Implements the Factory design pattern to create user objects.
 */
public interface UserFactory {
    /**
     * Create a student user.
     *
     * @param name the student's name
     * @param email the student's email
     * @return a new Student object
     */
    Student createStudent(String name, String email);

    /**
     * Create an employee user.
     *
     * @param name the employee's name
     * @param email the employee's email
     * @return a new Employee object
     */
    Employee createEmployee(String name, String email);

    /**
     * Create a teacher user.
     *
     * @param name the teacher's name
     * @param email the teacher's email
     * @return a new Teacher object
     */
    Teacher createTeacher(String name, String email);

    /**
     * Create a manager user.
     *
     * @param name the manager's name
     * @param email the manager's email
     * @return a new Manager object
     */
    Manager createManager(String name, String email);

    /**
     * Create an admin user.
     *
     * @param name the admin's name
     * @param email the admin's email
     * @return a new Admin object
     */
    Admin createAdmin(String name, String email);

    /**
     * Create a researcher user.
     *
     * @param name the researcher's name
     * @param email the researcher's email
     * @return a new Researcher object
     */
    Researcher createResearcher(String name, String email);
}
