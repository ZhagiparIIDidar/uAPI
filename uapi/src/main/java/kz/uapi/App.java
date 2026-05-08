package kz.uapi;

import kz.uapi.menu.Main;
import kz.uapi.database.*;

public class App {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        DatabaseInitializer.insertTestData();
        Main.showMainMenu();
    }
}