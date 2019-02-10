package com.psl.fantasy.league.season2;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;

import com.psl.classes.Config;
import com.psl.fantasy.league.season2.R;;


public class UserDashboard extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    RoundedImageView pImage;
   /* SharedPreferences sharedPreferences;
    RoundedImageView pImage;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        pImage = (RoundedImageView)findViewById(R.id.imgUserPicture);

        String strBase64 = sharedPreferences.getString(Config.PICTURE, "");
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        if (decodeByte != null)
            pImage.setImageBitmap(decodeByte);


    }

}
