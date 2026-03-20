import java.util.Scanner;

public class MainApp {
    // Used for coloring output in console
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ANSI_YELLOW + "Welcome to the Hotel Reservation Application! [v1.0.0]" + ANSI_RESET);

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
                    "7 - Exit Application\n" +
                    "-------------------------\n" + ANSI_RESET
            );

            // Get user input value
            System.out.println("Please enter an input value: ");

            // Checking if user inputted an integer
            if (!scanner.hasNextInt()) {
                System.out.println(ANSI_RED + "Error: Please enter an integer value" + ANSI_RESET);
                scanner.next();
                continue;
            }

            // Getting user input choice
            int input = scanner.nextInt();

            // Handling different user input values
            switch (input) {
                case 1:
                    System.out.println("Make a reservation");
                    break;
                case 2:
                    System.out.println("Cancel a reservation");
                    break;
                case 3:
                    System.out.println("Leave a review");
                    break;
                case 4:
                    System.out.println("Explore available attractions and transportation services nearby");
                    break;
                case 5:
                    System.out.println("Upgrade reservation");
                    break;
                case 6:
                    System.out.println("Show all reservations");
                    break;
                case 7:
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
