package nohad.com.bloodbank;

import java.util.Date;
import java.util.List;

import nohad.com.bloodbank.Activities.SignupActivity;

/**
 * Created by G.e.O.r.G.e.S on 4/15/2017.
 */

public class Donor {
    public int Donor_Id;
    public String First_Name;
    public String Middle_Name;
    public String Last_Name;
    public String Mobile_Number;
    public boolean Status;
    public int Blood_Type_Id;
    public int City_Id;
    public Date DOB;
    public boolean isDead;
    public boolean isOrganDonor;
    public Blood_Type Blood_Type;
    public City City;
    public String email;
    public List<Hospital> Hospitals;
    public String Token;
}
