package com.inception.betappuser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inception.betappuser.fragments.ShowFragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class MatchOddDetails extends AppCompatActivity implements View.OnClickListener {
    private static JSONArray jsonArray;

    private static RecyclerView recyclerView;

    private TextView team_1, back_1, lay_1, team_2, back_2, lay_2;
    static String balances, event_id, status, susername;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_odd_details);
        recyclerView = findViewById(R.id.placed_bets);
        recyclerView.setLayoutManager(new LinearLayoutManager(MatchOddDetails.this, LinearLayoutManager.VERTICAL, false));

        getSupportActionBar().setTitle("Match Odds");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp_get = getSharedPreferences("user_info", MODE_PRIVATE);
        balances = sp_get.getString("balance", "");
        susername = sp_get.getString("username", "");
        team_1 = findViewById(R.id.team_1);
        team_2 = findViewById(R.id.team_2);
        event_id = getIntent().getStringExtra("event_id");

        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        status = sp.getString("status", "");
        back_1 = findViewById(R.id.back_1);
        back_2 = findViewById(R.id.back_2);

        lay_1 = findViewById(R.id.lay_1);
        lay_2 = findViewById(R.id.lay_2);
        back_1.setOnClickListener(this);
        back_2.setOnClickListener(this);
        lay_1.setOnClickListener(this);
        lay_2.setOnClickListener(this);
        get_data(MatchOddDetails.this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                getdata();

                handler.postDelayed(this, 2000);
            }
        }, 0);


    }

    public static void get_data(final Context con) {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "show_bet");
            jsonObject.put("username", susername);
            jsonObject.put("event_id", event_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);


                try {

                    jsonArray = response.getJSONArray("result");
                    Log.i("adapter to recycler --", "");
                    Adapter_odds adapter = new Adapter_odds(con);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.print(error);

            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 2, 2));


        Volley.newRequestQueue(con).add(jsonObjectRequest);


    }

    private static class Adapter_odds extends RecyclerView.Adapter<view_holder> {
        Context context;

        public Adapter_odds(Context con) {
            this.context = con;
        }

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(context).inflate(R.layout.placed_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(final view_holder holder, int position) {

            try {
                JSONObject data = jsonArray.getJSONObject(position);
                holder.team_name.setText(data.getString("team"));
                holder.rate.setText(data.getString("rate"));
                holder.date_time.setText(data.getString("datee"));
                holder.bet_value.setText(data.getString("stake"));
                holder.bet.setText(data.getString("bet"));
                if (data.getString("bet").equals("lay")) {
                    holder.bet_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.Lay));
                } else {
                    holder.bet_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.Back));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }
    }

    private static class view_holder extends RecyclerView.ViewHolder {
        TextView team_name, date_time, rate, bet, bet_value;
        RelativeLayout bet_layout;

        public view_holder(View itemView) {
            super(itemView);
            team_name = itemView.findViewById(R.id.team_name);
            rate = itemView.findViewById(R.id.bet_rate);
            date_time = itemView.findViewById(R.id.date_time);
            bet_value = itemView.findViewById(R.id.bet_value);
            bet = itemView.findViewById(R.id.bet);
            bet_layout = itemView.findViewById(R.id.bet_layout);
        }
    }

    public void getdata() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://www.lotusbook.com/api/exchange/eventType/" + event_id, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);

                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("runners");

                    JSONObject jsonObject = jsonArray1.getJSONObject(0);

                    String team1 = jsonObject.getString("name");

                    String back1 = jsonObject.getJSONArray("back").getJSONObject(0).getString("price");

                    String lay1 = jsonObject.getJSONArray("lay").getJSONObject(0).getString("price");


                    JSONObject jsonObject2 = jsonArray1.getJSONObject(1);

                    String team2 = jsonObject2.getString("name");

                    String back2 = jsonObject2.getJSONArray("back").getJSONObject(0).getString("price");

                    String lay2 = jsonObject2.getJSONArray("lay").getJSONObject(0).getString("price");

                    team_1.setText(team1);

                    team_2.setText(team2);


                    back_1.setText(back1);

                    lay_1.setText(lay1);

                    back_2.setText(back2);

                    lay_2.setText(lay2);


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

        Volley.newRequestQueue(MatchOddDetails.this).add(jsonObjectRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_1:
                if (status.equals("1")) {
                    SharedPreferences.Editor sp_dialog = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    sp_dialog.putString("team1", team_1.getText().toString());
                    sp_dialog.putString("team2", team_2.getText().toString());
                    sp_dialog.putString("back1", back_1.getText().toString());
                    sp_dialog.putString("balance", balances);
                    sp_dialog.putString("event_id", event_id);
                    sp_dialog.commit();
                    betting_dialouge1 dialouge = new betting_dialouge1(MatchOddDetails.this, R.style.Theme_Dialog);
                    dialouge.show();
                } else {
                    Toast.makeText(MatchOddDetails.this, "You are blocked", Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.back_2:
                if (status.equals("1")) {
                    SharedPreferences.Editor sp_dialog1 = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    sp_dialog1.putString("team1", team_1.getText().toString());
                    sp_dialog1.putString("team2", team_2.getText().toString());
                    sp_dialog1.putString("back2", back_2.getText().toString());
                    sp_dialog1.putString("balance", balances);
                    sp_dialog1.putString("event_id", event_id);
                    sp_dialog1.commit();
                    betting_dialouge2 dialouge1 = new betting_dialouge2(MatchOddDetails.this, R.style.Theme_Dialog);
                    dialouge1.show();
                } else {
                    Toast.makeText(MatchOddDetails.this, "You are blocked", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.lay_1:
                if (status.equals("1")) {
                    SharedPreferences.Editor sp_dialog2 = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    sp_dialog2.putString("team1", team_1.getText().toString());
                    sp_dialog2.putString("team2", team_2.getText().toString());
                    sp_dialog2.putString("lay1", lay_1.getText().toString());
                    sp_dialog2.putString("balance", balances);
                    sp_dialog2.putString("event_id", event_id);
                    sp_dialog2.commit();
                    lay_dialouge1 dialouge2 = new lay_dialouge1(MatchOddDetails.this, R.style.Theme_Dialog);
                    dialouge2.show();
                } else {
                    Toast.makeText(MatchOddDetails.this, "You are blocked", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.lay_2:
                if (status.equals("1")) {

                    SharedPreferences.Editor sp_dialog3 = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    sp_dialog3.putString("team1", team_1.getText().toString());
                    sp_dialog3.putString("team2", team_2.getText().toString());
                    sp_dialog3.putString("lay2", lay_2.getText().toString());
                    sp_dialog3.putString("balance", balances);
                    sp_dialog3.putString("event_id", event_id);
                    sp_dialog3.commit();
                    lay_dialouge2 dialouge3 = new lay_dialouge2(MatchOddDetails.this, R.style.Theme_Dialog);
                    dialouge3.show();
                } else {
                    Toast.makeText(MatchOddDetails.this, "You are blocked", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

}
