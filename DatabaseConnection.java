package cashier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Establishes a connection to the team_phoenix database to allow use of SQL
 * queries.
 * <p>
 * Hosts the single method connect() to connect to the team phoenix database
 * <p>
 * 
 * <p>
 * ===========================
 * TABLE OF CONTENTS
 * ===========================
 * <ol>
 * 
 * <li>
 * ManagementUI Menu
 * </ul>
 * <li>{@link #connect()}
 * </ul>
 * 
 * </ol>
 * 
 * @author Rene Almeida
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://csce-315-db.engr.tamu.edu/team_phoenix_db";
    private static final String USER = "team_phoenix";
    private static final String PASSWORD = "123";

    /**
     * Establishes a connection to the team__phoenix database using its URL,
     * username, and password
     * 
     * @return connection to the database
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}