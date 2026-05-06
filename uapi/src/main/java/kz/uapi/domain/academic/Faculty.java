package kz.uapi.domain.academic;

public class Faculty {

    private int facultyId;
    private String name;

    public Faculty(int facultyId, String name) {
        this.facultyId = facultyId;
        this.name = name;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public String getName() {
        return name;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return facultyId + " | " + name;
    }
}
