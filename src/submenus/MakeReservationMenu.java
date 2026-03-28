package submenus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.sql.*;
import colors.ConsoleColors;
import database.dao.HotelDao;
import database.dao.RoomDao;
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

            // Getting a hotel selection from user
            int chosenHotelId;

            while (true) {
                System.out.println("Please input chosen hotel id number:");
                if (scanner.hasNextInt()) {
                    chosenHotelId = scanner.nextInt();

                    // Checking if selection is valid
                    if (isValidHotelId(hotels, chosenHotelId)) break;
                }

                else System.err.println("Invalid hotel id number. Please choose a hotel id that is shown in the available hotel options.");
            }

            // Getting check-in and check-out dates from user
            String checkInDate;
            String checkOutDate;

            scanner.nextLine(); // clear buffer
            while (true) {
                System.out.println("Please input your desired check-in date in a YYYY-MM-DD format:");
                checkInDate = scanner.nextLine();
                System.out.println("Please enter your desired check-out date in a YYYY-MM-DD format:");
                checkOutDate = scanner.nextLine();

                if (validateCheckInAndOutDates(checkInDate, checkOutDate)) break;
                else System.err.println("Invalid check-in or check-out date. Please try again.");
            }

            // Displaying available regular rooms and suites at chosen hotel to user
            List<RegularRoom> regularRooms;
            List<Suite> suites;

            try {
                regularRooms = RoomDao.getRegularRooms(chosenHotelId, checkInDate, checkOutDate);
                suites = RoomDao.getSuites(chosenHotelId, checkInDate, checkOutDate);

                // Regular rooms
                System.out.println(ConsoleColors.ANSI_BLUE + "━━Available Regular Rooms:" + "━".repeat(100) + ConsoleColors.ANSI_RESET);
                for (RegularRoom room : regularRooms) {
                    System.out.println(ConsoleColors.ANSI_BLUE + room + ConsoleColors.ANSI_RESET);
                }
                System.out.println(ConsoleColors.ANSI_BLUE + "━".repeat(115) + ConsoleColors.ANSI_RESET);

                // Suites
                System.out.println(ConsoleColors.ANSI_BLUE + "━━Available Suites:" + "━".repeat(100) + ConsoleColors.ANSI_RESET);
                for (Suite suite : suites) {
                    System.out.println(ConsoleColors.ANSI_BLUE + suite + ConsoleColors.ANSI_RESET);
                }
                System.out.println(ConsoleColors.ANSI_BLUE + "━".repeat(115) + ConsoleColors.ANSI_RESET);
            } catch (SQLException e) {
                System.err.println("Message: " + e.getMessage());
                System.err.println("There was an error listing available rooms. Returning to the main menu ... ");
                return;
            }








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
    private boolean isValidHotelId(List<Hotel> hotels, int hotelId) {
        for (Hotel hotel : hotels) {
            if (hotel.getId() == hotelId) return true;
        }

        return false;
    }

    /**
     * Checks if given check-in and check-out dates are valid by first checking if they are in
     * YYYY-MM-DD format and the check-in date is before the check-out date
     * @param checkInDate the check-in date string
     * @param checkOutDate the check-out date string
     * @return true if the check-in and check-out dates are valid
     */
    private boolean validateCheckInAndOutDates(String checkInDate, String checkOutDate) {
        if (checkInDate == null || checkOutDate == null) return false;

        // Checking if both strings are in YYYY-MM-DD format
        if (!validateDateFormat(checkInDate) || !validateDateFormat(checkOutDate)) return false;

        // Checking if check-in before check-out
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);

        return checkIn.isBefore(checkOut);
    }

    /**
     * Checks if a given date string is in YYYY-MM-DD format
     * @param date the date string
     * @return true if the date string is in YYYY-MM-DD format
     */
    private boolean validateDateFormat(String date) {
        if (date == null) return false;

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // enforces valid month and day values
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
