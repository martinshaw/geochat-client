package co.martinshaw.apps.android.geochat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Retrofit;

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
        retrofit = new Retrofit.Builder()
                .baseUrl(prefs.getString("apiUrl", "http://192.169.159.139:8001").toString())
                .build();
        service = retrofit.create(GeochatAPIService.class);

        // Test API Service
        Call<GeochatAPIResponse> users = service.getAllUsers(prefs.getString("sessionKey", ""));
        Toast.makeText(this, users.toString(), Toast.LENGTH_SHORT).show();


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

                            retrofit = new Retrofit.Builder()
                                    .baseUrl(prefs.getString("apiUrl", "http://192.169.159.139:8001").toString())
                                    .build();
                            service = retrofit.create(GeochatAPIService.class);
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
                    public void onClick(View v) {

                        

                    }
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
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
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



}
