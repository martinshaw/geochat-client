package co.martinshaw.apps.android.geochat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.androidsurvey.SurveyActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar vToolbar;

    DrawerLayout vDrawerLayout;
    SharedPreferences prefs;

    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLocation;

    final int FEEDBACK_QUIZ_REQUEST = 1337;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final AppCompatActivity self = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);









        // Drawer Panel Setup & onClick Events
        vDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        NavigationView vDrawerNavView = (NavigationView) findViewById(R.id.main_drawer);
        vDrawerNavView.setNavigationItemSelectedListener(this);

        // Drawer Panel Configure Animation Details
        AnimationDrawable animationDrawable =
                (AnimationDrawable) vDrawerNavView.getHeaderView(0).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        // Drawer Panel Dynamically Change Title
        TextView vDrawerTitle = (TextView) vDrawerNavView.getHeaderView(0).findViewById(R.id.main_drawer_header_title);
        vDrawerTitle.setText("Martin Shaw");








        // Setup Action Bar (Toolbar)
        vToolbar = findViewById(R.id.main_toolbar);
        ActionBarDrawerToggle mDrawerToggle;
        final ActionBar actionBar = getSupportActionBar();

        setSupportActionBar(vToolbar);

        // Set Dynamic Contents of Action Bar Title & Subtitle
        if (actionBar != null) {

            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
            vToolbar.setTitleTextColor(getResources().getColor(R.color.md_black_1000, null));
//            vToolbar.setLogo(getResources().getDrawable(R.drawable.geochat_logo,null));

            actionBar.setHomeAsUpIndicator(R.drawable.geochat_logo_large_foreground );
        }









        // Floating Action Button onClick Event
        FloatingActionButton vFloatingActionBtn = (FloatingActionButton) findViewById(R.id.main_fab);
        vFloatingActionBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "* Will open Message Compose view... *", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(self, MapActivity.class);
                        intent.putExtra("CURRENT_LOCATION_LAT", currentLocation.getLatitude());
                        intent.putExtra("CURRENT_LOCATION_LONG", currentLocation.getLongitude());
                        startActivity(intent);

                    }
                }
        );








        // Set default "current" location for before detection of actual location
        currentLocation = new Location("");
        currentLocation.setLatitude(51.509865);     // Use London as default location
        currentLocation.setLongitude(-0.118092);

        // Get last geo-location detected location
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                setCurrentLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }





    }




    public void setCurrentLocation(Location location){
        this.currentLocation.setLatitude(location.getLatitude());
        this.currentLocation.setLongitude(location.getLongitude());

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }






    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(prefs.getBoolean("areAlertsEnabled", false)){
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
                if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_notifications_active_black_24dp, null).getConstantState())){
                    item.setIcon(R.drawable.ic_notifications_off_black_24dp);
                    Toast.makeText(this, "Notifications & alerts turned off!", Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("areAlertsEnabled", false).apply();

                } else if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_notifications_off_black_24dp,null).getConstantState())){
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








    private String loadSurveyJson(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }







    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.main_drawer_nav_item_1: {

                Toast.makeText(this, "Drawer Menu Item #1 was clicked!", Toast.LENGTH_SHORT).show();

                break;

            }
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
                                "    \"intro_message\": \"<strong>Your feedback helps us to build a better mobile product.</strong><br><br><br>   Hello, Feedback from our clients, friends and family is how we make key decisions on what the future holds for XYZ App.<br><br>By combining data and previous feedback we have introduced many new features e.g. x, y, z.<br><br>It will take less than 2 minutes to answer the feedback quiz.\",\n" +
                                "    \"end_message\": \"Thank you for having the time to take our survey.\",\n" +
                                "    \"skip_intro\": false\n" +
                                "  },\n" +
                                "  \"questions\": [\n" +
                                "    {\n" +
                                "      \"question_type\": \"Checkboxes\",\n" +
                                "      \"question_title\": \"What were you hoping the XYZ mobile app would do?\",\n" +
                                "      \"description\": \"(Select all that apply)\",\n" +
                                "      \"required\": false,\n" +
                                "      \"random_choices\": false,\n" +
                                "      \"choices\": [\n" +
                                "        \"thing #1\",\n" +
                                "        \"thing #2\",\n" +
                                "        \"thing #3\",\n" +
                                "        \"thing #4\"\n" +
                                "      ]\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_type\": \"Checkboxes\",\n" +
                                "      \"question_title\": \"Do you currently use one of these other software solutions?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false,\n" +
                                "      \"random_choices\": true,\n" +
                                "      \"choices\": [\n" +
                                "        \"<font color='#AA0000'>Yes, I use a <strong>red</strong> product</font>\",\n" +
                                "        \"I use a <font color='#00AA00'>green product</font>\",\n" +
                                "        \"I partialy use a <font color='#0000AA'><strong>blue</strong></font> product\"\n" +
                                "      ]\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_type\": \"String\",\n" +
                                "      \"question_title\": \"Why did you not subscribe at the end of your free trial ?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_title\": \"If this app was paid, how much you would give to have it ?\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false,\n" +
                                "      \"question_type\": \"Number\"\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"question_type\": \"StringMultiline\",\n" +
                                "      \"question_title\": \"We love feedback and if there is anything else you’d like us to improve please let us know.\",\n" +
                                "      \"description\": \"\",\n" +
                                "      \"required\": false,\n" +
                                "      \"number_of_lines\": 4\n" +
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
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "thirdyearproject@martinshaw.co" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "GeoChat App Feedback: ");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Thank you so much for providing feedback for our Application.\n\n"+
                                "This will allow us to improve the app for yourself and for new users in the future.\n\n"+
                                "Please press \"Send Message\"...\n\n\n\n\n\n"+
                                answers_json+"\n\n"+
                                System.getProperty("os.version")+"\n"+
                                ((Build.VERSION.SDK != null)? Build.VERSION.SDK : "")+"\n"+
                                ((Build.DEVICE != null)? Build.DEVICE : "")+"\n"+
                                ((Build.MODEL != null)? Build.MODEL : "")+"\n"+
                                ((Build.PRODUCT != null)? Build.PRODUCT : "")+"\n\n"+
                                "==== message end ====");

                startActivity(Intent.createChooser(intent, "Email via..."));

            }
        }

    }






}
