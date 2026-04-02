public class Users {
    private String fname;
    private String lname;
    private long userID;

    public Users(String fname, String lname, long ID){
        this.fname = fname;
        this.lname = lname;
        this.userID = ID;
    }

    public String getName(){
        String name = this.fname + this.lname;
        return name;
    } 
    
    public long getID(){
        return this.userID;
    }

}
