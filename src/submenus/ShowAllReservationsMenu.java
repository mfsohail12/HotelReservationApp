package submenus;

import java.util.*;
import java.sql.*;
import colors.ConsoleColors;
import database.Database;
import database.dao.ReservationDao;
import database.dao.ReservationDao.ReservationInfo;

public class ShowAllReservationsMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println(ConsoleColors.ANSI_PURPLE + "[SHOW ALL RESERVATIONS MENU]" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_PURPLE + "View all your current and past reservations." + ConsoleColors.ANSI_RESET);
            System.out.println();

            // -- Get user email --
            System.out.println("Please enter your email (or 0 to return to main menu):");
            String email = scanner.next();
            if (email.equals("0")) return;

            // -- Verify user exists --
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

                // -- Get all reservations --
                List<ReservationInfo> reservations = ReservationDao.getReservationsByEmail(email);

                if (reservations.isEmpty()) {
                    System.err.println("You have no reservations on record.");
                    System.out.println(ConsoleColors.ANSI_BLUE + "━".repeat(69) + ConsoleColors.ANSI_RESET);
                    return;
                }

                System.out.println(ConsoleColors.ANSI_BLUE + "━━Your Reservations:" + "━".repeat(50) + ConsoleColors.ANSI_RESET);

                int currentResId = -1;
                for (ReservationInfo res : reservations) {
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

                return;

            } catch (SQLException e) {
                System.err.println("Message: " + e.getMessage());
                System.err.println("There was a database error. Returning to main menu ...");
                return;
            }
        }
    }
}
