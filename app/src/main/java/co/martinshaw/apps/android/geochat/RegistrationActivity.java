/*
 * RegistrationActivity
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/RegistrationActivity.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 21/05/18 10:51 <martin>
 * Last Compilation: 21/05/18 10:51
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        prefs = this.getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);

        // Setup API Service
        /*
        *  MOVE API SETUP CODE FROM API URL DIALOG EVENT TO HERE IN PRODUCTION
        * */

        // Setup and configure bottom sheet for user registration
        setupRegistrationBottomSheet(this);

        // Instantiate a ViewPager and a PagerAdapter for the Welcome Information Display
        mPager = (ViewPager) findViewById(R.id.regist_welcomeinfo_container);
        mPagerAdapter = new WelcomeinfoPagerAdaptor(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // Setup API URL setting dialog
        mAPIURLSettingDialog = (Button) findViewById(R.id.regist_bottomsheet_setting_button);
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
                    public void onClick(View v) {

                        createUserAccountAndProceed();

                    }
                }
        );

        // Setup "sign in" button
        findViewById(R.id.regist_bottomsheet_form_signin_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { signinUserAccountAndProceed(); }
                }
        );


    }



    public void activateBottomSheet(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    public void createUserAccountAndProceed(){

        // Dummy: Remember user is signed in
        prefs.edit().putBoolean("isSignedIn", true).apply();

        // Dummy: Attempt REST Function
        Toast.makeText(getApplicationContext(), "Creating user...", Toast.LENGTH_LONG).show();

        // Dummy: Move screen to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    public void signinUserAccountAndProceed(){
        // Check Internet Connection
        if (!isNetworkConnected()) {


            Toast.makeText(this, R.string.error_network_connection, Toast.LENGTH_SHORT).show();


        } else {


            // Check form inputs validated
            MaterialEditText mEmailAddressTextBox = (MaterialEditText) findViewById(R.id.regist_bottomsheet_form_email);
            MaterialEditText mPasswordTextBox = (MaterialEditText) findViewById(R.id.regist_bottomsheet_form_password);
            String inputed_email_address = (String) mEmailAddressTextBox.getText().toString();
            String inputed_password = (String) mPasswordTextBox.getText().toString();


            // Attempt to communicate with API Service. Using Sign In function to receive Session Key
            // onFailure will be triggered if no .data object is received (in case of !200 response), just in case, check for !200 .status code in onResponse function too
            Call<GeochatAPIResponse<UserSession>> signInRequest = service.signInUserAccount(inputed_email_address, inputed_password);
            signInRequest.enqueue(new Callback<GeochatAPIResponse<UserSession>>() {
                @Override
                public void onResponse(Call<GeochatAPIResponse<UserSession>> call, Response<GeochatAPIResponse<UserSession>> response) {
                    if (response.body().getErrorMsg() != null) {
                        Toast.makeText(RegistrationActivity.this, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        UserSession usersession = response.body().getData();
                        Log.i("GEOCHAT_INFO_API", "Geochat API Session Key received: " + usersession.session.session_key);
                        Log.i("GEOCHAT_INFO_API", "Geochat API User signed in: " + usersession.user.first_name + " " + usersession.user.last_name + "  id: " + usersession.user.id);

                        if (usersession.session.session_key != null) {
                            prefs.edit().putString("sessionKey", usersession.session.session_key).apply();
                            Toast.makeText(RegistrationActivity.this, "Welcome " + usersession.user.first_name + "!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<GeochatAPIResponse<UserSession>> call, Throwable t) {
                    Log.i("Failure", t.toString());
                    Toast.makeText(RegistrationActivity.this, R.string.error_signin, Toast.LENGTH_SHORT).show();
                }
            });

            
        }
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



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }



}
