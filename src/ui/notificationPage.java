package ui;

import database_access_only.DataBaseHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Users;
import model.notifications;
import java.awt.*;

public class notificationPage extends JPanel {
    private final DataBaseHandler dbHandler;
    private final Users currentUser;
    private final DefaultTableModel lostModel;
    private final DefaultTableModel foundModel;

    public notificationPage(Users user, DataBaseHandler db) {
        this.currentUser = user;
        this.dbHandler = db;

        setLayout(new BorderLayout());
        JTabbedPane subTabs = new JTabbedPane();

        lostModel = createReadOnlyTableModel(new String[]{"Message", "Date"});
        foundModel = createReadOnlyTableModel(new String[]{"Message", "Date"});

        subTabs.addTab("Lost Item Updates", new JScrollPane(new JTable(lostModel)));
        subTabs.addTab("Found Item Pings", new JScrollPane(new JTable(foundModel)));

        JButton refreshBtn = new JButton("Refresh Notifications");
        refreshBtn.addActionListener(e -> loadData());

        add(subTabs, BorderLayout.CENTER);
        add(refreshBtn, BorderLayout.SOUTH);

        loadData();
    }

    private DefaultTableModel createReadOnlyTableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void loadData() {
        lostModel.setRowCount(0);
        foundModel.setRowCount(0);

        java.util.List<notifications> lostNotifs = dbHandler.getNotificationsForUser(currentUser.getID(), "LOST");
        for (notifications n : lostNotifs) {
            lostModel.addRow(new Object[]{n.getMessage(), n.getSentDate()});
        }

        java.util.List<notifications> foundNotifs = dbHandler.getNotificationsForUser(currentUser.getID(), "FOUND");
        for (notifications n : foundNotifs) {
            foundModel.addRow(new Object[]{n.getMessage(), n.getSentDate()});
        }
    }
}