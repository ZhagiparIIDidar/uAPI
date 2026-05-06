package kz.uapi.users.Employee;

import java.time.LocalDateTime;
import java.util.List;

import kz.uapi.abs_class.Employee;
import kz.uapi.interfaces.Researcher;

public class ResearcherEmployee extends Employee implements Researcher {

    private int hIndex;
    private List<String> specializations;

    public ResearcherEmployee(String login, String password, String firstName, String lastName,
            LocalDateTime createdAt, String department, double salary,
            int hIndex, List<String> specializations) {
        super(login, password, "RESEARCHER", firstName, lastName, createdAt, department, salary);
        this.hIndex = hIndex;
        this.specializations = specializations;
    }

    public int getHIndex() {
        return hIndex;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setHIndex(int hIndex) {
        this.hIndex = hIndex;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " | Researcher | h-index: " + hIndex;
    }
}
