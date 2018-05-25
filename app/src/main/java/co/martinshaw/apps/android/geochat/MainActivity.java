/*
 * MainActivity
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/MainActivity.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 06:23 <martin>
 * Last Compilation: 23/05/18 06:23
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.androidsurvey.SurveyActivity;

import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MaterialTabListener, MainThisAreaFragment.OnFragmentInteractionListener,
        MainThisAreaFragment.OnReceiveDataFromFragmentListener,
        MainSentMessagesFragment.OnFragmentInteractionListener,
        MainExploreFragment.OnFragmentInteractionListener {

    Toolbar vToolbar;
    ActionBarDrawerToggle mDrawerToggle;
    NavigationView vDrawerNavView;

    MaterialTabHost tabHost;
    ViewPager pager;
    ViewPagerAdapter pagerAdapter;

    DrawerLayout vDrawerLayout;
    SharedPreferences prefs;

    Retrofit retrofit;
    GeochatAPIService service;

    User user;

    LocationManager locationManager;
    int geolocationTimeInterval = 1000;
    int geolocationDistance = 0;
    String geolocationProvider;
    TextView geolocationOutput;

    final int FEEDBACK_QUIZ_REQUEST = 1337;
    final int GEOLOCATION_PERMISSION_REQUEST = 1338;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final AppCompatActivity self = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.prefs = this.getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);


        // Checks for network connection. If none found, force quit application
        networkCheck();


        // Setup and configure Retrofit
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(prefs.getString("apiUrl", "http://192.169.159.139:8001"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(GeochatAPIService.class);


        // Drawer Panel Setup & onClick Events
        vDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        vDrawerNavView = (NavigationView) findViewById(R.id.main_drawer);
        vDrawerNavView.setNavigationItemSelectedListener(this);


        // Drawer Panel Configure Animation Details
        AnimationDrawable animationDrawable =
                (AnimationDrawable) vDrawerNavView.getHeaderView(0).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();


        // Setup Action Bar (Toolbar)
        vToolbar = findViewById(R.id.main_toolbar);
        final ActionBar actionBar = getSupportActionBar();

        setSupportActionBar(vToolbar);


        // Set Dynamic Contents of Action Bar Title & Subtitle
        if (actionBar != null) {

            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
            vToolbar.setTitleTextColor(getResources().getColor(R.color.md_black_1000, null));
//            vToolbar.setLogo(getResources().getDrawable(R.drawable.geochat_logo,null));

            actionBar.setHomeAsUpIndicator(R.drawable.geochat_logo_large_foreground);
        }




        // Floating Action Button onClick Event
        FloatingActionButton vFloatingActionBtn = (FloatingActionButton) findViewById(R.id.main_fab);
        vFloatingActionBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

//                        Intent intent = new Intent(self, MapActivity.class);
//                        intent.putExtra("CURRENT_LOCATION_LAT", currentLocation.getLatitude());
//                        intent.putExtra("CURRENT_LOCATION_LONG", currentLocation.getLongitude());
//                        startActivity(intent);

                    }
                }
        );


        // Get and store user data
        Call<GeochatAPIResponse<User>> userRequest = service.getUserBySessionKey(prefs.getString("sessionKey", ""), prefs.getString("sessionKey", ""));
        userRequest.enqueue(new Callback<GeochatAPIResponse<User>>() {
            @Override
            public void onResponse(Call<GeochatAPIResponse<User>> call, Response<GeochatAPIResponse<User>> response) {
                if (response.code() == 200) {
                    if (response.body().getErrorMsg() != null) {
                        Toast.makeText(getApplicationContext(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        user = response.body().getData();

                        insertUserSpecificLabels();

                    }
                } else {

                    Toast.makeText(self, R.string.api_message_404, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GeochatAPIResponse<User>> call, Throwable t) {
                Log.i("Failure", t.toString());
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        // Set default "current" location for before detection of actual location
        // Use Yanlong Zhang's office as default location until sufficient GeoLoc is implemented
        setCurrentLocation(53.471756, -2.238803);


        // Setup Material Tabs
        setupMaterialTabs();


        authoriseGeolocationFunctionality();


    }







    // Location getter/setters for storage in SharedPreferences instead of storage in variable
    public void setCurrentLocation(double _lat, double _long){
        prefs.edit().putLong("locationLat", Double.doubleToRawLongBits(_lat)).apply();
        prefs.edit().putLong("locationLong", Double.doubleToRawLongBits(_long)).apply();
    }
    public void setCurrentLocation(Location _location){
        prefs.edit().putLong("locationLat", Double.doubleToRawLongBits(_location.getLatitude())).apply();
        prefs.edit().putLong("locationLong", Double.doubleToRawLongBits(_location.getLongitude())).apply();
    }
    public void setCurrentRadius(double _radius){
        prefs.edit().putLong("locationRadius", Double.doubleToRawLongBits(_radius)).apply();
    }
    public Location getCurrentLocation() {
        Location _loc = new Location("");
        _loc.setLatitude(Double.longBitsToDouble(prefs.getLong("locationLat", 0)));
        _loc.setLongitude(Double.longBitsToDouble(prefs.getLong("locationLong", 0)));
        return _loc;
    }
    public double getCurrentRadius() {
        return Double.longBitsToDouble(prefs.getLong("locationRadius", 0));
    }







    public void insertUserSpecificLabels (){

        // Drawer Panel Dynamically Change Title
        TextView vDrawerTitle = (TextView) vDrawerNavView.getHeaderView(0).findViewById(R.id.main_drawer_header_title);
        vDrawerTitle.setText(user.first_name + " " + user.last_name);

    }









    public void setupMaterialTabs(){

        tabHost = this.findViewById(R.id.main_materialTabHost);
        pager = this.findViewById(R.id.main_materialViewPager);

        // Setup Page Fragment Pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }

        });

        // insert all tabs from Page Fragment Pager Adaptor data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab( tabHost.newTab().setText(pagerAdapter.getPageTitle(i)).setTabListener(this) );
        };


    }









    public void openMapView() {

        Location currentLocation = getCurrentLocation();
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("CURRENT_LOCATION_LAT", currentLocation.getLatitude());
        intent.putExtra("CURRENT_LOCATION_LONG", currentLocation.getLongitude());
        startActivity(intent);

    }












    public void authoriseGeolocationFunctionality() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GEOLOCATION_PERMISSION_REQUEST);

        } else {

            setupGeolocationFunctionality();

        }
    }












    public void setupGeolocationFunctionality() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            authoriseGeolocationFunctionality();

        } else {

            // ii

        }

    }












    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GEOLOCATION_PERMISSION_REQUEST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        setupGeolocationFunctionality();

                    }

                } else {

                    Toast.makeText(this, "GEOLOCATION PERMISSION WAS NOT GRANTED", Toast.LENGTH_SHORT).show();

                }

                break;

            default:

                break;

        }
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }











    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (prefs.getBoolean("areAlertsEnabled", false)) {
            menu.getItem(0).setIcon(R.drawable.ic_notifications_active_black_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_notifications_off_black_24dp);
        }

//        if(prefs.getBoolean("isAnonymous", false) == true){
//            menu.getItem(1).setIcon(R.drawable.ic_person_outline_black_24dp);
//        } else {
//            menu.getItem(1).setIcon(R.drawable.ic_person_black_24dp);
//        }

        return true;
    }













    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Drawable icon;
        switch (item.getItemId()) {

            // action with ID action_maptoggle was selected
//            case R.id.action_anontoggle:
//
//                // Alternate button icon depending on current state
//                icon = item.getIcon();
//                if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_person_black_24dp, null).getConstantState())){
//                    item.setIcon(R.drawable.ic_person_outline_black_24dp);
//                    Toast.makeText(this, "User Anonymity turned off!", Toast.LENGTH_SHORT).show();
//                    prefs.edit().putBoolean("isAnonymous", false).apply();
//
//                } else if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_person_outline_black_24dp,null).getConstantState())){
//                    item.setIcon(R.drawable.ic_person_black_24dp);
//                    Toast.makeText(this, "User Anonymity turned on!", Toast.LENGTH_SHORT).show();
//                    prefs.edit().putBoolean("isAnonymous", true).apply();
//                }

            // action with ID action_alerttoggle was selected
            case R.id.action_alerttoggle:

                // Alternate button icon depending on current state
                icon = item.getIcon();
                if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_notifications_active_black_24dp, null).getConstantState())) {
                    item.setIcon(R.drawable.ic_notifications_off_black_24dp);
                    Toast.makeText(this, "Notifications & alerts turned off!", Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("areAlertsEnabled", false).apply();

                } else if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_notifications_off_black_24dp, null).getConstantState())) {
                    item.setIcon(R.drawable.ic_notifications_active_black_24dp);
                    Toast.makeText(this, "Notifications & alerts turned on!", Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("areAlertsEnabled", true).apply();

                }

                break;

            default:
                break;
        }

        return true;
    }












    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            
            case R.id.main_drawer_nav_item_signout: {

                Toast.makeText(this, "Signing out ...", Toast.LENGTH_SHORT).show();
                prefs.edit().putBoolean("isSignedIn", false).apply();
                prefs.edit().putString("sessionKey", "").apply();

                Intent intent = new Intent(this, SplashscreenActivity.class);
                startActivity(intent);
                finish();

                break;

            }
            case R.id.main_drawer_nav_item_feedback: {

                Intent i_survey = new Intent(this, SurveyActivity.class);
                i_survey.putExtra(
                        "json_survey",
                        "{\n" +
                                "  \"survey_properties\": {\n" +
                                "    \"intro_message\": \"<strong>Your feedback helps us to build a better GeoChat platform.</strong><br><br><br>It will take less than 2 minutes to answer the feedback quiz.\",\n" +
                                "    \"end_message\": \"Thank you for giving the time to take our survey.\",\n" +
                                "    \"skip_intro\": false\n" +
                                "  },\n" +
                                "  \"questions\": [\n" +
                                "    {\n" +
                                "      \"question_type\": \"Radioboxes\",\n" +
                                "      \"question_title\": \"Will you continue to use the GeoChat service?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": true,\n" +
                                "      \"random_choices\": false,\n" +
                                "      \"choices\": [\n" +
                                "        \"Yes\",\n" +
                                "        \"No\",\n" +
                                "        \"Maybe\"\n" +
                                "      ]\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_type\": \"StringMultiline\",\n" +
                                "      \"question_title\": \"Has the GeoChat crashed or acted bizarrely while you have been using it?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false,\n" +
                                "      \"number_of_lines\": 3\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_type\": \"StringMultiline\",\n" +
                                "      \"question_title\": \"Did you have any technical problems using the app?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false,\n" +
                                "      \"number_of_lines\": 3\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_type\": \"StringMultiline\",\n" +
                                "      \"question_title\": \"How would you improve the GeoChat app?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false,\n" +
                                "      \"number_of_lines\": 3\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_type\": \"StringMultiline\",\n" +
                                "      \"question_title\": \"How might this GeoChat become useful to your day-to-day life?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false,\n" +
                                "      \"number_of_lines\": 3\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"
                );
                startActivityForResult(i_survey, FEEDBACK_QUIZ_REQUEST);

                break;

            }
            case R.id.main_drawer_nav_item_about: {

                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);

                break;

            }
            
            default:

                Toast.makeText(this, "This feature is coming soon!", Toast.LENGTH_SHORT).show();
                break;

        }

        //close navigation drawer
        vDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }










    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // On receiving response from feedback quiz....
        // Have the user send the pre-composed email message
        if (requestCode == FEEDBACK_QUIZ_REQUEST) {
            if (resultCode == RESULT_OK) {

                String answers_json = data.getExtras().getString("answers");
                Log.d("****", "****************** WE HAVE ANSWERS ******************");
                Log.v("ANSWERS JSON", answers_json);
                Log.d("****", "*****************************************************");

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"thirdyearproject@martinshaw.co"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "GeoChat App Feedback: ");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Thank you so much for providing feedback for our Application.\n\n" +
                                "This will allow us to improve the app for yourself and for new users in the future.\n\n" +
                                "Please press \"Send Message\"...\n\n\n\n\n\n" +
                                answers_json + "\n\n" +
                                System.getProperty("os.version") + "\n" +
                                ((Build.VERSION.SDK != null) ? Build.VERSION.SDK : "") + "\n" +
                                ((Build.DEVICE != null) ? Build.DEVICE : "") + "\n" +
                                ((Build.MODEL != null) ? Build.MODEL : "") + "\n" +
                                ((Build.PRODUCT != null) ? Build.PRODUCT : "") + "\n\n" +
                                "==== message end ====");

                startActivity(Intent.createChooser(intent, "Email via..."));

            }
        }

    }









    @Override
    protected void onResume() {
        super.onResume();

        networkCheck();

    }







    @Override
    protected void onPause() {
        super.onPause();

        // SETUP REMOVEUPDATES

    }









    // MaterialTabs inheritance functions
    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    // fragment sending data to activity
    // fragment -> activity
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onReceiveDataFromFragment(String action, Object data) {

        switch (action){

            case "openMapView":
                openMapView();
                break;

            default:
                break;

        }

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            switch (num) {
                case 0: return MainThisAreaFragment.newInstance();
                case 1: return MainSentMessagesFragment.newInstance();
                case 2: return MainExploreFragment.newInstance();
                default: return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "THIS AREA";
                case 1: return "SENT MESSAGES";
                case 2: return "EXPLORE";
                default: return String.valueOf(position);
            }
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
