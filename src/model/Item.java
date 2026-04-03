package model;

public abstract class Item {
    private String item_name;
    private String material;
    private String shape;
    private String brand;
    private String descriptions;
    private String status;
    private int objectID;
    private int colourID;
    private int categoryID;

    protected Item(String iname, String mat, String shape, String brand, String descrip, String status, int objID, int colID, int catID){
        this.item_name = iname;
        this.material = mat;
        this.shape = shape;
        this.brand = brand;
        this.descriptions = descrip;
        this.status = status;
        this.objectID = objID;
        this.colourID = colID;
        this.categoryID = catID;
    }

    public String getItemName() {
        return item_name;
    }

    public String getMaterial() {
        return material;
    }

    public String getShape() {
        return shape;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public String getStatus() {
        return status;
    }

    public int getObjectID() {
        return objectID;
    }

    public int getColourID() {
        return colourID;
    }

    public int getCategoryID() {
        return categoryID;
    }

}
