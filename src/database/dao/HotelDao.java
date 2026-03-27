package database.dao;

import database.Database;
import entities.Hotel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class HotelDao {
    private HotelDao() {}

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
}
