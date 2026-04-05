

import database_access_only.DBConnection;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // This ensures the UI is created on the correct thread (Event Dispatch Thread)
        try {
            DBConnection.getConnection();
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println(e);
        }
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
}