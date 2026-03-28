package database.dao;

import database.Database;

import java.sql.*;

public class ReservationDao {

    /**
     * Calls the UpgradeRoom procedure given the reservation ID, the old room number and the hotel ID
     * @throws SQLException if there is an error executing the SQL procedure
     */
    public static void upgradeRoom(int reservationId, int oldRoomNumber, int hotelId) throws SQLException {
        String sql = "{CALL UpgradeRoom(?, ?, ?)}";

        Connection conn = Database.getConnection();
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, reservationId);
            cs.setInt(2, oldRoomNumber);
            cs.setInt(3, hotelId);

            // Call the procedure
            cs.execute();

            String checkQuery = """
                SELECT COUNT(*) 
                FROM RoomBookings RB
                JOIN Suites S
                  ON RB.hotel_id = S.hotel_id 
                 AND RB.room_number = S.room_number
                WHERE RB.reservation_id = ?
            """;

            PreparedStatement ps = conn.prepareStatement(checkQuery);
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