package model;
/** This class extends the Part class.
 It also has a machineID integer variable. */
public class InHouse extends Part{

    private int machineID;
    /** This is the class constructor for InHouse.
     @param id The unique ID for each object
     @param name The name of the part
     @param price The price of the part
     @param stock How many are in stock
     @param min The minimum stock allowed
     @param max The maximum stock allowed
     @param machineID This integer ID of the machine*/
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }
    /** Assigns the machine ID
     @param machineID The ID of the machine that made this part */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
    /** Gets the ID of the machine that made this part
     * @return Returns the ID of the machine that made this part */
    public int getMachineID() {
        return machineID;
    }
}
