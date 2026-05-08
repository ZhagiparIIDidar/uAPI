package kz.uapi.menu;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }

    // ================================================================== MAIN MENU
    public static void showMainMenu() {
        while (true) {
            System.out.print("""
                    ================================================
                    UNIVERSITY INFORMATION SYSTEM
                    ================================================

                    1. Login
                    2. Exit

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> Login.showLoginMenu();
                case "2" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

}
