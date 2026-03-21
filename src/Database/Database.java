package Database;

import java.sql.*;

public class Database {
    private static Connection connection;

    /**
     * Creates a connection to the database.
     * Safe to call multiple times: subsequent calls are no-ops after the first successful init.
     * @param userid SOCSUSER value for connecting to database server
     * @param password SOCSPASSWD value for connecting to database server
     * @throws SQLException if there is an error connecting to the database
     */
    public static synchronized void init(String userid, String password) throws SQLException, IllegalArgumentException {
        if (connection != null) return;

        String url = "jdbc:db2://winter2026-comp421.cs.mcgill.ca:50000/comp421";

        if (userid == null || password == null) throw new IllegalArgumentException("userid and password are required");

        connection = DriverManager.getConnection (url,userid,password) ;
    }

    /**
     * Closes connection to the database
     * @throws SQLException if there is an error closing the connection to the database
     */
    public static synchronized void close() throws SQLException {
        if (connection == null) return;
        connection.close();
    }

    /**
     * @return the connection to the database
     * @throws IllegalStateException if the database connection has not been initialized first
     */
    public static Connection getConnection() throws IllegalStateException {
        if (connection == null) {
            throw new IllegalStateException("Database.Database has not been initialized yet! Database.Database.init() must be called first.");
        }
        return connection;
    }
}
