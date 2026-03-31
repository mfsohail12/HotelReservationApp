package submenus;

import java.util.*;
import java.sql.*;
import colors.ConsoleColors;
import database.Database;

public class LeaveReviewMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println(ConsoleColors.ANSI_PURPLE + "[LEAVE A REVIEW MENU]" + ConsoleColors.ANSI_RESET);
            System.out.println(ConsoleColors.ANSI_PURPLE + "Follow the instructions below to leave a review for a hotel you have stayed at." + ConsoleColors.ANSI_RESET);
            System.out.println();

            // Get user email
            System.out.println("Please enter your email (or 0 to return to main menu):");
            String email = scanner.next();
            if(email.equals("0")) {
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

                // Sub-menu, show hotels the user has stayed at (via their reservations)
                String hotelQuery = """
                    SELECT DISTINCT h.hotel_id, h.name, h.city
                    FROM Reservations res
                    JOIN RoomBookings rb ON res.reservation_id = rb.reservation_id
                    JOIN Hotels h ON rb.hotel_id = h.hotel_id
                    WHERE res.email = ?
                """;

                PreparedStatement hotelStmt = connection.prepareStatement(hotelQuery);
                hotelStmt.setString(1, email);
                ResultSet hotelRs = hotelStmt.executeQuery();

                List<int[]> hotelIds = new ArrayList<>();
                List<String> hotelDisplays = new ArrayList<>();

                while (hotelRs.next()) {
                    int hid = hotelRs.getInt("hotel_id");
                    String hname = hotelRs.getString("name");
                    String hcity = hotelRs.getString("city");
                    hotelIds.add(new int[]{hid});
                    hotelDisplays.add(hid + " - " + hname + " (" + hcity + ")");
                }

                hotelRs.close();
                hotelStmt.close();

                if (hotelIds.isEmpty()) {
                    System.err.println("You have no reservations on record. You can only review hotels you have stayed at.");
                    System.err.println("Returning to main menu ...");
                    return;
                }

                // Display sub-menu of hotels
                System.out.println(ConsoleColors.ANSI_BLUE + "━━Hotels You Have Stayed At:" + "━".repeat(42) + ConsoleColors.ANSI_RESET);
                for (String display : hotelDisplays) {
                    System.out.println(ConsoleColors.ANSI_BLUE + display + ConsoleColors.ANSI_RESET);
                }
                System.out.println(ConsoleColors.ANSI_BLUE + "━".repeat(69) + ConsoleColors.ANSI_RESET);

                // Get hotel selection
                int chosenHotelId;
                while (true) {
                    System.out.println("Please input the hotel id you want to review:");
                    if (!scanner.hasNextInt()) {
                        System.err.println("Error: Please enter an integer value.");
                        scanner.next();
                        continue;
                    }
                    chosenHotelId = scanner.nextInt();

                    boolean valid = false;
                    for (int[] id : hotelIds) {
                        if (id[0] == chosenHotelId) { valid = true; break; }
                    }

                    if (valid) break;
                    else System.err.println("Invalid hotel id. Please choose from the list above.");
                }

                // Get rating (1-5)
                int rating;
                while (true) {
                    System.out.println("Please enter a rating (1-5):");
                    if (!scanner.hasNextInt()) {
                        System.err.println("Error: Please enter an integer value.");
                        scanner.next();
                        continue;
                    }
                    rating = scanner.nextInt();
                    if (rating >= 1 && rating <= 5) break;
                    else System.err.println("Rating must be between 1 and 5.");
                }

                scanner.nextLine();

                // Get review title
                System.out.println("Please enter a title for your review:");
                String title = scanner.nextLine();

                // Get review comment
                System.out.println("Please enter your review comment:");
                String comment = scanner.nextLine();

                // Get next review_id
                Statement maxStmt = connection.createStatement();
                ResultSet maxRs = maxStmt.executeQuery("SELECT MAX(review_id) AS max_id FROM Reviews");
                int nextReviewId = 1;
                if (maxRs.next()) {
                    nextReviewId = maxRs.getInt("max_id") + 1;
                }
                maxRs.close();
                maxStmt.close();

                // Insert the review
                String insertQuery = """
                    INSERT INTO Reviews (review_id, rating, date, title, comment, email, hotel_id)
                    VALUES (?, ?, CURRENT DATE, ?, ?, ?, ?)
                """;

                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setInt(1, nextReviewId);
                insertStmt.setInt(2, rating);
                insertStmt.setString(3, title);
                insertStmt.setString(4, comment);
                insertStmt.setString(5, email);
                insertStmt.setInt(6, chosenHotelId);

                int rowsInserted = insertStmt.executeUpdate();
                insertStmt.close();

                if (rowsInserted > 0) {
                    System.out.println(ConsoleColors.ANSI_YELLOW + "Review submitted successfully! Thank you for your feedback." + ConsoleColors.ANSI_RESET);
                } else {
                    System.err.println("Something went wrong. The review was not submitted.");
                }

                return;

            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
                return;
            }
        }
    }
}
