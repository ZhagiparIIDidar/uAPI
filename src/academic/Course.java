import java.io.*;
import java.util.*;

public class Course {

    private int courseId;
    private String name;
    private int credits;
    private String major;
    private int year;

    private Teacher teacher;
    private List<Student> students = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();

    /**
     * Default constructor
     */
    public Course() {}

    public Course(int courseId, String name, int credits, String major, int year) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
        this.major = major;
        this.year = year;
    }



    public int getCourseId()          { return courseId; }
    public String getName()           { return name; }
    public int getCredits()           { return credits; }
    public String getMajor()          { return major; }
    public int getYear()              { return year; }
    public Teacher getTeacher()       { return teacher; }
    public List<Student> getStudents(){ return students; }
    public List<Lesson> getLessonsList(){ return lessons; }



    /**
     * Adds a student to this course.
     */
    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
            System.out.println(student.getName() + " added to " + name);
        }
    }

    /**
     * Removes a student from this course.
     */
    public void removeStudent(Student student) {
        if (students.remove(student))
            System.out.println(student.getName() + " removed from " + name);
        else
            System.out.println("Student not found in " + name);
    }

    /**
     * Assigns a teacher to this course.
     */
    public void addTeacher(Teacher t) {
        this.teacher = t;
        System.out.println("Teacher " + t.getName() + " assigned to " + name);
    }

    /**
     * Prints all lessons of this course.
     */
    public void getLessons() {
        System.out.println("=== Lessons for " + name + " ===");
        if (lessons.isEmpty()) { System.out.println("No lessons yet."); return; }
        for (Lesson l : lessons)
            System.out.println("  [" + l.getType() + "] " + l.getTopic() + " | " + l.getDate());
    }

    /** Adds a lesson (called by Teacher or Admin) */
    public void addLesson(Lesson lesson) {
        if (lesson != null) lessons.add(lesson);
    }

    @Override
    public String toString() {
        return "Course{id=" + courseId + ", name=" + name +
               ", credits=" + credits + ", students=" + students.size() + "}";
    }
}
