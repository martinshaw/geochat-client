package co.martinshaw.apps.android.geochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashscreenActivity extends AppCompatActivity {

    Boolean loggedIn = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        // Check if logged in? If so, direct user to MainActivity. Otherwise, direct user to
        // RegistrationActivity.
        if (loggedIn){
            // Prepare an instance of the MainActivity to be navigated to after
            // App preparations are completed
            intent = new Intent(this, MainActivity.class);
        } else {
            // Prepare an instance of the RegistrationActivity to be navigated to after
            // App preparations are completed
            intent = new Intent(this, RegistrationActivity.class);
        }

        // Force wait for 1 second for testing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Move to instance of MainActivity
        startActivity(intent);
        finish();

    }
}
