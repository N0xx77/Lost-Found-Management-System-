package model;

public abstract class Item {
    private final String item_name;
    private final String material;
    private final String shape;
    private final String brand;
    private final String descriptions;
    private final String status;
    private final int objectID;
    private final int colourID;
    private final int categoryID;

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
