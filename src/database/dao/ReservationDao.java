package database.dao;

import database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entities.Reservation;

public final class ReservationDao {
    private ReservationDao() {}

    /**
     * Gets all reservations for a given user email with hotel and room details
     * @param email the user's email
     * @return a list of ReservationInfo objects
     * @throws SQLException if there is an error executing the query
     */
    public static List<Reservation> getReservationsByEmail(String email) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();

        String query = """
            SELECT res.reservation_id, res.check_in, res.check_out, res.is_paid_completely,
                   h.name AS hotel_name, h.city,
                   rb.room_number, r.price, r.bed_type, r.number_of_beds
            FROM Reservations res
            JOIN RoomBookings rb ON res.reservation_id = rb.reservation_id
            JOIN Rooms r ON rb.hotel_id = r.hotel_id AND rb.room_number = r.room_number
            JOIN Hotels h ON rb.hotel_id = h.hotel_id
            WHERE res.email = ?
            ORDER BY res.check_in DESC
        """;

        Connection connection = Database.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                            rs.getInt("reservation_id"),
                            rs.getString("check_in"),
                            rs.getString("check_out"),
                            rs.getInt("is_paid_completely"),
                            rs.getString("hotel_name"),
                            rs.getString("city"),
                            rs.getInt("room_number"),
                            rs.getInt("price"),
                            rs.getString("bed_type"),
                            rs.getString("number_of_beds")
                    ));
                }
            }
        }

        return reservations;
    }

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
}