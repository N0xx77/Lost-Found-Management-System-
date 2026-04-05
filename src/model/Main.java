package model;

import database_access_only.DBConnection;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // This ensures the UI is created on the correct thread (Event Dispatch Thread)
        try {
            DBConnection.getConnection();
        } catch (SQLException e) {
            System.out.println(e);
        }
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}