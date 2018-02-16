package co.martinshaw.apps.android.geochat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout vDrawerLayout;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);


        // Setup Action Bar (Toolbar)
        Toolbar vToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(vToolbar);

        // Set Dynamic Contents of Action Bar Title & Subtitle
//        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("123 Fake Street, Manchester, UK");
            vToolbar.setTitleTextColor(getResources().getColor(R.color.md_black_1000, null));
//            vToolbar.setLogo(getResources().getDrawable(R.drawable.geochat_logo,null));
            vToolbar.setSubtitle("Within 100 metres");
        }


        // Floating Action Button onClick Event
        FloatingActionButton vFloatingActionBtn = (FloatingActionButton) findViewById(R.id.main_fab);
        vFloatingActionBtn.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Toast.makeText(getApplicationContext(), "asdasdads", Toast.LENGTH_SHORT).show();
                    }
                }
        );


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





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(prefs.getBoolean("isAnonymous", false) == true){
            menu.getItem(R.id.action_anontoggle).setIcon(R.drawable.ic_person_outline_black_24dp);
        } else {
            menu.getItem(R.id.action_anontoggle).setIcon(R.drawable.ic_person_black_24dp);
        }

        if(prefs.getBoolean("areAlertsEnabled", false) == true){
            menu.getItem(R.id.action_alerttoggle).setIcon(R.drawable.ic_notifications_active_black_24dp);
        } else {
            menu.getItem(R.id.action_alerttoggle).setIcon(R.drawable.ic_notifications_off_black_24dp);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Drawable icon;
        switch (item.getItemId()) {

            // action with ID action_maptoggle was selected
            case R.id.action_anontoggle:

                // Alternate button icon depending on current state
                icon = item.getIcon();
                if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_person_black_24dp, null).getConstantState())){
                    item.setIcon(R.drawable.ic_person_outline_black_24dp);
                    Toast.makeText(this, "User Anonymity turned off!", Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("isAnonymous", false).apply();

                } else if (icon.getConstantState().equals(getResources().getDrawable(R.drawable.ic_person_outline_black_24dp,null).getConstantState())){
                    item.setIcon(R.drawable.ic_person_black_24dp);
                    Toast.makeText(this, "User Anonymity turned on!", Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("isAnonymous", true).apply();
                }

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

                Intent intent = new Intent(this, SplashscreenActivity.class);
                startActivity(intent);
                finish();

                break;
            }

        }

        //close navigation drawer
        vDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
