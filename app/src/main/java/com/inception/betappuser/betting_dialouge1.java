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

public class betting_dialouge1 extends Dialog implements View.OnClickListener {
    TextView team1, team2, name, balance, price1, price2, rate, add500, add1000, add5000, add10000, clear, profit;
    Button cancel, place;
    EditText etstake;
    String steam1, steam2, sbalance, srate;
    int irate, istake,iprice;

    public betting_dialouge1(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialouge_betting);
        SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
        steam1 = sp.getString("team1", "");
        steam2 = sp.getString("team2", "");
        sbalance = sp.getString("balance", "");
        srate = sp.getString("back1", "");
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
        add500 = findViewById(R.id.stake_500);
        add1000 = findViewById(R.id.stake_1000);
        add5000 = findViewById(R.id.stake_5000);
        add10000 = findViewById(R.id.stake_10000);
        clear = findViewById(R.id.stake_clear);
        profit = findViewById(R.id.profit);
        cancel = findViewById(R.id.cancel);
        place = findViewById(R.id.place);
        add500.setOnClickListener(this);
        add1000.setOnClickListener(this);
        add5000.setOnClickListener(this);
        add10000.setOnClickListener(this);
        clear.setOnClickListener(this);
        cancel.setOnClickListener(this);
        place.setOnClickListener(this);
        irate=Integer.parseInt(srate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stake_500:
                istake= Integer.parseInt(etstake.getText().toString());
                istake+=500;
                iprice=(istake*irate)-istake;
                price1.setText(iprice);
                profit.setText(iprice);
                price2.setText("-"+istake);
                etstake.setText(String.valueOf(istake));

                break;
                case R.id.stake_1000:
                    istake = Integer.parseInt(etstake.getText().toString());
                    istake+=1000;
                    iprice=(istake*irate)-istake;
                    price1.setText(iprice);
                    profit.setText(iprice);
                    price2.setText("-"+istake);
                etstake.setText(istake);
                break;
            case R.id.stake_5000:
                istake= Integer.parseInt(etstake.getText().toString());
                istake+=5000;iprice=(istake*irate)-istake;
                price1.setText(iprice);
                profit.setText(iprice);
                price2.setText("-"+istake);
                etstake.setText(istake);
                break;
                case R.id.stake_10000:
                    istake = Integer.parseInt(etstake.getText().toString());
                    istake+=10000;
                    iprice=(istake*irate)-istake;
                    price1.setText(iprice);
                    profit.setText(iprice);
                    price2.setText("-"+istake);
                    etstake.setText(istake);
                break;
            case R.id.stake_clear:
                price1.setText("");
                profit.setText(0);
                price2.setText("0");
                etstake.setText(0);
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
