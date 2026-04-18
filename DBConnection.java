import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/donation_db";
    private static final String USER = "root";
    private static final String PASS = "purval"; // Change this

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            return con;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found! Add the JAR to Libraries.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Connection failed! Check username/password.");
            e.printStackTrace();
            return null;
        }
    }
}