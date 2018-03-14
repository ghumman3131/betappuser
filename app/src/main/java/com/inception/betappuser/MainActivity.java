package com.inception.betappuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sp_main = getSharedPreferences("user_info" , MODE_PRIVATE);

                if(!sp_main.getString("username" ,"").equals(""))

                {
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);

                    startActivity(i);

                    finish();
                }

                else {

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);

                    startActivity(i);

                    finish();
                }
            }
        }, 3000);
    }
}
