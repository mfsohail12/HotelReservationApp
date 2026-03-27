package submenus;

import java.util.*;
import java.sql.*;
import colors.ConsoleColors;
import database.Database;
import database.dao.HotelDao;
import entities.*;

public class MakeReservationMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Displaying menu info
            System.out.println();
            System.out.println(ConsoleColors.ANSI_PURPLE + "[MAKE RESERVATION MENU]" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_PURPLE + "Follow the instructions below to make a reservation at a hotel." + ConsoleColors.ANSI_RESET);
            System.out.println();

            // Displaying hotel options
            try {
                List<Hotel> hotels = HotelDao.getAllHotels();

                System.out.println(ConsoleColors.ANSI_BLUE + "━━Available Hotels:" + "━".repeat(50) + ConsoleColors.ANSI_RESET);
                for (Hotel hotel : hotels) {
                    System.out.println(ConsoleColors.ANSI_BLUE + hotel + ConsoleColors.ANSI_RESET);
                }
                System.out.println(ConsoleColors.ANSI_BLUE + "━".repeat(69) + ConsoleColors.ANSI_RESET);
            } catch (SQLException e) {
                System.err.println("Message: " + e.getMessage());
                System.err.println("There was an error listing available hotels. Returning to the main menu ... ");
                return;
            }

            int num = scanner.nextInt();
            if (num == 0) return;
        }
    }
}
