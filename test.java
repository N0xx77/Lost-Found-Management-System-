
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class test {

    public static void main(String [] args){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");   
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
        }

        String url = "jdbc:mysql://localhost:3306/lost_and_found";
        String user = "root";
        String password = "root";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Successful connection"); 
        } catch (SQLException e) {
            System.out.println("Fail connection"); 
        }
    }
}
