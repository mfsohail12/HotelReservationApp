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

            // -- Displaying hotel options -----------
            List<Hotel> hotels;

            try {
                hotels = HotelDao.getAllHotels();

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

            // -- Getting a hotel selection from user --------
            int chosenHotelId;

            while (true) {
                System.out.println("Please input chosen hotel id number:");
                chosenHotelId = scanner.nextInt();

                // Checking if selection is valid
                if (isValidHotelId(hotels, chosenHotelId)) break;
                else System.err.println("Invalid hotel id number. Please choose a hotel id that is shown in the available hotel options.");
            }

            // -- Getting check-in and check-out dates from user --------


            int num = scanner.nextInt();
            if (num == 0) return;
        }
    }

    /**
     * Checks if the given hotel id is found in the list of all hotels
     * @param hotels list of all hotels
     * @param hotelId the hotel id to search for
     * @return true if the hotel id is found in the list of all hotels
     */
    public boolean isValidHotelId(List<Hotel> hotels, int hotelId) {
        for (Hotel hotel : hotels) {
            if (hotel.getId() == hotelId) return true;
        }

        return false;
    }
}
