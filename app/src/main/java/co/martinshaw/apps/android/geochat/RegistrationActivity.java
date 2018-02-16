package co.martinshaw.apps.android.geochat;

import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        prefs = this.getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);

        // Setup and configure bottom sheet for user registration
        setupRegistrationBottomSheet(this);

        // Instantiate a ViewPager and a PagerAdapter for the Welcome Information Display
        mPager = (ViewPager) findViewById(R.id.regist_welcomeinfo_container);
        mPagerAdapter = new WelcomeinfoPagerAdaptor(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);




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

                        createUseraccountAndProceed();

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


    public void createUseraccountAndProceed(){

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
