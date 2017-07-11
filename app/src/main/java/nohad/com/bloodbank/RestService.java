package nohad.com.bloodbank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nohad.com.bloodbank.WebApiService;
import retrofit.converter.GsonConverter;

/**
 * Created by Tan on 7/2/2015.
 */
public class RestService {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different URL than we have here
    private static final String URL = "http://api.lobdonation.com/";
//    private static final String URL = "http://192.168.1.108:11094/";

//    private static final String URL = "http://175.175.50.103/api/";

    private retrofit.RestAdapter restAdapter;
    private WebApiService apiService;

    public RestService()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();


        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = restAdapter.create(WebApiService.class);
    }

    public WebApiService getService()
    {
        return apiService;
    }
}