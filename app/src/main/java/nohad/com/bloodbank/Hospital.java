package nohad.com.bloodbank;

/**
 * Created by G.e.O.r.G.e.S on 4/24/2017.
 */

public class Hospital {
    public int Hospital_Id;
    public String Name;
    public String Address;
    public String Phone_Number;
    public int ind; // index of hospital in global hospital db table

    @Override
    public String toString(){
        return this.Name;
    }

}
