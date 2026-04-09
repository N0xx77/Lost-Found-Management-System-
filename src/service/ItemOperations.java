package service;

import javax.swing.table.DefaultTableModel;
import model.*;

public interface ItemOperations {
    void insertLostItem(Lost_item item);
    void insertFoundItem(Found_item item);
    DefaultTableModel getGlobalFeed();
    DefaultTableModel getReportedItems(long userId);
    boolean deleteItem(Long item_id);
    void confirmMatch(long matchId);

}