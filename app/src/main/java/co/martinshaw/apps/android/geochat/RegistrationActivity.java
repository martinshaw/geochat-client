package co.martinshaw.apps.android.geochat;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Setup and configure bottom sheet for user registration
        setupRegistrationBottomSheet(this);

        // Setup and configure bottom sheet handle gradient background animation
        setupRegBSGradientAnimEffect(this);

        // Instantiate a ViewPager and a PagerAdapter for the Welcome Information Display
        mPager = (ViewPager) findViewById(R.id.regist_welcomeinfo_container);
        mPagerAdapter = new WelcomeinfoPagerAdaptor(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);




    }



    public void setupRegistrationBottomSheet(AppCompatActivity context){

        // get the bottom sheet view
        LinearLayout vBottomsheet = (LinearLayout) findViewById(R.id.regist_bottomsheet_root);

        // init the bottom sheet behavior
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(vBottomsheet);

        // set the peek height
        final int _bottomSheetPeekHeight = (findViewById(R.id.regist_bottomsheet_handle)).getLayoutParams().height;
        bottomSheetBehavior.setPeekHeight(_bottomSheetPeekHeight);

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }, 800);




        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // set onClick event listener for Bottom Sheet Handle
        vBottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

    }



    public void setupRegBSGradientAnimEffect(AppCompatActivity context){
        AnimationDrawable animationDrawable = (AnimationDrawable) ((LinearLayout)findViewById(R.id.regist_bottomsheet_handle)).getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(1500);
        animationDrawable.start();
    }



    private int getNavigationBarHeight(Context context, int orientation){
        Resources resources = context.getResources();
        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }




    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }


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
