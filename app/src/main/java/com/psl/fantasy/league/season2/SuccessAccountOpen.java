package com.psl.fantasy.league.season2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;

public class SuccessAccountOpen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_account_open);
        try{findViewById(R.id.textView36).bringToFront();} catch (Exception e){}

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Fragment fragment =null;
                if(Config.prevFragment!=null)
                    fragment= Config.prevFragment;
                else
                    fragment= new MyWallet();

                FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
                ft.attach(fragment);
                ft.commit();

            }
        });
    }
}
