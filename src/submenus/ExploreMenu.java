package submenus;
import colors.ConsoleColors;
import database.dao.*;
import entities.Attraction;
import entities.Transportation;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;

public class ExploreMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        // Displaying menu info
        System.out.println();
        System.out.println(ConsoleColors.ANSI_PURPLE + "[EXPLORATION MENU]" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_PURPLE + "Explore nearby attractions and transportation based on your reservation." + ConsoleColors.ANSI_RESET);
        System.out.println();

        try {
            int reservationId;

            // Validate reservation ID
            while (true) {
                System.out.print("Enter Reservation ID (or 0 to return to main menu): ");

                if (scanner.hasNextInt()) {
                    reservationId = scanner.nextInt();
                    if (reservationId == 0) {return;}
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
     // Get hotel id from reservation id
            int hotelId = ReservationDao.getHotelIdFromResId(reservationId);

            // Call procedures
            List<Attraction> attractions = AttractionDao.getAttractionsByHotel(hotelId);
            List<Transportation> transportations = TransportationDao.getTransportByHotel(hotelId);

            // Iteratively print records
            if (attractions.isEmpty()) {
                System.out.println("\nNo nearby attractions found.");
            }
            else {
            System.out.println("-----------------------------");
            System.out.println("Nearby Attractions:");
            for (Attraction a : attractions) {
                System.out.println(a);
            }
            System.out.println("-----------------------------");
        }
            if (transportations.isEmpty()) {
                System.out.println("No nearby transportation found.");
            }
            else {
            System.out.println("Nearby Transportation:");
            for (Transportation t : transportations) {
                System.out.println(t);
            }
            System.out.println("-----------------------------\n");
        }
        } catch (SQLException e) {
            System.out.println("Error retrieving exploration data: " + e.getMessage());
        }
    }
}
