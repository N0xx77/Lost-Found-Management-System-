package model;

public class Lost_item extends Item{
    private final String lost_location;
    private final java.sql.Date lost_date;
    private final long user_id;
    private final long item_id;

    public Lost_item(String iname, String mat, String shape, String brand, String descrip, String status, int objID, int colID, int catID, String lost_location, java.sql.Date lost_date, long user_id, long item_id) {
        super(iname, mat, shape, brand, descrip, status, objID, colID, catID);
        this.lost_location = lost_location;
        this.lost_date = lost_date;
        this.user_id = user_id;
        this.item_id = item_id;
    }

    public String getLostLocation() {
        return lost_location;
    }

    public java.sql.Date getLostDate() {
        return lost_date;
    }

    public long getUserId() {
        return user_id;
    }

    public long getItemId() {
        return item_id;
    }
}