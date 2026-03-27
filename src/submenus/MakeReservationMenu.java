package submenus;

import java.util.*;
import java.sql.*;
import colors.ConsoleColors;
import database.Database;

public class MakeReservationMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Displaying menu info
            System.out.println(ConsoleColors.ANSI_PURPLE + "[MAKE RESERVATION MENU]" + ConsoleColors.ANSI_RESET);
            System.out.println("Follow the instructions below to make a reservation at a hotel.");

            // Displaying hotel options

        }
    }
}
