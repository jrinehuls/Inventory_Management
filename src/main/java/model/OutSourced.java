package model;
/** This class extends the Part class.
 It also has a companyName String variable. */
public class OutSourced extends Part{

    private String companyName;
    /** This is the class constructor for InHouse.
     @param id The unique ID for each object
     @param name The name of the part
     @param price The price of the part
     @param stock How many are in stock
     @param min The minimum stock allowed
     @param max The maximum stock allowed
     @param companyName The name of the company the part was outsourced to*/
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /** Assigns the company name
     @param companyName The name of the company that made this part */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    /** Gets the name of the company that made this part
     * @return Returns the name of the company that made this part */
    public String getCompanyName(){
        return companyName;
    }
}
