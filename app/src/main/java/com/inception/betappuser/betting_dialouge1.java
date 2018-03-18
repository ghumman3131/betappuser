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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohit pal on 3/7/2018.
 */

public class betting_dialouge1 extends Dialog implements View.OnClickListener {
    TextView team1, team2, name, balance, price1, price2, rate, add500, add1000, add5000, add10000, clear, profit;
    Button cancel, place;
    EditText etstake;
    String sdis_id,steam1, steam2, sbalance, srate, sprice1, sprice2, eventid, susername;
    float irate, istake, iprice, ietstake, ibalance;
    Context mcontext;

    public betting_dialouge1(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mcontext = context;
        setContentView(R.layout.dialouge_betting);
        SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
        steam1 = sp.getString("team1", "");
        steam2 = sp.getString("team2", "");
        susername = sp.getString("username", "");
        sbalance = sp.getString("balance", "");
        srate = sp.getString("back1", "");
        eventid = sp.getString("event_id", "");
        sdis_id= sp.getString("dis_id", "");
        team1 = findViewById(R.id.Team_1);
        team1.setText(steam1);
        team2 = findViewById(R.id.Team_2);
        team2.setText(steam2);
        name = findViewById(R.id.name_txt);
        name.setText(steam1);
        balance = findViewById(R.id.bal_value);
        balance.setText(sbalance);
        ibalance = Float.parseFloat(balance.getText().toString());
        price1 = findViewById(R.id.price_1);
        price2 = findViewById(R.id.price_2);
        rate = findViewById(R.id.rate);
        rate.setText(srate);
        etstake = findViewById(R.id.stake);

        etstake.setText("0");

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
        irate = Float.parseFloat(srate);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stake_500:
                istake = Float.parseFloat(etstake.getText().toString());
                istake += 500;
                iprice = (istake * irate) - istake;
                price1.setText(String.valueOf(iprice));
                profit.setText(String.valueOf(iprice));
                price2.setText(String.valueOf("-" + istake));
                etstake.setText(String.valueOf(istake));

                break;
            case R.id.stake_1000:
                istake = Float.parseFloat(etstake.getText().toString());
                istake += 1000;
                iprice = (istake * irate) - istake;
                price1.setText(String.valueOf(iprice));
                profit.setText(String.valueOf(iprice));
                price2.setText(String.valueOf("-" + istake));
                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_5000:
                istake = Float.parseFloat(etstake.getText().toString());
                istake += 5000;
                iprice = (istake * irate) - istake;
                price1.setText(String.valueOf(iprice));
                profit.setText(String.valueOf(iprice));
                price2.setText(String.valueOf("-" + istake));
                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_10000:
                istake = Float.parseFloat(etstake.getText().toString());
                istake += 10000;
                iprice = (istake * irate) - istake;
                price1.setText(String.valueOf(iprice));
                profit.setText(String.valueOf(iprice));
                price2.setText(String.valueOf("-" + istake));
                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_clear:
                price1.setText("");
                profit.setText("0");
                price2.setText("");
                etstake.setText("0");
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.place:
                sprice1 = price1.getText().toString();
                sprice2 = price2.getText().toString();
                ietstake = Float.parseFloat(etstake.getText().toString());
                if (ietstake > ibalance) {
                    Toast.makeText(mcontext, "Low balance", Toast.LENGTH_SHORT).show();
                } else {
                    if (sprice1.isEmpty()) {
                        Toast.makeText(mcontext, "ENTER STAKE", Toast.LENGTH_SHORT).show();
                    } else {
                        final JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("module", "place_bet");
                            jsonObject.put("username", susername);
                            jsonObject.put("win", sprice1);
                            jsonObject.put("lose2", sprice2);
                            jsonObject.put("lose", "0");
                            jsonObject.put("win2", "0");
                            jsonObject.put("rate", srate);
                            jsonObject.put("team", name.getText().toString());
                            jsonObject.put("dis_id", sdis_id);
                            jsonObject.put("event_id", eventid);
                            jsonObject.put("bet", "Back");
                            jsonObject.put("team2", team2.getText().toString());

                            jsonObject.put("stake", etstake.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.print(jsonObject);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url.ip, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                System.out.println(response);

                                try {
                                    if (response.getString("result").equals("done")) {


                                        Toast.makeText(mcontext, "Bet placed", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                            MatchOddDetails.get_data(mcontext);

                                    } else {
                                        Toast.makeText(mcontext, "Error", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 2, 2));

                        Volley.newRequestQueue(mcontext).add(jsonObjectRequest);
                    }
                }


        break;
    }
}
    }

