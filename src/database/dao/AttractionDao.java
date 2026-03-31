package database.dao;

import database.Database;
import entities.Attraction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class AttractionDao {
    private AttractionDao() {}

    /**
     * Gets all attractions near a given hotel
     * @return a list of attractions
     * @throws SQLException if there is an error executing the SQL queries
     */
    public static List<Attraction> getAttractionsByHotel(int hotelId) throws SQLException {
        List<Attraction> attractions = new ArrayList<>();

        String query = """
        SELECT a.attraction_id, a.attraction_name, a.address, a.rating, s.distance
        FROM SurroundingAttractions s
        JOIN Attractions a 
            ON s.attraction_id = a.attraction_id
        WHERE s.hotel_id = """ + hotelId;

        Connection connection = Database.getConnection();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int attractionId = rs.getInt("attraction_id");
                String name = rs.getString("attraction_name");
                String address = rs.getString("address");
                int rating = rs.getInt("rating");
                int distance = rs.getInt("distance");

                attractions.add(new Attraction(attractionId, name, address, rating, distance));
            }
        }

        return attractions;
    }
}
