package kz.uapi.menu;

import java.util.Scanner;

import kz.uapi.users.Employee.ResearcherEmployee;

public class ResearcherMenu {

    static Scanner scanner = Main.getScanner();

    // ================================================================== RESEARCHER
    static void showResearcherEmployeeMenu(ResearcherEmployee researcher) {
        while (true) {
            System.out.print("""
                    ================================================
                    RESEARCHER MENU
                    ================================================

                    1. View Personal Info
                    2. Research Projects
                    3. Publish Research Paper
                    4. Messages
                    5. News
                    0. Exit

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CommonMethods.viewPersonalInfo(researcher.getLogin());
                case "2" -> CommonMethods.researchProjects(researcher.getLogin());
                case "3" -> CommonMethods.publishResearchPaper(researcher.getLogin());
                case "4" -> CommonMethods.sendMessages(researcher.getLogin());
                case "5" -> CommonMethods.viewNews();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

}
