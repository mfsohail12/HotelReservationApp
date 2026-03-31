package database.dao;

import database.Database;
import entities.Transportation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class TransportationDao {
    private TransportationDao() {}

    /**
     * Gets all the transportations methods near a given hotel
     * @return a list of transportations
     * @throws SQLException if there is an error executing the SQL queries
     */
    public static List<Transportation> getTransportByHotel(int hotelId) throws SQLException {
        List<Transportation> transportations = new ArrayList<>();

        String query = """
        SELECT t.transport_name, t.type, a.distance
        FROM AccessibleTransportation a
        JOIN Transportation t
            ON a.type = t.type 
           AND a.transport_name = t.transport_name
        WHERE a.hotel_id = """ + hotelId + 
        """
        \nORDER BY a.distance""";

        Connection connection = Database.getConnection();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("transport_name");
                String type = rs.getString("type");
                int distance = rs.getInt("distance");

                transportations.add(new Transportation(name, type, distance));
            }
        }

        return transportations;
    }
}
