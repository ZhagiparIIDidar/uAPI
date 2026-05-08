package kz.uapi.menu;

import java.util.Scanner;

import kz.uapi.abs_class.User;
import kz.uapi.database.AuthService;
import kz.uapi.users.Employee.Admin;
import kz.uapi.users.Employee.Manager;
import kz.uapi.users.Employee.ResearcherEmployee;
import kz.uapi.users.Employee.Teacher;

public class Login {

    static Scanner scanner = Main.getScanner();

    // ================================================================== LOGIN MENU
    static void showLoginMenu() {
        System.out.print("""

                ================================================
                                    LOGIN
                ================================================

                Enter login:\s""");
        String login = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = AuthService.login(login, password);

        if (user == null) {
            System.out.println("\nInvalid login or password. Try again.\n");
            return;
        }

        System.out.println("\nWelcome, " + user.getFirstName() + "!\n");

        switch (user.getRole()) {
            case "STUDENT" -> StudentMenu.showStudentMenu((kz.uapi.users.Student) user);
            case "TEACHER" -> TeacherMenu.showTeacherMenu((Teacher) user);
            case "MANAGER" -> ManagerMenu.showManagerMenu((Manager) user);
            case "ADMIN" -> AdminMenu.showAdminMenu((Admin) user);
            case "RESEARCHER" -> ResearcherMenu.showResearcherEmployeeMenu((ResearcherEmployee) user);
            default -> System.out.println("Unknown role.\n");
        }
    }

}
