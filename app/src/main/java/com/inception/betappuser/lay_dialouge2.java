package com.inception.betappuser;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohit pal on 3/7/2018.
 */

public class lay_dialouge2 extends Dialog implements View.OnClickListener {
    TextView team1, team2, name, balance, price1, price2, rate, add500, add1000, add5000, add10000, clear, liability;
    Button cancel, place;
    EditText etstake;
    String steam1, steam2, sbalance, srate;
    float irate, istake,iprice;

    public lay_dialouge2(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        setContentView(R.layout.lay_dialouge);
        SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
        steam1 = sp.getString("team1", "");
        steam2 = sp.getString("team2", "");
        sbalance = sp.getString("balance", "");
        srate = sp.getString("lay2", "");
        team1 = findViewById(R.id.Team_1);
        team1.setText(steam1);
        team2 = findViewById(R.id.Team_2);
        team2.setText(steam2);
        name = findViewById(R.id.name_txt);
        name.setText(steam1);
        balance = findViewById(R.id.bal_value);
        balance.setText(sbalance);
        price1 = findViewById(R.id.price_1);
        price2 = findViewById(R.id.price_2);
        rate = findViewById(R.id.rate);
        rate.setText(srate);
        etstake=findViewById(R.id.stake);

        etstake.setText("0");

        add500 = findViewById(R.id.stake_500);
        add1000 = findViewById(R.id.stake_1000);
        add5000 = findViewById(R.id.stake_5000);
        add10000 = findViewById(R.id.stake_10000);
        clear = findViewById(R.id.stake_clear);
        liability = findViewById(R.id.liability);
        cancel = findViewById(R.id.cancel);
        place = findViewById(R.id.place);
        add500.setOnClickListener(this);
        add1000.setOnClickListener(this);
        add5000.setOnClickListener(this);
        add10000.setOnClickListener(this);
        clear.setOnClickListener(this);
        cancel.setOnClickListener(this);
        place.setOnClickListener(this);
        irate=Float.parseFloat(srate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stake_500:
                istake= Float.parseFloat(etstake.getText().toString());
                istake+=500;
                iprice=(istake*irate)-istake;
                price1.setText(String.valueOf("-"+iprice));
                liability.setText(String.valueOf("-"+iprice));
                price2.setText(String.valueOf(istake));
                etstake.setText(String.valueOf(istake));

                break;
            case R.id.stake_1000:
                istake = Float.parseFloat(etstake.getText().toString());
                istake+=1000;
                iprice=(istake*irate)-istake;
                price1.setText(String.valueOf("-"+iprice));
                liability.setText(String.valueOf("-"+iprice));
                price2.setText(String.valueOf(istake));
                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_5000:
                istake= Float.parseFloat(etstake.getText().toString());
                istake+=5000;iprice=(istake*irate)-istake;
                price1.setText(String.valueOf("-"+iprice));
                liability.setText(String.valueOf("-"+iprice));
                price2.setText(String.valueOf(istake));
                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_10000:
                istake = Float.parseFloat(etstake.getText().toString());
                istake+=10000;
                iprice=(istake*irate)-istake;
                price1.setText(String.valueOf("-"+iprice));
                liability.setText(String.valueOf("-"+iprice));
                price2.setText(String.valueOf(istake));
                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_clear:
                price1.setText("");
                liability.setText("0");
                price2.setText("0");
                etstake.setText("0");
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.place:
                dismiss();
                break;
        }
    }
}
