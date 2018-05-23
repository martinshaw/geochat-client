/*
 * SplashscreenActivity
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/SplashscreenActivity.java
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashscreenActivity extends AppCompatActivity {

    Boolean signedIn = false;
    Intent intent;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        prefs = this.getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);

        // Set default settings on first run
        if (! prefs.contains("isSignedIn")){ prefs.edit().putBoolean("isSignedIn", false).apply(); }
        if (! prefs.contains("areAlertsEnabled")){ prefs.edit().putBoolean("areAlertsEnabled", false).apply(); }
        if (! prefs.contains("isAnonymous")){ prefs.edit().putBoolean("isAnonymous", false).apply(); }
        if (! prefs.contains("apiUrl")){ prefs.edit().putString("apiUrl", "http://192.168.159.139:8001").apply(); }
        if (! prefs.contains("locationLat")){ prefs.edit().putLong("locationLat", 0).apply(); }
        if (! prefs.contains("locationLong")){ prefs.edit().putLong("locationLong", 0).apply(); }
        if (! prefs.contains("locationRadius")){ prefs.edit().putLong("locationRadius", 0).apply(); }

        // Check if app remembers a signed in user from settings
        if(prefs.getBoolean("isSignedIn", false)){
            signedIn = true;
        }

        // Check if logged in? If so, direct user to MainActivity. Otherwise, direct user to
        // RegistrationActivity.
        if (signedIn){
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
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Move to instance of MainActivity
        startActivity(intent);
        finish();

    }
}
