package kz.uapi.domain.academic;

/**
 * Класс, представляющий факультет университета.
 * Факультет содержит информацию о своём идентификаторе и названии.
 * 
 * <p>
 * Факультет является организационной единицей, которая содержит множество
 * курсов.
 * </p>
 * 
 * @author UAPI System
 * @version 1.0
 * @since 1.0
 */
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
