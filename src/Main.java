import database_access_only.DBConnection;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import ui.StartPage;

public class Main {
    public static void main(String[] args) {
        try  {
            DBConnection.getConnection();
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println(e);
        }
        SwingUtilities.invokeLater(() -> {
            try {
                StartPage startPage = new StartPage();
                startPage.setVisible(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
}