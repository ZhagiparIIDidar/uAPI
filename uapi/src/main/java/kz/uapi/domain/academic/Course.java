package kz.uapi.domain.academic;

/**
 * Класс, представляющий учебный курс в системе.
 * Содержит информацию о названии, описании, количестве кредитов, преподавателе
 * и факультете.
 * 
 * <p>
 * Каждый курс имеет максимальное количество студентов и связан с одним
 * преподавателем.
 * </p>
 * 
 * @author UAPI System
 * @version 1.0
 * @since 1.0
 */
public class Course {

    private int courseId;
    private String name;
    private String description;
    private int credits;
    private String teacherLogin;
    private int maxStudents;
    private int facultyId;

    public Course(int courseId, String name, String description, int credits,
            String teacherLogin, int maxStudents, int facultyId) {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.teacherLogin = teacherLogin;
        this.maxStudents = maxStudents;
        this.facultyId = facultyId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCredits() {
        return credits;
    }

    public String getTeacherLogin() {
        return teacherLogin;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setTeacherLogin(String teacherLogin) {
        this.teacherLogin = teacherLogin;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    @Override
    public String toString() {
        return courseId + " | " + name + " | Credits: " + credits + " | Teacher: " + teacherLogin;
    }
}
