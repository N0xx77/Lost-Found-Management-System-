
public class Found_item extends Item {

    private String found_location;
    private java.sql.Date found_date;
    private long user_id;
    private long item_id;

    public Found_item(String iname, String mat, String shape, String brand, String descrip, String status, int objID, int colID, int catID, String found_location, java.sql.Date found_date, long user_id, long item_id) {
        super(iname, mat, shape, brand, descrip, status, objID, colID, catID);
        this.found_location = found_location;
        this.found_date = found_date;
        this.user_id = user_id;
        this.item_id = item_id;
    }

    public String getFoundLocation() {
        return found_location;
    }

    public java.sql.Date getFoundDate() {
        return found_date;
    }

    public long getUserId() {
        return user_id;
    }

    public long getItemId() {
        return item_id;
    }

}
