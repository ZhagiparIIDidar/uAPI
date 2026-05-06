package kz.uapi.users.Employee;

import java.time.LocalDateTime;

import kz.uapi.interfaces.*;
import kz.uapi.abs_class.Employee;
import kz.uapi.enums.ManagerType;

public class Manager extends Employee implements OrManager, AcademicManager,
        StudentDepartmentManager, HrManager, FinanceManager {

    private ManagerType managerType;

    public Manager(String login, String password, String firstName, String lastName,
            LocalDateTime createdAt, String department, double salary,
            ManagerType managerType) {
        super(login, password, "MANAGER", firstName, lastName, createdAt, department, salary);
        this.managerType = managerType;
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void setManagerType(ManagerType managerType) {
        this.managerType = managerType;
    }

    @Override
    public void approveRegistration() {
        if (managerType != ManagerType.OR)
            throw new UnsupportedOperationException("Only OR Manager can approve registration");
    }

    @Override
    public void manageSchedule() {
        if (managerType != ManagerType.ACADEMIC)
            throw new UnsupportedOperationException("Only Academic Manager can manage schedule");
    }

    @Override
    public void manageEvents() {
        if (managerType != ManagerType.STUDENT)
            throw new UnsupportedOperationException("Only Student Department Manager can manage events");
    }

    @Override
    public void manageNews() {
        if (managerType != ManagerType.STUDENT)
            throw new UnsupportedOperationException("Only Student Department Manager can manage news");
    }

    @Override
    public void addUser() {
        if (managerType != ManagerType.HR)
            throw new UnsupportedOperationException("Only HR Manager can add users");
    }

    @Override
    public void dropUser() {
        if (managerType != ManagerType.HR)
            throw new UnsupportedOperationException("Only HR Manager can drop users");
    }

    @Override
    public void manageSalaries() {
        if (managerType != ManagerType.FINANCE)
            throw new UnsupportedOperationException("Only Finance Manager can manage salaries");
    }

    @Override
    public void manageScholarships() {
        if (managerType != ManagerType.FINANCE)
            throw new UnsupportedOperationException("Only Finance Manager can manage scholarships");
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " | Manager: " + managerType;
    }
}
