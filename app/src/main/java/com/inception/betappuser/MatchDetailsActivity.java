package com.inception.betappuser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class MatchDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private static JSONArray jsonArray;

    private static RecyclerView recyclerView;

    private static TextView team_1, back_1, lay_1, team_2, back_2, lay_2, team1_result, team2_result;
    static String balances, event_id, status = "-1", susername, opendate, dis_id, limit, match_name;

    private static RequestQueue requestQueue;

    public static Boolean team_win_lose_fetched = false;

    public static String back_1_rate, lay_1_rate, back_2_rate, lay_2_rate, username;

    private Boolean ispause = false;

    private RecyclerView session_recyclerView;

    private JSONArray session_jsonArray;

    public static long LAST_BET_PLACED = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_odd_details);


        event_id = getIntent().getStringExtra("event_id");

        match_name = getIntent().getStringExtra("match_vs");

        opendate = getIntent().getStringExtra("opendate");


        getSupportActionBar().setTitle(match_name);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.placed_bets);
        recyclerView.setLayoutManager(new LinearLayoutManager(MatchDetailsActivity.this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setNestedScrollingEnabled(false);

        session_recyclerView = findViewById(R.id.recycler);
        session_recyclerView.setLayoutManager(new LinearLayoutManager(MatchDetailsActivity.this, LinearLayoutManager.VERTICAL, false));


        requestQueue = Volley.newRequestQueue(MatchDetailsActivity.this);


        SharedPreferences sp_get = getSharedPreferences("user_info", MODE_PRIVATE);

        susername = sp_get.getString("username", "");
        username = susername;
        dis_id = sp_get.getString("dis_id", "");
        team_1 = findViewById(R.id.team_1);
        team_2 = findViewById(R.id.team_2);


        team1_result = findViewById(R.id.result_team1);
        team2_result = findViewById(R.id.result_team2);


        back_1 = findViewById(R.id.back_1);
        back_2 = findViewById(R.id.back_2);

        lay_1 = findViewById(R.id.lay_1);
        lay_2 = findViewById(R.id.lay_2);
        back_1.setOnClickListener(this);
        back_2.setOnClickListener(this);
        lay_1.setOnClickListener(this);
        lay_2.setOnClickListener(this);
        get_data(MatchDetailsActivity.this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                if (ispause) {
                    return;
                }
                getdata();

                handler.postDelayed(this, 5000);
            }
        }, 0);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ispause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ispause = false;

        get_user_info(MatchDetailsActivity.this);


        team_win_lose_fetched = false;
    }

    public static void get_data(final Context con) {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "show_bet");
            jsonObject.put("username", susername);
            jsonObject.put("event_id", event_id + opendate.toLowerCase().replace(" ", ""));
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


        requestQueue.add(jsonObjectRequest);


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
                if (data.getString("bet").toLowerCase().equals("lay")) {
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

                    if (jsonObject.getJSONArray("back").length() > 0) {
                        back_1_rate = jsonObject.getJSONArray("back").getJSONObject(0).getString("price");

                    }
                    if (jsonObject.getJSONArray("lay").length() > 0) {
                        lay_1_rate = jsonObject.getJSONArray("lay").getJSONObject(0).getString("price");
                    }

                    JSONObject jsonObject2 = jsonArray1.getJSONObject(1);

                    String team2 = jsonObject2.getString("name");

                    if (jsonObject2.getJSONArray("back").length() > 0) {
                        back_2_rate = jsonObject2.getJSONArray("back").getJSONObject(0).getString("price");
                    }

                    if (jsonObject2.getJSONArray("lay").length() > 0) {
                        lay_2_rate = jsonObject2.getJSONArray("lay").getJSONObject(0).getString("price");
                    }
                    team_1.setText(team1);

                    team_2.setText(team2);


                    back_1.setText(back_1_rate);

                    lay_1.setText(lay_1_rate);

                    back_2.setText(back_2_rate);

                    lay_2.setText(lay_2_rate);

                    if (response.getString("status").equals("SUSPENDED")) {
                        back_1.setText("suspended");

                        lay_1.setText("suspended");

                        back_2.setText("suspended");

                        lay_2.setText("suspended");

                        back_1.setClickable(false);

                        lay_1.setClickable(false);

                        back_2.setClickable(false);

                        lay_2.setClickable(false);
                    }

                    session_jsonArray = response.getJSONArray("result");

                    if(session_jsonArray.length() > 1) {

                        JSONArray session_jsonArray1 = session_jsonArray.getJSONObject(1).getJSONArray("runners");

                        System.out.println(session_jsonArray1.toString());

                        SessionAdapter adapter = new SessionAdapter();

                        session_recyclerView.setAdapter(adapter);
                    }

                    if (!team_win_lose_fetched) {
                        team_win_lose();

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


        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_1:


                    if (status.equals("1")) {
                        if ( team_win_lose_fetched && ( System.currentTimeMillis()/1000 - LAST_BET_PLACED ) > ShowFragments.BETTING_TIME ) {
                            betting_dialouge1 dialouge = new betting_dialouge1(MatchDetailsActivity.this, R.style.Theme_Dialog, team_1.getText().toString(),
                                    team_2.getText().toString(), susername, balances, back_1.getText().toString(), event_id, opendate, dis_id, "1", match_name, team1_result.getText().toString(),
                                    team2_result.getText().toString());
                            dialouge.show();
                        } else {
                            Toast.makeText(MatchDetailsActivity.this, "please wait", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(MatchDetailsActivity.this, "You are blocked", Toast.LENGTH_SHORT).show();

                    }


                break;
            case R.id.back_2:

                    if (status.equals("1")) {

                        if ( team_win_lose_fetched && ( System.currentTimeMillis()/1000 - LAST_BET_PLACED ) > ShowFragments.BETTING_TIME ) {
                            betting_dialouge1 dialouge = new betting_dialouge1(MatchDetailsActivity.this, R.style.Theme_Dialog, team_1.getText().toString(),
                                    team_2.getText().toString(), susername, balances, back_2.getText().toString(), event_id, opendate, dis_id, "2", match_name, team1_result.getText().toString(),
                                    team2_result.getText().toString());
                            dialouge.show();
                        } else {
                            Toast.makeText(MatchDetailsActivity.this, "please wait", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(MatchDetailsActivity.this, "You are blocked", Toast.LENGTH_SHORT).show();

                    }

                break;
            case R.id.lay_1:

                    if (status.equals("1")) {

                        if ( team_win_lose_fetched && ( System.currentTimeMillis()/1000 - LAST_BET_PLACED ) > ShowFragments.BETTING_TIME ) {
                            lay_dialouge1 dialouge2 = new lay_dialouge1(MatchDetailsActivity.this, R.style.Theme_Dialog, team_1.getText().toString(),
                                    team_2.getText().toString(), susername, balances, lay_1.getText().toString(), event_id, opendate, dis_id, "1", match_name, team1_result.getText().toString(),
                                    team2_result.getText().toString());
                            dialouge2.show();
                        } else {
                            Toast.makeText(MatchDetailsActivity.this, "please wait", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(MatchDetailsActivity.this, "You are blocked", Toast.LENGTH_SHORT).show();

                    }

                break;
            case R.id.lay_2:

                    if (status.equals("1")) {

                        if ( team_win_lose_fetched && ( System.currentTimeMillis()/1000 - LAST_BET_PLACED ) > ShowFragments.BETTING_TIME ) {

                            lay_dialouge1 dialouge2 = new lay_dialouge1(MatchDetailsActivity.this, R.style.Theme_Dialog, team_1.getText().toString(),
                                    team_2.getText().toString(), susername, balances, lay_2.getText().toString(), event_id, opendate, dis_id, "2", match_name, team1_result.getText().toString(),
                                    team2_result.getText().toString());
                            dialouge2.show();
                        } else {
                            Toast.makeText(MatchDetailsActivity.this, "please wait", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(MatchDetailsActivity.this, "You are blocked", Toast.LENGTH_SHORT).show();

                    }

                break;
        }
    }

    public static void get_user_info(Context c) {


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "get_user_info");
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);


                try {

                    balances = response.getString("bal");

                    limit = response.getString("limit_");

                    status = response.getString("status");


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


        requestQueue.add(jsonObjectRequest);

    }


    public void team_win_lose() {


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "team_win_lose");
            jsonObject.put("username", username);
            jsonObject.put("team1", team_1.getText().toString());
            jsonObject.put("team2", team_2.getText().toString());

            jsonObject.put("event_id", event_id + opendate.toLowerCase().replace(" ", ""));
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
                        team1_result.setVisibility(View.VISIBLE);
                        team2_result.setVisibility(View.VISIBLE);

                        team1_result.setText(response.getString(team_1.getText().toString()));

                        team2_result.setText(response.getString(team_2.getText().toString()));

                        if (Integer.parseInt(response.getString(team_1.getText().toString())) < 0) {


                            team1_result.setBackground(MatchDetailsActivity.this.getResources().getDrawable(R.drawable.lose_rounded_background));


                        } else {


                            team1_result.setBackground(MatchDetailsActivity.this.getResources().getDrawable(R.drawable.profit_rounded_background));


                        }

                        if (Integer.parseInt(response.getString(team_2.getText().toString())) < 0) {

                            team2_result.setBackground(MatchDetailsActivity.this.getResources().getDrawable(R.drawable.lose_rounded_background));


                        } else {


                            team2_result.setBackground(MatchDetailsActivity.this.getResources().getDrawable(R.drawable.profit_rounded_background));


                        }
                    } else {

                        team1_result.setText("0");
                        team2_result.setText("0");
                        team1_result.setVisibility(View.GONE);
                        team2_result.setVisibility(View.GONE);
                    }

                    team_win_lose_fetched = true;

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


        requestQueue.add(jsonObjectRequest);

    }

    public static void force_fetch_team_win_lose(final Context context) {


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "team_win_lose");
            jsonObject.put("username", username);
            jsonObject.put("team1", team_1.getText().toString());
            jsonObject.put("team2", team_2.getText().toString());

            jsonObject.put("event_id", event_id + opendate.toLowerCase().replace(" ", ""));
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
                        team1_result.setVisibility(View.VISIBLE);
                        team2_result.setVisibility(View.VISIBLE);

                        team1_result.setText(response.getString(team_1.getText().toString()));

                        team2_result.setText(response.getString(team_2.getText().toString()));

                        if (Integer.parseInt(response.getString(team_1.getText().toString())) < 0) {
                            team1_result.setBackground(context.getResources().getDrawable(R.drawable.lose_rounded_background));
                        } else {
                            team1_result.setBackground(context.getResources().getDrawable(R.drawable.profit_rounded_background));
                        }

                        if (Integer.parseInt(response.getString(team_2.getText().toString())) < 0) {
                            team2_result.setBackground(context.getResources().getDrawable(R.drawable.lose_rounded_background));
                        } else {
                            team2_result.setBackground(context.getResources().getDrawable(R.drawable.profit_rounded_background));
                        }
                    } else {

                        team1_result.setText("0");
                        team2_result.setText("0");
                        team1_result.setVisibility(View.GONE);
                        team2_result.setVisibility(View.GONE);
                    }

                    team_win_lose_fetched = true;

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


        requestQueue.add(jsonObjectRequest);

    }


    private class SessionAdapter extends RecyclerView.Adapter<session_view_holder> {

        @Override
        public session_view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new session_view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.session_run_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(session_view_holder holder, int position) {

            try {
                JSONArray session_jsonArray1 = session_jsonArray.getJSONObject(position + 1).getJSONArray("runners");

                JSONObject jsonObject = session_jsonArray1.getJSONObject(0);

                holder.session_name.setText(jsonObject.getString("name"));

                holder.lay_line.setText(String.valueOf(jsonObject.getJSONArray("lay").getJSONObject(0).getInt("line")));

                holder.lay_price.setText(String.valueOf(jsonObject.getJSONArray("lay").getJSONObject(0).getInt("price")));


                holder.back_line.setText(String.valueOf(jsonObject.getJSONArray("back").getJSONObject(0).getInt("line")));


                holder.back_price.setText(String.valueOf(jsonObject.getJSONArray("back").getJSONObject(0).getInt("price")));

                if (session_jsonArray.getJSONObject(position + 1).getString("statusLabel").equals("Ball Running")) {
                    holder.ball_running.setVisibility(View.VISIBLE);
                } else {
                    holder.ball_running.setVisibility(View.GONE);

                }

                if (session_jsonArray.getJSONObject(position + 1).getString("status").equals("SUSPENDED")) {
                    holder.suspended.setVisibility(View.VISIBLE);
                } else {
                    holder.suspended.setVisibility(View.GONE);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return session_jsonArray.length() - 1;
        }
    }


    private class session_view_holder extends RecyclerView.ViewHolder {

        private TextView session_name, lay_line, lay_price, back_line, back_price;

        private LinearLayout ball_running, suspended;

        public session_view_holder(View itemView) {
            super(itemView);

            session_name = itemView.findViewById(R.id.session_name);

            lay_line = itemView.findViewById(R.id.lay_line);

            lay_price = itemView.findViewById(R.id.lay_price);

            back_line = itemView.findViewById(R.id.back_line);

            back_price = itemView.findViewById(R.id.back_price);

            ball_running = itemView.findViewById(R.id.ball_running);

            suspended = itemView.findViewById(R.id.suspended);

        }
    }
}
