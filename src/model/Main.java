package model;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // This ensures the UI is created on the correct thread (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}