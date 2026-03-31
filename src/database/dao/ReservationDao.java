package database.dao;

import database.Database;
import java.sql.*;
import java.time.LocalDate;

public class ReservationDao {
    /**
     * Calls the UpgradeRoom procedure given the reservation ID, the old room number and the hotel ID
     * @throws SQLException if there is an error executing the SQL procedure
     */
    public static void upgradeRoom(int reservationId, int oldRoomNumber, int hotelId) throws SQLException {
        Connection conn = Database.getConnection();

        // Validate that the room belongs to the reservation
        String validateQuery = """
            SELECT COUNT(*)
            FROM RoomBookings
            WHERE reservation_id = ?
              AND room_number = ?
              AND hotel_id = ?
        """;

        try (PreparedStatement validatePs = conn.prepareStatement(validateQuery)) {
            validatePs.setInt(1, reservationId);
            validatePs.setInt(2, oldRoomNumber);
            validatePs.setInt(3, hotelId);

            ResultSet validateRs = validatePs.executeQuery();
            validateRs.next();

            if (validateRs.getInt(1) == 0) {
                System.out.println("Error: This room is not part of your reservation.");
                return;
            }
        }

        // Call stored procedure
        String sql = "{CALL UpgradeRoom(?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, reservationId);
            cs.setInt(2, oldRoomNumber);
            cs.setInt(3, hotelId);

            cs.execute();
        }

        // Check if upgrade succeeded
        String checkQuery = """
            SELECT COUNT(*) 
            FROM RoomBookings RB
            JOIN Suites S
              ON RB.hotel_id = S.hotel_id 
             AND RB.room_number = S.room_number
            WHERE RB.reservation_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setInt(1, reservationId);

            ResultSet rs = ps.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                System.out.println("Upgrade successful!");
            } else {
                System.out.println("Upgrade failed: no suites available.");
            }
        }
    }

    /**
     * Gets the Hotel Id number based off the user's reservation Id from the RoomBooking table
     * @return the hoteld id as an int
     * @throws SQLException if there is an error executing the SQL queries
     */
    public static int getHotelIdFromResId(int reservationId) throws SQLException {
        String query = """
            SELECT hotel_id
            FROM RoomBookings
            WHERE reservation_id = ?
            FETCH FIRST 1 ROW ONLY
        """;

        Connection conn = Database.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("hotel_id");
                } else {
                    throw new SQLException("No hotel found for reservation ID: " + reservationId);
                }
            }
        }
    }

    /**
     * Inserts a new reservation into the database
     * @param email the user's email
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return the generated reservation_id
     * @throws SQLException if there is an error executing the SQL query
     */
    public static int insertReservation(String email, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        // Generate the next reservation ID
        int reservationId = getNextReservationId();

        String query = "INSERT INTO Reservations (reservation_id, email, check_in, check_out, is_paid_completely) VALUES (?, ?, ?, ?, 0)";

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            pstmt.setString(2, email);
            pstmt.setDate(3, Date.valueOf(checkInDate));
            pstmt.setDate(4, Date.valueOf(checkOutDate));

            pstmt.executeUpdate();
            return reservationId;
        }
    }

    /**
     * Inserts a room booking into the database for a given reservation
     * @param reservationId the reservation ID
     * @param hotelId the hotel ID
     * @param roomNumber the room number
     * @throws SQLException if there is an error executing the SQL query
     */
    public static void insertRoomBooking(int reservationId, int hotelId, int roomNumber) throws SQLException {
        String query = "INSERT INTO RoomBookings (reservation_id, hotel_id, room_number) VALUES (?, ?, ?)";

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            pstmt.setInt(2, hotelId);
            pstmt.setInt(3, roomNumber);

            pstmt.executeUpdate();
        }
    }

    /**
     * Inserts a payment into the database
     * @param reservationId the reservation ID
     * @param amount the payment amount
     * @param method the payment method (Credit, Debit, or PayPal)
     * @param email the user's email
     * @throws SQLException if there is an error executing the SQL query
     */
    public static void insertPayment(int reservationId, int amount, String method, String email) throws SQLException {
        // Generate the next payment ID
        int paymentId = getNextPaymentId();

        String query = "INSERT INTO Payments (payment_id, reservation_id, amount, method, email) VALUES (?, ?, ?, ?, ?)";

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, paymentId);
            pstmt.setInt(2, reservationId);
            pstmt.setInt(3, amount);
            pstmt.setString(4, method);
            pstmt.setString(5, email);

            pstmt.executeUpdate();
        }
    }

    /**
     * Generates the next reservation ID by finding the max existing ID and incrementing
     * @return the next available reservation_id
     * @throws SQLException if there is an error executing the SQL query
     */
    private static int getNextReservationId() throws SQLException {
        String query = "SELECT MAX(reservation_id) FROM Reservations";
        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return maxId + 1;
            }
        }
        return 1;
    }

    /**
     * Generates the next payment ID by finding the max existing ID and incrementing
     * @return the next available payment_id
     * @throws SQLException if there is an error executing the SQL query
     */
    private static int getNextPaymentId() throws SQLException {
        String query = "SELECT MAX(payment_id) FROM Payments";
        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return maxId + 1;
            }
        }
        return 1;
    }
}