package submenus;

import colors.ConsoleColors;
import database.dao.ReservationDao;
import java.sql.SQLException;
import java.util.Scanner;

public class UpgradeReservationMenu implements Submenu {

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        // Displaying menu info
        System.out.println();
        System.out.println(ConsoleColors.ANSI_PURPLE + "[UPGRADE RESERVATION MENU]" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_PURPLE + "Follow the instructions below to upgrade your reservation." + ConsoleColors.ANSI_RESET);
        System.out.println();

        try {
            int reservationId;
            int oldRoomNumber;

            // Validate reservation ID
            while (true) {
                System.out.print("Enter Reservation ID: ");

                if (scanner.hasNextInt()) {
                    reservationId = scanner.nextInt();

                    if (reservationId > 0) {
                        break;
                    } else {
                        System.out.println("Reservation ID must be a positive number.");
                    }

                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // clear invalid input
                }
            }

            // Validate room number
            while (true) {
                System.out.print("Enter Current Room Number to Upgrade: ");

                if (scanner.hasNextInt()) {
                    oldRoomNumber = scanner.nextInt();

                    if (oldRoomNumber > 0) {
                        break;
                    } else {
                        System.out.println("Room number must be positive.");
                    }

                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // clear invalid input
                }
            }

            // Get hotel id from reservation id
            int hotelId = ReservationDao.getHotelIdFromResId(reservationId);

            // Call procedure
            ReservationDao.upgradeRoom(reservationId, oldRoomNumber, hotelId);

        } catch (SQLException e) {
            System.out.println("Error upgrading room: " + e.getMessage());
        }
    }
}