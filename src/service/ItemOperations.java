package service;

import java.sql.ResultSet;
import model.Found_item;
import model.Lost_item;

public interface ItemOperations {
    void insertLostItem(Lost_item item);
    void insertFoundItem(Found_item item);
    ResultSet getGlobalFeed();
    void confirmMatch(long matchId);
}