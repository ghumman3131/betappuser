package com.inception.betappuser;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohit pal on 3/7/2018.
 */

public class betting_dialouge1 extends Dialog implements View.OnClickListener {
    TextView team1, team2, name, balance, price1, price2, rate, add500, add1000, add5000, add10000, clear, profit;
    Button cancel, place;
    EditText etstake;
    String sdis_id, steam1, steam2, sbalance, srate, sprice1, sprice2, eventid, susername, opendate, match_name;
    int istake, iprice, ietstake, ibalance;

    float irate;

    String stake_team, team1_win_lose, team2_win_lose;
    ;


    Context mcontext;

    public betting_dialouge1(@NonNull Context context, int themeResId, String team1, String team2, String username, String balance,
                             String rate, String eventid, String opendate, String dis_id, final String stake_team, String match_name,
                             final String team1_win_lose, final String team2_win_lose) {
        super(context, themeResId);
        this.mcontext = context;
        setContentView(R.layout.dialouge_betting);
        this.stake_team = stake_team;
        steam1 = team1;
        steam2 = team2;
        susername = username;
        sbalance = balance;

        this.team1_win_lose = team1_win_lose;
        this.team2_win_lose = team2_win_lose;

        srate = rate;
        this.eventid = eventid;
        this.opendate = opendate;
        sdis_id = dis_id;
        this.team1 = findViewById(R.id.Team_1);
        this.team1.setText(steam1);
        this.team2 = findViewById(R.id.Team_2);
        this.team2.setText(steam2);
        name = findViewById(R.id.name_txt);

        this.match_name = match_name;

        if (stake_team.equals("1")) {
            name.setText(steam1);
        } else {
            name.setText(steam2);
        }
        this.balance = findViewById(R.id.bal_value);
        this.balance.setText(sbalance);
        ibalance = Integer.parseInt(this.balance.getText().toString());
        price1 = findViewById(R.id.price_1);
        price2 = findViewById(R.id.price_2);
        this.rate = findViewById(R.id.rate);
        this.rate.setText(srate);
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

        price1.setText(team1_win_lose);
        price2.setText(team2_win_lose);


        etstake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals("")) {

                    istake = Integer.parseInt(etstake.getText().toString());

                    if (istake != 0) {
                        iprice = (int) ((istake * irate) - istake);

                        profit.setText(String.valueOf(iprice));

                        if (stake_team.equals("1")) {


                            price1.setText(String.valueOf(Integer.parseInt(team1_win_lose) + Integer.parseInt(String.valueOf(iprice))));

                            price2.setText(String.valueOf(Integer.parseInt(String.valueOf(team2_win_lose)) + Integer.parseInt(String.valueOf("-" + istake))));
                        } else {

                            price2.setText(String.valueOf(Integer.parseInt(team2_win_lose) + Integer.parseInt(String.valueOf(iprice))));

                            price1.setText(String.valueOf(Integer.parseInt(String.valueOf(team1_win_lose)) + Integer.parseInt(String.valueOf("-" + istake))));

                        }

                    }

                } else {

                    price1.setText(team1_win_lose);
                    profit.setText("0");
                    price2.setText(team2_win_lose);
                    etstake.setText("0");
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stake_500:
                istake = Integer.parseInt(etstake.getText().toString());
                istake += 500;

                etstake.setText(String.valueOf(istake));

                break;
            case R.id.stake_1000:
                istake = Integer.parseInt(etstake.getText().toString());
                istake += 1000;

                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_5000:
                istake = Integer.parseInt(etstake.getText().toString());
                istake += 5000;

                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_10000:
                istake = Integer.parseInt(etstake.getText().toString());
                istake += 10000;

                etstake.setText(String.valueOf(istake));
                break;
            case R.id.stake_clear:
                price1.setText(team1_win_lose);
                profit.setText("0");
                price2.setText(team2_win_lose);
                etstake.setText("0");
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.place:

                if (stake_team.equals("1")) {


                    sprice1 = String.valueOf(iprice);

                    sprice2 = String.valueOf("-" + istake);
                } else {

                    sprice2 = String.valueOf(iprice);

                    sprice1 = String.valueOf("-" + istake);

                }

                ietstake = Integer.parseInt(etstake.getText().toString());

                String bal_to_subtract = "0";

                if (Integer.parseInt(price1.getText().toString()) < 0) {
                    bal_to_subtract = price1.getText().toString().replace("-", "");
                }

                if (Integer.parseInt(price2.getText().toString()) < 0) {
                    bal_to_subtract = price2.getText().toString().replace("-", "");
                }

                if (Integer.parseInt(price1.getText().toString()) < 0 && Integer.parseInt(price2.getText().toString()) < 0 && Integer.parseInt(price1.getText().toString()) < Integer.parseInt(price2.getText().toString())) {
                    bal_to_subtract = price1.getText().toString().replace("-", "");
                }

                if (Integer.parseInt(price1.getText().toString()) < 0 && Integer.parseInt(price2.getText().toString()) < 0 && Integer.parseInt(price2.getText().toString()) < Integer.parseInt(price1.getText().toString())) {
                    bal_to_subtract = price2.getText().toString().replace("-", "");
                }

                if (stake_team.equals("1")) {
                    if (!MatchDetailsActivity.back_1_rate.equals(srate)) {
                        Toast.makeText(mcontext, "rate changed try again", Toast.LENGTH_SHORT).show();
                        dismiss();
                        return;
                    }
                } else {
                    if (!MatchDetailsActivity.back_2_rate.equals(srate)) {
                        Toast.makeText(mcontext, "rate changed try again", Toast.LENGTH_SHORT).show();
                        dismiss();
                        return;
                    }
                }

                if (Integer.parseInt(bal_to_subtract) > ibalance) {
                    Toast.makeText(mcontext, "Low balance", Toast.LENGTH_SHORT).show();
                } else {
                    if (sprice1.isEmpty()) {
                        Toast.makeText(mcontext, "ENTER STAKE", Toast.LENGTH_SHORT).show();
                    } else {

                        detect_balance(etstake.getText().toString(), eventid + opendate.toLowerCase().replace(" ", ""), sdis_id);

                        place.setEnabled(false);
                        place.setText("placing..");

                    }
                }


                break;
        }
    }


    public void place_bet()
    {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "place_bet");
            jsonObject.put("username", susername);

            if (stake_team.equals("1")) {
                jsonObject.put("win", sprice1);
                jsonObject.put("lose2", sprice2);
            } else {
                jsonObject.put("win", sprice2);
                jsonObject.put("lose2", sprice1);
            }


            jsonObject.put("lose", "0");
            jsonObject.put("win2", "0");
            jsonObject.put("rate", srate);

            jsonObject.put("team", name.getText().toString());
            jsonObject.put("dis_id", sdis_id);
            jsonObject.put("event_id", eventid + opendate.toLowerCase().replace(" ", ""));
            jsonObject.put("bet", "Back");

            if (stake_team.equals("1")) {
                jsonObject.put("team2", team2.getText().toString());

            } else {
                jsonObject.put("team2", team1.getText().toString());

            }

            jsonObject.put("stake", etstake.getText().toString());

            jsonObject.put("match_name", match_name);
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


                        MatchDetailsActivity.force_fetch_team_win_lose(mcontext);

                        MatchDetailsActivity.get_data(mcontext);

                        MatchDetailsActivity.get_user_info(mcontext);

                        MatchDetailsActivity.LAST_BET_PLACED = System.currentTimeMillis()/1000;

                        dismiss();

                    } else {

                        place.setText("Place");
                        place.setEnabled(true);
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

    public void detect_balance(String stake, String event_id, String dis_id) {

        place.setText("Placing..");
        place.setEnabled(false);

        SharedPreferences sp = mcontext.getSharedPreferences("user_info", mcontext.MODE_PRIVATE);


        String bal_to_add = "0";

        if (Integer.parseInt(team1_win_lose) < 0) {
            bal_to_add = team1_win_lose.replace("-", "");
        }

        if (Integer.parseInt(team2_win_lose) < 0) {

            bal_to_add = team2_win_lose.replace("-", "");
        }

        if (Integer.parseInt(team1_win_lose) < 0 && Integer.parseInt(team1_win_lose) < 0 && Integer.parseInt(team1_win_lose) > Integer.parseInt(team2_win_lose))

        {
            bal_to_add = team2_win_lose.replace("-", "");
        }

        if (Integer.parseInt(team1_win_lose) < 0 && Integer.parseInt(team1_win_lose) < 0 && Integer.parseInt(team2_win_lose) > Integer.parseInt(team1_win_lose))

        {
            bal_to_add = team1_win_lose.replace("-", "");
        }

        String bal_to_subtract = "0";

        if (Integer.parseInt(price1.getText().toString()) < 0) {
            bal_to_subtract = price1.getText().toString().replace("-", "");
        }

        if (Integer.parseInt(price2.getText().toString()) < 0) {
            bal_to_subtract = price2.getText().toString().replace("-", "");
        }

        if (Integer.parseInt(price1.getText().toString()) < 0 && Integer.parseInt(price2.getText().toString()) < 0 && Integer.parseInt(price1.getText().toString()) < Integer.parseInt(price2.getText().toString())) {
            bal_to_subtract = price1.getText().toString().replace("-", "");
        }

        if (Integer.parseInt(price1.getText().toString()) < 0 && Integer.parseInt(price2.getText().toString()) < 0 && Integer.parseInt(price2.getText().toString()) < Integer.parseInt(price1.getText().toString())) {
            bal_to_subtract = price2.getText().toString().replace("-", "");
        }


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "deduct_balance");

            jsonObject.put("username", sp.getString("username", ""));
            jsonObject.put("stake", stake);
            jsonObject.put("event_id", event_id);
            jsonObject.put("dis_id", dis_id);
            jsonObject.put("bal_to_add", bal_to_add);
            jsonObject.put("bal_to_subtract", bal_to_subtract);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);


                try {
                    if (response.getString("result").equals("done")) {


                        place_bet();

                    } else {

                        place.setText("Place");
                        place.setEnabled(true);

                        Toast.makeText(mcontext, "error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);

            }
        });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 2, 2));

        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);


        requestQueue.add(jsonObjectRequest);

    }


}

