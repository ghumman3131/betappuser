package com.inception.betappuser;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchOddDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView team_1, back_1, lay_1, team_2, back_2, lay_2 ;
    String balances;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_odd_details);

        getSupportActionBar().setTitle("Match Odds");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp_get = getSharedPreferences("user_info", MODE_PRIVATE);
        balances = sp_get.getString("balance", "");
        team_1 = findViewById(R.id.team_1);
        team_2 = findViewById(R.id.team_2);



        back_1 = findViewById(R.id.back_1);
        back_2 = findViewById(R.id.back_2);

        lay_1 = findViewById(R.id.lay_1);
        lay_2 = findViewById(R.id.lay_2);
        back_1.setOnClickListener(this);
        back_2.setOnClickListener(this);
        lay_1.setOnClickListener(this);
        lay_2.setOnClickListener(this);

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

    public void getdata() {
        String event_id = getIntent().getStringExtra("event_id");

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
                SharedPreferences.Editor sp_dialog = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                sp_dialog.putString("team1", team_1.getText().toString());
                sp_dialog.putString("team2", team_2.getText().toString());
                sp_dialog.putString("back1", back_1.getText().toString());
                sp_dialog.putString("balance", balances);
                sp_dialog.commit();
                betting_dialouge1 dialouge = new betting_dialouge1(MatchOddDetails.this, R.style.Theme_Dialog);
                dialouge.show();
                break;
            case R.id.back_2:
                SharedPreferences.Editor sp_dialog1 = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                sp_dialog1.putString("team1", team_1.getText().toString());
                sp_dialog1.putString("team2", team_2.getText().toString());
                sp_dialog1.putString("back2", back_2.getText().toString());
                sp_dialog1.putString("balance", balances);
                sp_dialog1.commit();
                betting_dialouge2 dialouge1 = new betting_dialouge2(MatchOddDetails.this, R.style.Theme_Dialog);

                dialouge1.show();
                break;
            case R.id.lay_1:
                SharedPreferences.Editor sp_dialog2 = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                sp_dialog2.putString("team1", team_1.getText().toString());
                sp_dialog2.putString("team2", team_2.getText().toString());
                sp_dialog2.putString("lay1", lay_1.getText().toString());
                sp_dialog2.putString("balance", balances);
                sp_dialog2.commit();
                lay_dialouge1 dialouge2 = new lay_dialouge1(MatchOddDetails.this, R.style.Theme_Dialog);
                dialouge2.show();
                break;
            case R.id.lay_2:

                SharedPreferences.Editor sp_dialog3 = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                sp_dialog3.putString("team1", team_1.getText().toString());
                sp_dialog3.putString("team2", team_2.getText().toString());
                sp_dialog3.putString("lay2", lay_2.getText().toString());
                sp_dialog3.putString("balance", balances);
                sp_dialog3.commit();
                lay_dialouge2 dialouge3 = new lay_dialouge2(MatchOddDetails.this, R.style.Theme_Dialog);
                dialouge3.show();
                break;
        }
    }
}
