/*
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Martin Shaw on 21/05/2018 09:03 for GeoChat.
 */

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
