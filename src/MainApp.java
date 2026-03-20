import Submenus.*;

import java.util.Scanner;

/**
 * This is the main file where the application is run
 */
public class MainApp {
    // Ansi colour codes for coloring output in the console
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * Displays the main menu of the application with all options
     */
    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW + "Welcome to the Hotel Reservation Application! [v1.0.0]" + ANSI_RESET);
        Scanner scanner = new Scanner(System.in);

        // Main menu loop
        while (true) {
            // Displaying input menu options
            System.out.println(ANSI_BLUE + "\n-------------------------\n" +
                    "Input Menu:\n" +
                    "1 - Make a reservation\n" +
                    "2 - Cancel a reservation\n" +
                    "3 - Leave a review\n" +
                    "4 - Explore available attractions and transportation services nearby\n" +
                    "5 - Upgrade reservation\n" +
                    "6 - Show all reservations\n" +
                    "0 - Exit Application\n" +
                    "-------------------------\n" + ANSI_RESET
            );

            // Getting input value
            System.out.println("Please enter an input value: ");

            // Checking if user input is an integer
            if (!scanner.hasNextInt()) {
                System.out.println(ANSI_RED + "Error: Please enter an integer value" + ANSI_RESET);
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
                    System.out.println(ANSI_YELLOW + "Exiting Application ..." + ANSI_RESET);
                    System.out.println(ANSI_YELLOW + "Goodbye!" + ANSI_RESET);
                    System.exit(0);
                default:
                    System.out.println(ANSI_RED + "Error: Please enter a valid input value from the menu" + ANSI_RESET);
                    break;
            }
        }
    }
}
