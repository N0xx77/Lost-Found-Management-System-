package ui;

import database_access_only.DataBaseHandler;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

public class ItemMatchesFrame extends JFrame{
    private final Users currentUser;
    private final DataBaseHandler dbHandler;
    private DefaultTableModel matchesModel;
    private JTable matchesTable; 


    private long foundItemId;

    public ItemMatchesFrame(Users u, long foundId) {
        this.currentUser = u;
        this.foundItemId = foundId;
        this.dbHandler = new DataBaseHandler();

        setTitle("SIT Lost & Found - Potential Matches"); // Changed from "Dashboard"
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(0, 51, 102));
        JLabel welcomeLbl = new JLabel("Logged in as: " + currentUser.getName());
        welcomeLbl.setForeground(Color.WHITE);
        header.add(welcomeLbl);
        add(header, BorderLayout.NORTH);

        //Getting data from SQL via DBHandler function and putting into tables
        matchesModel = dbHandler.getMatchesForFoundItem(foundItemId);
        matchesTable = new JTable(matchesModel);

        //Main screen & initialisation
        JPanel matchesPanel = new JPanel(new BorderLayout(10, 10));
        JScrollPane scrollPane = new JScrollPane(matchesTable);
        matchesPanel.add(scrollPane, BorderLayout.CENTER);

        //Footer (Buttons)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmBtn = new JButton("Confirm & Return Item");
        JButton backBtn = new JButton("Back to Dashboard");
        confirmBtn.setBackground(new Color(0, 153, 76));
        confirmBtn.setForeground(Color.WHITE);

        footer.add(confirmBtn);
        footer.add(backBtn);
        matchesPanel.add(footer, BorderLayout.SOUTH);


        add(matchesPanel, BorderLayout.CENTER);

        //Confirm Button part:
        backBtn.addActionListener(e -> {
            this.dispose();
        });

        confirmBtn.addActionListener(e -> {
            int selectedRow = matchesTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = matchesTable.convertRowIndexToModel(selectedRow);
                long matchId = (long) matchesTable.getModel().getValueAt(modelRow, 0);

                dbHandler.confirmMatch(matchId);
                JOptionPane.showMessageDialog(this, "Confirmation sent! The item status is now 'Returned'.");
                refreshMatchesTable();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a match from the table first.");
            }
        });

        refreshMatchesTable();
        setVisible(true);
    }

    private void refreshMatchesTable(){
        matchesModel = dbHandler.getMatchesForFoundItem(foundItemId);
        matchesTable.setModel(matchesModel);

        if (matchesTable.getColumnCount() > 0) {
            matchesTable.removeColumn(matchesTable.getColumnModel().getColumn(0));
        }
        
        matchesTable.revalidate();
        matchesTable.repaint();
    }
    
}
