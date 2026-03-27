package Submenus;

import java.util.*;
import java.sql.*;
import Colors.ConsoleColors;
import Database.Database;

public class MakeReservationMenu implements Submenu {
    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Displaying menu info
            System.out.println(ConsoleColors.ANSI_PURPLE + "[MAKE RESERVATION MENU]" + ConsoleColors.ANSI_RESET);
            System.out.println("Follow the instructions below to make a reservation at a hotel.");

            // Displaying hotel options


            int input = scanner.nextInt();

            if (input == 0) return;

            // Temporary testing code to check if DB connection works
            if (input == 999) {
                try {
                    Connection c = Database.getConnection();

                    Statement statement = c.createStatement();

                    String querySQL = "SELECT * from Users WHERE NAME = \'Stefan Dow\'";
                    java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;

                    while ( rs.next ( ) )
                    {
                        String email = rs.getString(1);
                        String name = rs.getString (2);
                        String DOB = rs.getString(3);
                        System.out.println ("email:  " + email);
                        System.out.println ("name:  " + name);
                        System.out.println ("DOB:  " + DOB);
                    }

                    statement.close();
                    System.out.println ("DONE");
                } catch (SQLException e)
                {
                    int sqlCode = e.getErrorCode(); // Get SQLCODE
                    String sqlState = e.getSQLState(); // Get SQLSTATE

                    // Your code to handle errors comes here;
                    // something more meaningful than a print would be good
                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    System.out.println(e);
                }

            }
        }
    }
}
