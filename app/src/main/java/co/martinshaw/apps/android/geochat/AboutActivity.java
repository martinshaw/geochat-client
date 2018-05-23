/*
 * AboutActivity
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/AboutActivity.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 06:23 <martin>
 * Last Compilation: 23/05/18 06:23
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.geochat_logo_large_foreground)
                .setDescription("A geolocation-based messaging and media content sharing platform where the user interacts with the people who surround them geographically, rather than interacting only with their existing friends selected by their profile, as is common with existing messaging applications.")
                .addGroup("Connect with the GeoChat development team:")
                .addEmail("thirdyearproject@martinshaw.co")
                .addWebsite("http://martinshaw.co")
                .addPlayStore(this.getPackageName())
                .addGitHub("martinshaw")
                .create();

        setContentView(aboutPage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
