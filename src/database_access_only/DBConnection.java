package database_access_only;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/lost_and_found";
        String user = "root";
        String password = System.getenv("DB_PASSWORD");
        if(password == null || password.isBlank()){
            throw new SQLException("DB_PASSWORD is missing! Check your .env in util folder and launch.json envFile.");
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found!");
        }
        
    }
}
