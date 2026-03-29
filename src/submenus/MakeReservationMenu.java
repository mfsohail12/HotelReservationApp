package submenus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.sql.*;
import colors.ConsoleColors;
import database.dao.HotelDao;
import database.dao.RoomDao;
import database.dao.ReservationDao;
import database.dao.UserDao;
import entities.*;

public class MakeReservationMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        // User input values
        int chosenHotelId;
        String checkInDate;
        String checkOutDate;
        List<Integer> selectedRoomsNumbers = new ArrayList<>();

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

            // Getting chosen rooms numbers from user:
            List<Room> allAvailableRooms = new ArrayList<>(regularRooms); // merging regular rooms and suites lists into one
            allAvailableRooms.addAll(suites);
            
            while (true) {
                System.out.println("Please input a desired room number. When finished selecting desired rooms, input 'done':");

                if (scanner.hasNextInt()) {
                    int roomNumber = scanner.nextInt();
                    scanner.nextLine(); // clear input

                    boolean validRoomNumber = false;

                    // Checking if selected room number is one of the displayed ones
                    

                    for (Room room : allAvailableRooms) {

                        if (room.getRoomNumber() == roomNumber) { // valid room number was given
                            validRoomNumber = true;

                            // Checking if the room hasn't already been selected by the user to avoid duplicates
                            if (selectedRoomsNumbers.contains(roomNumber)) {
                                System.err.println("You have already selected room " + roomNumber + ".");
                                break;
                            }

                            selectedRoomsNumbers.add(room.getRoomNumber());
                            break;
                        }
                    }

                    // Display error for invalid selection
                    if (!validRoomNumber) {
                        System.err.println("Please enter a room number shown in the available rooms menu.");
                    }
                } else {
                    // Checking if user is done choosing rooms
                    String input = scanner.nextLine();

                    if (input.equals("done")) {
                        // Checking if user chose at least one room
                        if (selectedRoomsNumbers.isEmpty()) {
                            System.err.println("You must choose at least one room!");
                            continue;
                        } else {
                            break;
                        }
                    } else {
                        System.err.println("Invalid room number. Please enter a room number.");
                    }

                }
            }

            // Getting user information
            scanner.nextLine(); // clear buffer
            String userName;
            String userEmail;
            LocalDate dateOfBirth;

            while (true) {
                System.out.println("Please enter your full name:");
                userName = scanner.nextLine().trim();

                if (userName.isEmpty()) {
                    System.err.println("Name cannot be empty. Please try again.");
                    continue;
                }

                System.out.println("Please enter your email:");
                userEmail = scanner.nextLine().trim();

                if (!isValidEmail(userEmail)) {
                    System.err.println("Invalid email format. Please try again.");
                    continue;
                }

                System.out.println("Please enter your date of birth in YYYY-MM-DD format:");
                String dobString = scanner.nextLine().trim();

                if (!validateDateFormat(dobString)) {
                    System.err.println("Invalid date format. Please use YYYY-MM-DD format.");
                    continue;
                }

                dateOfBirth = LocalDate.parse(dobString);

                // Validate user is old enough (18 years old)
                LocalDate today = LocalDate.now();
                long age = ChronoUnit.YEARS.between(dateOfBirth, today);

                if (age < 18) {
                    System.err.println("You must be at least 18 years old to make a reservation.");
                    continue;
                }

                break;
            }

            // Check if user exists in database
            boolean userExists = false;
            User user = null;

            try {
                userExists = UserDao.userExists(userEmail);

                if (userExists) {
                    user = UserDao.getUserByEmail(userEmail);
                } else {
                    // Create new user
                    user = new User(userName, userEmail, dateOfBirth);
                }
            } catch (SQLException e) {
                System.err.println("Message: " + e.getMessage());
                System.err.println("There was an error checking user information. Returning to the main menu ... ");
                return;
            }

            // Calculate total cost
            int totalCost = 0;
            List<Room> selectedRooms = new ArrayList<>();

            // Calculate nights
            LocalDate checkInLocalDate = LocalDate.parse(checkInDate);
            LocalDate checkOutLocalDate = LocalDate.parse(checkOutDate);
            long nights = ChronoUnit.DAYS.between(checkInLocalDate, checkOutLocalDate);

            for (int roomNumber : selectedRoomsNumbers) {
                // Find room info from allAvailableRooms list
                Room selectedRoom = null;

                for (Room room : allAvailableRooms) {
                    if (room.getRoomNumber() == roomNumber) {
                        selectedRoom = room;
                        break;
                    }
                }

                selectedRooms.add(selectedRoom);

                // Add to total cost
                totalCost += selectedRoom.getPrice() * nights;
            }

            // Display confirmation
            System.out.println();
            System.out.println(ConsoleColors.ANSI_PURPLE + "━━[RESERVATION CONFIRMATION]━" + "━".repeat(40) + ConsoleColors.ANSI_RESET);

            System.out.println(ConsoleColors.ANSI_BLUE + "User Information:" + ConsoleColors.ANSI_RESET);
            System.out.println("  Name: " + userName);
            System.out.println("  Email: " + userEmail);
            System.out.println("  Date of Birth: " + dateOfBirth);

            Hotel chosenHotel = null;
            try {
                for (Hotel h : hotels) {
                    if (h.getId() == chosenHotelId) {
                        chosenHotel = h;
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error retrieving hotel information.");
                return;
            }

            System.out.println();
            System.out.println(ConsoleColors.ANSI_BLUE + "Reservation Details:" + ConsoleColors.ANSI_RESET);
            System.out.println("  Hotel: " + chosenHotel.getName());
            System.out.println("  Check-in: " + checkInDate);
            System.out.println("  Check-out: " + checkOutDate);
            System.out.println("  Number of nights: " + nights);

            System.out.println();
            System.out.println(ConsoleColors.ANSI_BLUE + "Selected Rooms:" + ConsoleColors.ANSI_RESET);
            for (Room room : selectedRooms) {
                System.out.println(room);
            }

            System.out.println();
            System.out.println(ConsoleColors.ANSI_BLUE + "Total Cost: $" + totalCost + ConsoleColors.ANSI_RESET);

            System.out.println();
            System.out.println("Do you want to proceed with this reservation? (yes/no)");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("yes")) {
                System.out.println("Reservation cancelled. Returning to the main menu ...");
                return;
            }

            // Get payment amount
            int paymentAmount;
            while (true) {
                System.out.println("Enter payment amount (0 to " + totalCost + "):");
                if (scanner.hasNextInt()) {
                    paymentAmount = scanner.nextInt();
                    scanner.nextLine(); // clear buffer

                    if (paymentAmount >= 0 && paymentAmount <= totalCost) {
                        break;
                    } else {
                        System.err.println("Payment amount must be between 0 and " + totalCost + ".");
                    }
                } else {
                    System.err.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            }

            // Get payment method if payment amount > 0
            String paymentMethod = null;
            if (paymentAmount > 0) {
                while (true) {
                    System.out.println("Select payment method:");
                    System.out.println("1. Credit");
                    System.out.println("2. Debit");
                    System.out.println("3. PayPal");
                    String methodChoice = scanner.nextLine().trim();

                    if (methodChoice.equals("1")) {
                        paymentMethod = "Credit";
                        break;
                    } else if (methodChoice.equals("2")) {
                        paymentMethod = "Debit";
                        break;
                    } else if (methodChoice.equals("3")) {
                        paymentMethod = "PayPal";
                        break;
                    } else {
                        System.err.println("Invalid choice. Please select 1, 2, or 3.");
                    }
                }
            }

            // Insert into database
            try {
                // Insert user if doesn't exist
                if (!userExists) {
                    UserDao.insertUser(user);
                    System.out.println("New user created with email: " + userEmail);
                }

                // Insert reservation
                int reservationId = ReservationDao.insertReservation(userEmail, checkInLocalDate, checkOutLocalDate);

                // Insert room bookings
                for (int roomNumber : selectedRoomsNumbers) {
                    ReservationDao.insertRoomBooking(reservationId, chosenHotelId, roomNumber);
                }

                // Insert payment if payment amount > 0
                if (paymentAmount > 0) {
                    ReservationDao.insertPayment(reservationId, paymentAmount, paymentMethod, userEmail);
                    System.out.println(ConsoleColors.ANSI_GREEN + "Payment of $" + paymentAmount + " recorded via " + paymentMethod + "." + ConsoleColors.ANSI_RESET);
                }

                System.out.println();
                System.out.println(ConsoleColors.ANSI_GREEN + "━━[RESERVATION SUCCESSFUL]━" + "━".repeat(40) + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_GREEN + "Your reservation has been created!" + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_GREEN + "Reservation ID: " + reservationId + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_GREEN + "Please save this ID for your records." + ConsoleColors.ANSI_RESET);
                System.out.println();

                // Ask if user wants to make another reservation
                System.out.println("Would you like to make another reservation? (yes/no)");
                String anotherReservation = scanner.nextLine().trim().toLowerCase();

                if (!anotherReservation.equals("yes")) {
                    System.out.println("Thank you for using our Hotel Reservation App!");
                    return;
                }

                // Reset for next iteration
                selectedRoomsNumbers.clear();

            } catch (SQLException e) {
                System.err.println("Message: " + e.getMessage());
                System.err.println("There was an error creating the reservation. Please try again.");
            }
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

    /**
     * Checks if a given email string has a valid email format
     * @param email the email string
     * @return true if the email has a valid format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
