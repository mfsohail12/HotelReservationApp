package database.dao;

import database.Database;
import entities.Hotel;
import entities.RegularRoom;
import entities.Suite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ReservationDao {
    private ReservationDao() {}

    /**
     * Gets all hotels available in the Hotel Reservation Application with their average ratings
     * @return a list of hotels
     * @throws SQLException if there is an error executing the SQL queries
     */
    public static List<Hotel> getAllHotels() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();

        String query = """
            SELECT h.hotel_id, h.name, h.address, h.city, AVG(r.rating) AS avg_rating
            FROM Hotels h
            LEFT JOIN Reviews r ON h.hotel_id = r.hotel_id
            GROUP BY h.hotel_id, h.name, h.address, h.city
        """;

        Connection connection = Database.getConnection();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int hotelId = rs.getInt("hotel_id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int rating = rs.getInt("avg_rating");

                hotels.add(new Hotel(hotelId, name, address, city, rating));
            }
        }

        return hotels;
    }

    /**
     * Gets all the regular rooms from a given hotel that are available between the specified check-in and check-out dates
     * @param hotelId the id of the hotel
     * @param checkInDate the desired check-in date
     * @param checkOutDate the desired check-out date
     * @return a list of regular rooms
     * @throws SQLException if there is an error executing the SQL queries
     */
    public static List<RegularRoom> getRegularRooms(int hotelId, String checkInDate, String checkOutDate) throws SQLException {
        List<RegularRoom> regularRooms = new ArrayList<>();

        // We use LISTAGG to pull all amenities into a single string separated by commas
        String query = """
          SELECT
              reg.hotel_id,
              reg.room_number,
              reg.standard_category,
              r.price,
              r.bed_type,
              r.number_of_beds,
              r.size,
              LISTAGG(DISTINCT a.type, ', ') WITHIN GROUP(ORDER BY a.type) AS amenities
          FROM RegularRooms reg
          JOIN Rooms r
              ON reg.hotel_id = r.hotel_id
              AND reg.room_number = r.room_number
          LEFT JOIN RoomAmenities ra
              ON r.hotel_id = ra.hotel_id
              AND r.room_number = ra.room_number
          LEFT JOIN Amenities a
              ON ra.amenity_id = a.amenity_id
          WHERE reg.hotel_id = ?
          AND NOT EXISTS (
              SELECT 1
              FROM RoomBookings rb
              JOIN Reservations res ON rb.reservation_id = res.reservation_id
              WHERE rb.hotel_id = r.hotel_id
                AND rb.room_number = r.room_number
                AND res.check_in < CAST(? AS DATE)
                AND res.check_out > CAST(? AS DATE)
          )
          GROUP BY
              reg.hotel_id,
              reg.room_number,
              reg.standard_category,
              r.price,
              r.bed_type,
              r.number_of_beds,
              r.size;
         """;

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            // set params
            pstmt.setInt(1, hotelId);
            pstmt.setString(2, checkInDate);
            pstmt.setString(3, checkOutDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int roomNumber = rs.getInt("room_number");
                    int price = rs.getInt("price");
                    String standardCategory = rs.getString("standard_category");
                    String bedType = rs.getString("bed_type");
                    int numberOfBeds = rs.getInt("number_of_beds");
                    int size = rs.getInt("size");
                    String amenities = rs.getString("amenities");

                    RegularRoom room = new RegularRoom(roomNumber, price, bedType, numberOfBeds, size, amenities, standardCategory);
                    regularRooms.add(room);
                }
            }
        }

        return regularRooms;
    }

    /**
     * Gets all the suites from a given hotel that are available between the specified check-in and check-out dates
     * @param hotelId the id of the hotel
     * @param checkInDate the desired check-in date
     * @param checkOutDate the desired check-out date
     * @return a list of suites
     * @throws SQLException if there is an error executing the SQL queries
     */
    public static List<Suite> getSuites(int hotelId, String checkInDate, String checkOutDate) throws SQLException {
        List<Suite> suites = new ArrayList<>();

        // We use LISTAGG to pull all amenities and premium features into a single string separated by commas
        String query = """
          SELECT
              s.hotel_id,
              s.room_number,
              s.view_type,
              r.price,
              r.bed_type,
              r.number_of_beds,
              r.size,
              LISTAGG(DISTINCT a.type, ', ') WITHIN GROUP(ORDER BY a.type) AS amenities,
              LISTAGG(DISTINCT pf.feature_name, ', ') WITHIN GROUP(ORDER BY pf.feature_name) AS premium_features
          FROM Suites s
          JOIN Rooms r
              ON s.hotel_id = r.hotel_id
              AND s.room_number = r.room_number
          LEFT JOIN RoomAmenities ra
              ON r.hotel_id = ra.hotel_id
              AND r.room_number = ra.room_number
          LEFT JOIN Amenities a
              ON ra.amenity_id = a.amenity_id
          LEFT JOIN SuitePremiumFeatures spf
              ON s.hotel_id = spf.hotel_id
              AND s.room_number = spf.room_number
          LEFT JOIN PremiumFeatures pf
              ON spf.feature_name = pf.feature_name
          WHERE s.hotel_id = ?
          AND NOT EXISTS (
              SELECT 1
              FROM RoomBookings rb
              JOIN Reservations res ON rb.reservation_id = res.reservation_id
              WHERE rb.hotel_id = r.hotel_id
                AND rb.room_number = r.room_number
                AND res.check_in < CAST(? AS DATE)
                AND res.check_out > CAST(? AS DATE)
          )
          GROUP BY
              s.hotel_id,
              s.room_number,
              s.view_type,
              r.price,
              r.bed_type,
              r.number_of_beds,
              r.size;
         """;

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            // set params
            pstmt.setInt(1, hotelId);
            pstmt.setString(2, checkInDate);
            pstmt.setString(3, checkOutDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int roomNumber = rs.getInt("room_number");
                    int price = rs.getInt("price");
                    String viewType = rs.getString("view_type");
                    String bedType = rs.getString("bed_type");
                    int numberOfBeds = rs.getInt("number_of_beds");
                    int size = rs.getInt("size");
                    String amenities = rs.getString("amenities");
                    String premiumFeatures = rs.getString("premium_features");

                    Suite suite = new Suite(roomNumber, price, bedType, numberOfBeds, size, amenities, viewType, premiumFeatures);
                    suites.add(suite);
                }
            }
        }

        return suites;
    }
}
