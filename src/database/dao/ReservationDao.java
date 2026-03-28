package database.dao;

import database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ReservationDao {
    private ReservationDao() {}

    /**
     * Represents a reservation with its hotel and room details
     */
    public static class ReservationInfo {
        public int reservationId;
        public String checkIn;
        public String checkOut;
        public int isPaid;
        public String hotelName;
        public String city;
        public int roomNumber;
        public int price;
        public String bedType;
        public String numBeds;

        public ReservationInfo(int reservationId, String checkIn, String checkOut, int isPaid, String hotelName, String city, int roomNumber, int price, String bedType, String numBeds) {
            this.reservationId = reservationId;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.isPaid = isPaid;
            this.hotelName = hotelName;
            this.city = city;
            this.roomNumber = roomNumber;
            this.price = price;
            this.bedType = bedType;
            this.numBeds = numBeds;
        }

        public String getPaidStatus() {
            return (isPaid == 1) ? "Paid" : "Not Fully Paid";
        }
    }

    /**
     * Gets all reservations for a given user email with hotel and room details
     * @param email the user's email
     * @return a list of ReservationInfo objects
     * @throws SQLException if there is an error executing the query
     */
    public static List<ReservationInfo> getReservationsByEmail(String email) throws SQLException {
        List<ReservationInfo> reservations = new ArrayList<>();

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
                    reservations.add(new ReservationInfo(
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
}