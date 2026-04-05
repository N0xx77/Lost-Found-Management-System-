package model;

public class Users {
    private final String fname;
    private final String lname;
    private final long userID;

    public Users(String fname, String lname, long ID){
        this.fname = fname;
        this.lname = lname;
        this.userID = ID;
    }

    public String getName(){
        String name = this.fname + " " + this.lname;
        return name;
    } 
    
    public long getID(){
        return this.userID;
    }

}
