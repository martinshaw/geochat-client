/*
 * RegistrationActivity
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/RegistrationActivity.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 06:23 <martin>
 * Last Compilation: 23/05/18 06:23
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity implements
        RegistWelcomeinfoFragment1.OnFragmentInteractionListener,
        RegistWelcomeinfoFragment2.OnFragmentInteractionListener,
        RegistWelcomeinfoFragment3.OnFragmentInteractionListener,
        RegistWelcomeinfoFragment4.OnFragmentInteractionListener{


    /**
     * The number of pages to be expected for Welcome Information Display
     */
    private static final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    BottomSheetBehavior bottomSheetBehavior;
    SharedPreferences prefs;
    Retrofit retrofit;
    GeochatAPIService service;

    Button mAPIURLSettingDialog;
//    ImageView mAPIURLSettingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        prefs = this.getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);


        // Checks for network connection. If none found, force quit application
        networkCheck();


        // Setup API Service
        /*
        *  MOVE API SETUP CODE FROM API URL DIALOG EVENT TO HERE IN PRODUCTION
        * */
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(prefs.getString("apiUrl", "http://192.169.159.139:8001"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GeochatAPIService.class);

        // Setup and configure bottom sheet for user registration
        setupRegistrationBottomSheet(this);

        // Instantiate a ViewPager and a PagerAdapter for the Welcome Information Display
        mPager = (ViewPager) findViewById(R.id.regist_welcomeinfo_container);
        mPagerAdapter = new WelcomeinfoPagerAdaptor(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // Setup API URL setting dialog
        mAPIURLSettingDialog = (Button) findViewById(R.id.regist_bottomsheet_setting_button);
//        mAPIURLSettingDialog = (ImageView) findViewById(R.id.regist_welcomeinfo_1_logo);
        mAPIURLSettingDialog.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_LONG).show();

                final EditText txtUrl = new EditText(getApplicationContext());
                txtUrl.setHint("http://192.168.159.139:8001");

                new android.support.v7.app.AlertDialog.Builder(RegistrationActivity.this)
                    .setTitle("API URL Setting")
                    .setMessage("Type the URL which you would like the app to use to locate the API server")
                    .setView(txtUrl)
                    .setPositiveButton("Change Setting", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String url = txtUrl.getText().toString();

                            Toast.makeText(getApplicationContext(), "API URL changed to: " + url, Toast.LENGTH_LONG).show();
                            prefs.edit().putString("apiUrl", url).apply();


                            /* REMOVE ME ON PRODUCTION   /    ECHOS RESPONSE TO LOGCAT*/
                            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                            /* ====================== */

                            /* MOVE ME, SEE ABOVE */
                            retrofit = new Retrofit.Builder()
                                .client(client)
                                .baseUrl(prefs.getString("apiUrl", "http://192.169.159.139:8001"))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                            service = retrofit.create(GeochatAPIService.class);
                            /* ============================= */

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();

            }
        });



    }





    public void setupRegistrationBottomSheet(AppCompatActivity context){

        // get the bottom sheet view
        LinearLayout vBottomsheet = (LinearLayout) findViewById(R.id.regist_bottomsheet_root);

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(vBottomsheet);

        //
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Setup "create account" button
        findViewById(R.id.regist_bottomsheet_form_createaccount_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { createUserAccountAndProceed();

                    }
                }
        );

        // Setup "sign in" button
        findViewById(R.id.regist_bottomsheet_form_signin_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { showSigninDialog(); }
                }
        );


    }







    public void activateBottomSheet(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }







    public void createUserAccountAndProceed(){

        MaterialEditText _first_name = findViewById(R.id.regist_bottomsheet_form_firstname);
        MaterialEditText _last_name = findViewById(R.id.regist_bottomsheet_form_lastname);
        MaterialEditText _email_address = findViewById(R.id.regist_bottomsheet_form_email);
        final MaterialEditText _password = findViewById(R.id.regist_bottomsheet_form_password);

//        Toast.makeText(getApplicationContext(), "Creating a GeoChat account from this app is not yet available!", Toast.LENGTH_LONG).show();

        final User _user = new User();
        _user.first_name = _first_name.getText().toString();
        _user.last_name = _last_name.getText().toString();
        _user.email_address = _email_address.getText().toString();
        _user.password = _password.getText().toString();

        Call<GeochatAPIResponse> signInRequest = service.createUserAccount(
                _user.email_address,
                _user.password,
                _user.first_name,
                _user.last_name
        );
        signInRequest.enqueue(new Callback<GeochatAPIResponse>() {
            @Override
            public void onResponse(Call<GeochatAPIResponse> call, Response<GeochatAPIResponse> response) {
                if (response.code() == 200) {

                    if (response.body().getErrorMsg() != null) {
                        Toast.makeText(RegistrationActivity.this, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        // sign in
                        signinUserAccount(_user.email_address, _user.password);

                    }

                } else {

                    Toast.makeText(getApplicationContext(), R.string.api_message_404, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GeochatAPIResponse> call, Throwable t) {
                Log.i("Failure", t.toString());
                Toast.makeText(RegistrationActivity.this, R.string.api_message_createaccount, Toast.LENGTH_SHORT).show();
            }
        });

    }






    public void showSigninDialog(){

        // Show Sign-in dialog
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.dialog_signin, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText _email = (EditText) prompt.findViewById(R.id.signin_email) ;
        final EditText _pass = (EditText) prompt.findViewById(R.id.signin_password);
        alertDialogBuilder.setTitle("Sign in with existing account...");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                    signinUserAccount(_email.getText().toString(), _pass.getText().toString());

                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();

    }






    public void signinUserAccount(String _email, String _pass){

        // Attempt to communicate with API Service. Using Sign In function to receive Session Key
        Call<GeochatAPIResponse<UserSession>> signInRequest = service.signInUserAccount(_email, _pass);
        signInRequest.enqueue(new Callback<GeochatAPIResponse<UserSession>>() {
            @Override
            public void onResponse(Call<GeochatAPIResponse<UserSession>> call, Response<GeochatAPIResponse<UserSession>> response) {
                if (response.code() == 200) {

                    if (response.body().getErrorMsg() != null) {
                        Toast.makeText(RegistrationActivity.this, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        UserSession usersession = response.body().getData();
                        Log.i("GEOCHAT_INFO_API", "Geochat API Session Key received: " + usersession.session.session_key);
                        Log.i("GEOCHAT_INFO_API", "Geochat API User signed in: " + usersession.user.first_name + " " + usersession.user.last_name + "  id: " + usersession.user.id);

                        if (usersession.session.session_key != null) {
                            prefs.edit().putBoolean("isSignedIn", true).apply();
                            prefs.edit().putString("sessionKey", usersession.session.session_key).apply();
                            Toast.makeText(RegistrationActivity.this, "Welcome " + usersession.user.first_name + "!", Toast.LENGTH_SHORT).show();

                            // Dummy: Move screen to MainActivity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                } else {

                    Toast.makeText(getApplicationContext(), R.string.api_message_404, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GeochatAPIResponse<UserSession>> call, Throwable t) {
                Log.i("Failure", t.toString());
                Toast.makeText(RegistrationActivity.this, R.string.api_message_signin, Toast.LENGTH_SHORT).show();
            }
        });

    }







    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // Check if Bottomsheet is expanded, if so, close before exiting application
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            } else {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
            }
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }








    @Override
    protected void onResume() {
        super.onResume();

        networkCheck();

    }








    @Override
    public void onFragmentInteraction(Uri uri) {  }


    /**
     * A simple pager adapter that represents 4 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class WelcomeinfoPagerAdaptor extends FragmentStatePagerAdapter {
        Context context;

        public WelcomeinfoPagerAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            switch (position){
                case 0:
                    return new RegistWelcomeinfoFragment1();
                case 1:
                    return new RegistWelcomeinfoFragment2();
                case 2:
                    return new RegistWelcomeinfoFragment3();
                case 3:
                    return new RegistWelcomeinfoFragment4();
                default:
                    return new Fragment();


            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }



    private boolean networkCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_network_connection)
                    .setCancelable(false)
                    .setPositiveButton("Exit...", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            finish();
                            System.exit(0);

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return false;

        } else {

            return true;

        }
    }




}
