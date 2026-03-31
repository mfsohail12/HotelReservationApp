package submenus;

import java.util.*;
import java.sql.*;
import colors.ConsoleColors;
import database.Database;
import database.dao.ReservationDao;
import entities.Reservation;

public class CancelReservationMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println(ConsoleColors.ANSI_PURPLE + "[CANCEL RESERVATION MENU]" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_PURPLE + "Follow the instructions below to cancel a reservation." + ConsoleColors.ANSI_RESET);
            System.out.println();

            // Get user email
            System.out.println("Please enter your email (or 0 to return to main menu):");
            String email = scanner.next();
            if (email.equals("0")){
                return;
            }

            // Verify user exists
            Connection connection = Database.getConnection();
            try {
                PreparedStatement userCheck = connection.prepareStatement("SELECT email, name FROM Users WHERE email = ?");
                userCheck.setString(1, email);
                ResultSet userRs = userCheck.executeQuery();

                if (!userRs.next()) {
                    System.err.println("No user found with that email. Returning to main menu ...");
                    userRs.close();
                    userCheck.close();
                    return;
                }

                String userName = userRs.getString("name");
                userRs.close();
                userCheck.close();

                System.out.println("Welcome, " + userName + "!");

                // Get all reservations
                List<Reservation> reservations = ReservationDao.getReservationsByEmail(email);

                if (reservations.isEmpty()) {
                    System.err.println("You have no reservations to cancel.");
                    System.err.println("Returning to main menu ...");
                    return;
                }

                // Build unique reservation ids and display strings
                Set<Integer> reservationIds = new LinkedHashSet<>();
                System.out.println(ConsoleColors.ANSI_BLUE + "━━Your Reservations:" + "━".repeat(50) + ConsoleColors.ANSI_RESET);

                int currentResId = -1;
                for (Reservation res : reservations) {
                    reservationIds.add(res.reservationId);

                    if (res.reservationId != currentResId) {
                        if (currentResId != -1) {
                            System.out.println(ConsoleColors.ANSI_BLUE + "─".repeat(69) + ConsoleColors.ANSI_RESET);
                        }
                        currentResId = res.reservationId;
                        System.out.println(ConsoleColors.ANSI_BLUE +
                                "Reservation #" + res.reservationId + " | " + res.hotelName + " (" + res.city + ")" +
                                " | Check-in: " + res.checkIn + " | Check-out: " + res.checkOut +
                                " | Status: " + res.getPaidStatus() +
                                ConsoleColors.ANSI_RESET);
                    }

                    System.out.println(ConsoleColors.ANSI_BLUE +
                            "    Room " + res.roomNumber +
                            " | $" + res.price + "/night" +
                            " | Bed type: " + res.bedType +
                            " | Beds: " + res.numBeds +
                            ConsoleColors.ANSI_RESET);
                }

                System.out.println(ConsoleColors.ANSI_BLUE + "━".repeat(69) + ConsoleColors.ANSI_RESET);

                // Get reservation selection
                int chosenResId;
                while (true) {
                    System.out.println("Please input the reservation id you want to cancel:");
                    if (!scanner.hasNextInt()) {
                        System.err.println("Error: Please enter an integer value.");
                        scanner.next();
                        continue;
                    }
                    chosenResId = scanner.nextInt();

                    if (reservationIds.contains(chosenResId)) break;
                    else System.err.println("Invalid reservation id. Please choose from the list above.");
                }

                // Confirm cancellation
                System.out.println("Are you sure you want to cancel Reservation #" + chosenResId + "? (yes/no):");
                String confirmation = scanner.next();

                if (confirmation.equalsIgnoreCase("no")) {
                    System.out.println("Cancellation aborted. Returning to main menu...");
                    return;

                } else if (confirmation.equalsIgnoreCase("yes")) {

                    // Delete related records then the reservation
                    PreparedStatement deletePayments = connection.prepareStatement(
                            "DELETE FROM Payments WHERE reservation_id = ?");
                    deletePayments.setInt(1, chosenResId);
                    deletePayments.executeUpdate();
                    deletePayments.close();

                    PreparedStatement deleteBookings = connection.prepareStatement(
                            "DELETE FROM RoomBookings WHERE reservation_id = ?");
                    deleteBookings.setInt(1, chosenResId);
                    deleteBookings.executeUpdate();
                    deleteBookings.close();

                    PreparedStatement deleteRes = connection.prepareStatement(
                            "DELETE FROM Reservations WHERE reservation_id = ?");
                    deleteRes.setInt(1, chosenResId);
                    int rowsDeleted = deleteRes.executeUpdate();
                    deleteRes.close();

                    if (rowsDeleted > 0) {
                        System.out.println(ConsoleColors.ANSI_YELLOW + "Reservation #" + chosenResId + " has been successfully cancelled." + ConsoleColors.ANSI_RESET);
                    } else {
                        System.err.println("Something went wrong. The reservation was not cancelled.");
                    }

                    return;

                } else {
                    // Invalid input
                    System.out.println("Invalid input. Cancellation aborted.");
                    return;
                }

            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
                return;
            }
        }
    }
}
