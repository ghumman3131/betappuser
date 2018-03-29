package com.inception.betappuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StatementsActivity extends AppCompatActivity {

    private RequestQueue requestQueue ;

    private JSONArray jsonArray ;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);

        getSupportActionBar().setTitle("Statements");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(StatementsActivity.this);

        recyclerView = findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(StatementsActivity.this , LinearLayoutManager.VERTICAL , false));

        get_statements();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return true;
    }

    public void get_statements()
    {
        SharedPreferences sp = getSharedPreferences("user_info" , MODE_PRIVATE);
        String username = sp.getString("username","");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module" , "get_statements");
            jsonObject.put("username" , username );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);





                try {
                    jsonArray = response.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Adapter adapter = new Adapter();

                recyclerView.setAdapter(adapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);

            }
        });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000 , 2,2 ));




        requestQueue.add(jsonObjectRequest);
    }

    public  class  view_holder extends RecyclerView.ViewHolder{

        TextView amount , match_name , date_txt , match_fee , total;

        public view_holder(View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            match_name = itemView.findViewById(R.id.match_name);
            date_txt = itemView.findViewById(R.id.date_txt);

            match_fee = itemView.findViewById(R.id.match_fee);

            total = itemView.findViewById(R.id.total);

        }
    }

    private class Adapter extends RecyclerView.Adapter<view_holder> {
        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.statement_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(final view_holder holder, int position) {

            try {
                final JSONObject jsonObject = jsonArray.getJSONObject(position);

                holder.amount.setText("-"+jsonObject.getString("match_fee"));

                holder.match_name.setText(jsonObject.getString("match_name"));

                holder.date_txt.setText(getDate(jsonObject.getString("datee")));

                holder.match_fee.setText(jsonObject.getString("match_fee"));

                holder.total.setText("-"+jsonObject.getString("match_fee"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }
    }

    private String getDate(String OurDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date value = formatter.parse(OurDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM, dd, yyyy"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);

            //Log.d("OurDate", OurDate);
        } catch (Exception e) {
            OurDate = "00-00-0000 00:00";
        }
        return OurDate;
    }


}
