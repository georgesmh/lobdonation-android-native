package nohad.com.bloodbank;

import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by G.e.O.r.G.e.S on 4/15/2017.
 */

public interface WebApiService {
    //Retrofit turns our institute WEB API into a Java interface.
    //i.e. http://localhost/api/institute/Students
    @GET("/lob/Donors")
    public void getDonor(Callback<List<Donor>> callback);

    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/institute/Students/1
    //Get student record base on ID
    @GET("/lob/Donors/{id}")
    public void getDonorById(@Path("id") Integer id,Callback<Donor> callback);

    //i.e. http://localhost/api/institute/Students/1
    //Delete student record base on ID
    @DELETE("/lob/Donors/{id}")
    public void deleteDonorById(@Path("id") Integer id,Callback<Donor> callback);

    //i.e. http://localhost/api/institute/Students/1
    //PUT student record and post content in HTTP request BODY
    @PUT("/lob/Donors/PutDonor")
    public void updateDonorById(@Query("id") Integer id,@Body Donor donor,Callback<Donor> callback);

    //i.e. http://localhost/api/institute/Students
    //Add student record and post content in HTTP request BODY
    @POST("/lob/Donors")
    public void addDonor(@Body Donor donor,Callback<Donor> callback);

    @GET("/lob/Donors/FindDonorByEmail")
    public void findDonorByEmail(@Query("id") String id, Callback<donorObjectResult> callback);


    @GET("/lob/Cities")
    public void getCities(Callback<List<City>> callback);

    @GET("/lob/Blood_Type")
    public void getBloodTypes(Callback<List<Blood_Type>> callback);

    @GET("/lob/Hospitals")
    public void getHospitals(Callback<List<Hospital>> callback);

    @GET("/lob/Cities/GetCityHospitals")
    public void getCityHospitals(@Query("cityId") int cityId,Callback<List<Hospital>> callback);

}
