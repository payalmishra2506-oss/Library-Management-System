import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static final String URL = "jdbc:mysql://localhost:3306/librarydb";
    static final String USER = "root";
    static final String PASSWORD = "b1X#4jW*2f";  

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected successfully!");
            return con;
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();   // VERY IMPORTANT
            return null;
        }
    }
}
