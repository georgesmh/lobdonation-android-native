package nohad.com.bloodbank.Activities;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import nohad.com.bloodbank.Blood_Type;
import nohad.com.bloodbank.City;
import nohad.com.bloodbank.Donor;
import nohad.com.bloodbank.Hospital;
import nohad.com.bloodbank.MultiSelectionSpinner;
import nohad.com.bloodbank.R;
import nohad.com.bloodbank.RestService;
import nohad.com.bloodbank.donorObjectResult;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import static nohad.com.bloodbank.R.id.input_address;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    Button btnSignUp;
    ToggleButton tbtnStatus;
    CheckBox ckOrgan;
    Spinner bloodTypesSpinner, citiesSpinner, hospitalsSpinner;
    EditText tvFName, tvMName, tvLName, tvEmail, tvPhone;
    String name1, name2, name3, email;
    private GoogleApiClient mGoogleApiClient;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText fromDateEtxt;
    RestService restService;
    private boolean isRegistered = false;


    private int registeredDonorId = -1;
    List<Hospital> allHospitals = new ArrayList<Hospital>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        bloodTypesSpinner = (Spinner) findViewById(R.id.bloodTypesSpinner);
        citiesSpinner = (Spinner) findViewById(R.id.citiesSpinner);
        hospitalsSpinner = (Spinner) findViewById(R.id.hospitalsSpinner);
        tbtnStatus = (ToggleButton) findViewById(R.id.toggleButton);
        ckOrgan = (CheckBox) findViewById(R.id.organdonor);
        tbtnStatus.setChecked(true);
        ckOrgan.setChecked(false);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        // Get cities:::
        restService = new RestService();
        restService.getService().getCities(new Callback<List<City>>() {
            @Override
            public void success(List<City> cities, Response response) {

                ArrayAdapter<City> dataAdapter = new ArrayAdapter<City>(SignupActivity.this, android.R.layout.simple_spinner_item, cities);

                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                citiesSpinner.setAdapter(dataAdapter);


            }

            @Override
            public void failure(RetrofitError error) {
                int y = 69;
            }
        });

        // Get Blood Types:::

        restService.getService().getBloodTypes(new Callback<List<Blood_Type>>() {
            @Override
            public void success(List<Blood_Type> bloodTypes, Response response) {

                ArrayAdapter<Blood_Type> dataAdapter = new ArrayAdapter<Blood_Type>(SignupActivity.this, android.R.layout.simple_spinner_item, bloodTypes);

                dataAdapter
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                bloodTypesSpinner.setAdapter(dataAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                int y = 69;
            }
        });

        // Get Hospitals :::

        restService.getService().getHospitals(new Callback<List<Hospital>>() {
            @Override
            public void success(List<Hospital> hospitals, Response response) {

//                ArrayAdapter<Hospital> dataAdapter = new ArrayAdapter<Hospital>(SignupActivity.this, android.R.layout.simple_spinner_item, hospitals);
//
//                dataAdapter
//                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                // attaching data adapter to spinner
//                hospitalsSpinner.setAdapter(dataAdapter);
                allHospitals = hospitals;
                List<String> hospitalsStr = new ArrayList<String>();

                for(Hospital item:hospitals){

                    hospitalsStr.add(item.toString());
                }


                MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.hospitalsSpinner);

                multiSelectionSpinner.setItems(hospitalsStr);
                multiSelectionSpinner.setListener(SignupActivity.this);
            }

            @Override
            public void failure(RetrofitError error) {
                int y = 69;
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Signup");
        setSupportActionBar(myToolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        String email = this.getIntent().getStringExtra("email");

        fromDateEtxt = (EditText) findViewById(R.id.input_date);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        tvFName = (EditText) findViewById(R.id.input_first_name);
        tvMName = (EditText) findViewById(R.id.input_middle_name);
        tvLName = (EditText) findViewById(R.id.input_last_name);
        tvEmail = (EditText) findViewById(R.id.input_email);
        tvPhone = (EditText) findViewById(R.id.input_mobile);

        setDateTimeField();
        tvEmail.setText(email);
        restService = new RestService();
        // Check if the user exists or not in donors table:
        restService.getService().findDonorByEmail(email, new Callback<donorObjectResult>() {
            @Override
            public void success(donorObjectResult donorRes, Response response) {
                if (donorRes != null) {
                    isRegistered = true;
                    registeredDonorId = donorRes.donor.Donor_Id;
                    // Get Data:
                    tvFName.setText(donorRes.donor.First_Name);
                    tvLName.setText(donorRes.donor.Last_Name);
                    tvMName.setText(donorRes.donor.Middle_Name);
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formatted = df.format(donorRes.donor.DOB);
                    fromDateEtxt.setText(formatted);
                    tvPhone.setText(donorRes.donor.Mobile_Number);
                    tbtnStatus.setChecked(donorRes.donor.Status);
                    ckOrgan.setChecked(donorRes.donor.isOrganDonor);
                    Spinner bloodTypeSpinner = (Spinner) findViewById(R.id.bloodTypesSpinner);
                    bloodTypeSpinner.setSelection(getIndex(bloodTypeSpinner, donorRes.bloodTypeName));

                    Spinner citiesSpinner = (Spinner) findViewById(R.id.citiesSpinner);
                    citiesSpinner.setSelection(getIndex(citiesSpinner, donorRes.cityName));

                    donorRes.donor.Donor_Id = registeredDonorId;


                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    //Toast.makeText(SignupActivity.this, refreshedToken, Toast.LENGTH_LONG).show();
                    Donor donor = new Donor();
                    donor.First_Name = tvFName.getText().toString();
                    donor.Last_Name = tvLName.getText().toString();
                    donor.Middle_Name = tvMName.getText().toString();
                    donor.Status = tbtnStatus.isChecked();
                    donor.isOrganDonor = ckOrgan.isChecked();
                    String s = fromDateEtxt.getText().toString();
                    try {
                        donor.DOB = new SimpleDateFormat("dd-MM-yyyy").parse(s);//fromDateEtxt.getText().toString());
                    } catch (ParseException e) {
                        donor.DOB = null;
                    }
                    //donor.isDead = false;
                    donor.Mobile_Number = tvPhone.getText().toString();

                    Blood_Type bt = (Blood_Type) ( (Spinner) findViewById(R.id.bloodTypesSpinner) ).getSelectedItem();

                    City city = (City) ( (Spinner) findViewById(R.id.citiesSpinner) ).getSelectedItem();

                    donor.Blood_Type = bt;
                    donor.City = city;

                    MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.hospitalsSpinner);

                    List<String> selectedHospitalsStr = multiSelectionSpinner.getSelectedStrings();
                    List<Hospital> selectedHospitals = new ArrayList<Hospital>();

                    for(String element:selectedHospitalsStr){

                        for(Hospital hos:allHospitals){
                            if(hos.Name.equals(element)){
                                selectedHospitals.add(hos);
                                break;
                            }
                        }
                    }
                    donor.Hospitals = selectedHospitals;
                    donor.Blood_Type_Id = bt.Blood_Type_Id;
                    donor.City_Id = city.City_Id;
                    donor.email = tvEmail.getText().toString();
                    donor.Token = refreshedToken;
                    donor.Donor_Id = registeredDonorId;

                    restService.getService().updateDonorById(registeredDonorId, donor, new Callback<Donor>() {
                        @Override
                        public void success(Donor donor, Response response) {
                           // Toast.makeText(SignupActivity.this, "Donor Updated Successfully!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(SignupActivity.this, "Could not load Donor", Toast.LENGTH_LONG).show();

                        }
                    });


                    btnSignUp.setText("Update");


                } else {
                    isRegistered = false;

                }
            }

            @Override
            public void failure(RetrofitError error) {
int y= 7;
            }
        });

        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                City city = (City) ( (Spinner) findViewById(R.id.citiesSpinner) ).getSelectedItem();
                int cityId = city.City_Id;
                restService.getService().getCityHospitals(cityId, new Callback<List<Hospital>>() {
                    @Override
                    public void success(List<Hospital> hospitals, Response response) {

                        MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.hospitalsSpinner);

                        List<String> selectedHospitals = new ArrayList<String>();

                        for(Hospital item: hospitals){
                            selectedHospitals.add(item.Name);
                        }
                        multiSelectionSpinner.setSelection(selectedHospitals);


                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                //Toast.makeText(SignupActivity.this, refreshedToken, Toast.LENGTH_LONG).show();
                Donor donor = new Donor();
                donor.First_Name = tvFName.getText().toString();
                donor.Last_Name = tvLName.getText().toString();
                donor.Middle_Name = tvMName.getText().toString();
                donor.Status = tbtnStatus.isChecked();
                donor.isOrganDonor = ckOrgan.isChecked();
                String s = fromDateEtxt.getText().toString();
                try {
                    donor.DOB = new SimpleDateFormat("dd-MM-yyyy").parse(s);//fromDateEtxt.getText().toString());
                } catch (ParseException e) {
                    donor.DOB = null;
                }
                //donor.isDead = false;
                donor.Mobile_Number = tvPhone.getText().toString();

                Blood_Type bt = (Blood_Type) ( (Spinner) findViewById(R.id.bloodTypesSpinner) ).getSelectedItem();

                City city = (City) ( (Spinner) findViewById(R.id.citiesSpinner) ).getSelectedItem();

                donor.Blood_Type = bt;
                donor.City = city;

                MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.hospitalsSpinner);

                List<String> selectedHospitalsStr = multiSelectionSpinner.getSelectedStrings();
                List<Hospital> selectedHospitals = new ArrayList<Hospital>();

               for(String element:selectedHospitalsStr){

                   for(Hospital hos:allHospitals){
                       if(hos.Name.equals(element)){
                           selectedHospitals.add(hos);
                           break;
                       }
                   }
               }
               donor.Hospitals = selectedHospitals;
                donor.Blood_Type_Id = bt.Blood_Type_Id;
                donor.City_Id = city.City_Id;
                donor.email = tvEmail.getText().toString();
                donor.Token = refreshedToken;
                if (!isRegistered) {
                    restService.getService().addDonor(donor, new Callback<Donor>() {
                        @Override
                        public void success(Donor donor, Response response) {

                            Toast.makeText(SignupActivity.this, "Donor Created!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(SignupActivity.this, "Donor Creation Failed!!", Toast.LENGTH_LONG).show();


                        }

                    });

                } else {
                    donor.Donor_Id = registeredDonorId;
                    restService.getService().updateDonorById(registeredDonorId, donor, new Callback<Donor>() {
                        @Override
                        public void success(Donor donor, Response response) {
                            Toast.makeText(SignupActivity.this, "Donor Updated Successfully!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(SignupActivity.this, "Donor Not Updated!", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });
    }


    private void setDateTimeField() {

        fromDateEtxt.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                signUp();
                break;
            case R.id.input_date:
                fromDatePickerDialog.show();
        }
    }

    private void signUp() {
        //insert in database
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        //Toast.makeText(this, strings.toString(), Toast.LENGTH_LONG).show();
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().trim().equals(myString.trim())){
                index = i;
            }
        }
        return index;
    }


//    private void signout() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // [START_EXCLUDE]
//                        // [END_EXCLUDE]
//                    }
//                });
//    }



}
