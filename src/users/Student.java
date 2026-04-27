import java.io.*;
import java.util.*;

/**
 * Represents a student in the university system.
 */
public class Student extends User implements Researcher {

    private double gpa;
    private int year;
    private String major;
    private int credits;

    private List<Course> registeredCourses = new ArrayList<>();
    private List<Mark> marks = new ArrayList<>();
    private List<ResearchPaper> papers = new ArrayList<>();

    /**
     * Default constructor
     */
    public Student() {
        super();
    }

    public Student(int id, String name, String email, String password, int year, String major) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.year = year;
        this.major = major;
    }

    // ── Getters ───────────────────────────────────────────────

    public double getGpa()                        { return gpa; }
    public int getYear()                          { return year; }
    public String getMajor()                      { return major; }
    public int getCredits()                       { return credits; }
    public List<Course> getRegisteredCourses()    { return registeredCourses; }
    public List<Mark> getMarks()                  { return marks; }

    // ── Internal helpers ──────────────────────────────────────

    /** Called by Manager after approval */
    public void addCourse(Course course) {
        if (course != null && !registeredCourses.contains(course))
            registeredCourses.add(course);
    }

    /** Called by AcademicService when mark is set */
    public void addMark(Mark mark) {
        if (mark != null) {
            marks.add(mark);
            recalculateGPA();
        }
    }

    private void recalculateGPA() {
        if (marks.isEmpty()) { gpa = 0.0; return; }
        double totalPoints = 0;
        int totalCredits = 0;
        for (Mark m : marks) {
            totalPoints += toGpaPoints(m.calculateTotal()) * m.getCourse().getCredits();
            totalCredits += m.getCourse().getCredits();
        }
        gpa = totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    private double toGpaPoints(double total) {
        if (total >= 90) return 4.0;
        else if (total >= 85) return 3.7;
        else if (total >= 80) return 3.3;
        else if (total >= 75) return 3.0;
        else if (total >= 70) return 2.7;
        else if (total >= 65) return 2.3;
        else if (total >= 60) return 2.0;
        else if (total >= 50) return 1.0;
        else return 0.0;
    }

    private String letterGrade(double total) {
        if (total >= 90) return "A";
        else if (total >= 80) return "B";
        else if (total >= 70) return "C";
        else if (total >= 60) return "D";
        else return "F";
    }

    // ── Student methods ───────────────────────────────────────

    /**
     * Sends a course registration request (Manager approves it).
     */
    public void registerCourse(Course course) {
        if (course == null) { System.out.println("Course not found."); return; }
        if (registeredCourses.contains(course)) {
            System.out.println("Already registered: " + course.getName());
            return;
        }
        System.out.println("Registration request sent for: " + course.getName());
        // Manager.getInstance().requestRegistration(this, course);
    }

    /**
     * Prints all marks of this student.
     */
    public void viewMarks() {
        if (marks.isEmpty()) { System.out.println("No marks yet."); return; }
        System.out.println("=== Marks for " + name + " ===");
        for (Mark m : marks)
            System.out.printf("  %-20s | Total: %5.1f | %s%n",
                    m.getCourse().getName(), m.calculateTotal(), letterGrade(m.calculateTotal()));
    }

    /**
     * Prints the full academic transcript with GPA.
     */
    public void viewTranscript() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║           ACADEMIC TRANSCRIPT            ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Name  : %-31s║%n", name);
        System.out.printf( "║  ID    : %-31d║%n", id);
        System.out.printf( "║  Major : %-31s║%n", major);
        System.out.printf( "║  Year  : %-31d║%n", year);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  %-22s %4s %6s %4s║%n", "Course", "Cr.", "Total", "Ltr");
        System.out.println("╠══════════════════════════════════════════╣");
        if (marks.isEmpty()) {
            System.out.println("║  No grades recorded.                    ║");
        } else {
            for (Mark m : marks)
                System.out.printf("║  %-22s %4d %6.1f %4s║%n",
                        m.getCourse().getName(),
                        m.getCourse().getCredits(),
                        m.calculateTotal(),
                        letterGrade(m.calculateTotal()));
        }
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  GPA : %-33s║%n", String.format("%.2f / 4.00", gpa));
         System.out.println("╚══════════════════════════════════════════╝");
    }
    }

    
    public void rateTeacher(Teacher teacher, int rating) {
        if (teacher == null) { System.out.println("Teacher not found."); return; }
        if (rating < 1 || rating > 5) { System.out.println("Rating must be 1-5."); return; }
        teacher.addRating(rating);
        System.out.println("Rated " + teacher.getName() + ": " + rating + "/5");
    }


    public void rateTeacher() {
        System.out.println("Call rateTeacher(Teacher, int) with arguments.");
    }

    @Override
    public int getHIndex() {
        if (papers.isEmpty()) return 0;
        List<Integer> cites = new ArrayList<>();
        for (ResearchPaper p : papers) cites.add(p.getCitations());
        Collections.sort(cites, Collections.reverseOrder());
        int h = 0;
        for (int i = 0; i < cites.size(); i++) {
            if (cites.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    
    @Override
    public void printPapers(Comparator c) {
        if (papers.isEmpty()) { System.out.println("No papers."); return; }
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(c);
        System.out.println("=== Papers of " + name + " ===");
        for (ResearchPaper p : sorted) System.out.println(p.toString());
    }

    @Override
    public void addPaper() {
        System.out.println("Call addPaper(ResearchPaper) to add a paper.");
    }


    public void addPaper(ResearchPaper paper) {
        if (paper != null && !papers.contains(paper)) {
            papers.add(paper);
            System.out.println("Paper added: " + paper.getTitle());
        }
    }

    
    @Override
    public void joinProject() {
        System.out.println("Call joinProject(ResearchProject) to join.");
    }


    public void joinProject(ResearchProject project) {
        if (project != null) {
            project.addParticipant(this);
            System.out.println(name + " joined project: " + project.getTopic());
        }
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name=" + name +
               ", major=" + major + ", year=" + year +
               ", gpa=" + String.format("%.2f", gpa) + "}";
    }
}