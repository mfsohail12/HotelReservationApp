import colors.ConsoleColors;
import database.Database;
import submenus.*;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * This is the main file where the application is run
 */
public class MainApp {
    /**
     * This is the main function for launching the Hotel Reservation Application.
     * Initializes a connection to the database and displays the main menu of the
     * application with available options.
     */
    public static void main(String[] args) {
        System.out.println(ConsoleColors.ANSI_YELLOW + "Welcome to the Hotel Reservation Application! [v1.0.0]" + ConsoleColors.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);

        // Initializing database connection
        try {
            String your_userid = System.getenv("SOCSUSER");
            String your_password = System.getenv("SOCSPASSWD");

            if (your_userid == null || your_password == null) {
                throw new IllegalStateException("Error: Unable to find SOCSUSER or SOCSPASSWD environment variables.");
            }

            Database.init(your_userid, your_password);
        } catch (Exception e) {
            System.err.println("There was an error connecting to the database");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Main menu loop
        while (true) {
            // Displaying menu title
            System.out.println(ConsoleColors.ANSI_PURPLE + "[MAIN MENU]" + ConsoleColors.ANSI_RESET);

            // Displaying input menu options
            System.out.println(ConsoleColors.ANSI_BLUE +
                    "\n━━Input Menu:━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "1 - Make a reservation\n" +
                    "2 - Cancel a reservation\n" +
                    "3 - Leave a review\n" +
                    "4 - Explore available attractions and transportation services nearby\n" +
                    "5 - Upgrade reservation\n" +
                    "6 - Show all reservations\n" +
                    "0 - Exit Application\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    ConsoleColors.ANSI_RESET
            );

            // Getting input value
            System.out.println("Please enter an input value: ");

            // Checking if user input is an integer
            if (!scanner.hasNextInt()) {
                System.err.println("Error: Please enter an integer value");
                scanner.next();
                continue;
            }

            int input = scanner.nextInt();

            // Handling different user input values
            switch (input) {
                case 1:
                    MakeReservationMenu makeReservationMenu = new MakeReservationMenu();
                    makeReservationMenu.showMenu();
                    break;
                case 2:
                    CancelReservationMenu cancelReservationMenu = new CancelReservationMenu();
                    cancelReservationMenu.showMenu();
                    break;
                case 3:
                    LeaveReviewMenu leaveReviewMenu = new LeaveReviewMenu();
                    leaveReviewMenu.showMenu();
                    break;
                case 4:
                    ExploreMenu exploreMenu = new ExploreMenu();
                    exploreMenu.showMenu();
                    break;
                case 5:
                    UpgradeReservationMenu upgradeReservationMenu = new UpgradeReservationMenu();
                    upgradeReservationMenu.showMenu();
                    break;
                case 6:
                    ShowAllReservationsMenu showAllReservationsMenu = new ShowAllReservationsMenu();
                    showAllReservationsMenu.showMenu();
                    break;
                case 0:
                    System.out.println(ConsoleColors.ANSI_YELLOW + "Exiting Application ..." + ConsoleColors.ANSI_RESET);
                    System.out.println(ConsoleColors.ANSI_YELLOW + "Goodbye!" + ConsoleColors.ANSI_RESET);

                    // Closing connection to the database before exiting the application
                    try {
                        Database.close();
                    } catch (SQLException e) {
                        System.err.println("There was an error closing the connection to the database");
                        e.printStackTrace();
                    } finally {
                        System.exit(0);
                    }
                default:
                    System.err.println("Error: Please enter a valid input value from the menu");
                    break;
            }
        }
    }
}
