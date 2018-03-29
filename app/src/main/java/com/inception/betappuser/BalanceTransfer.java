package com.inception.betappuser;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import java.lang.reflect.Method;

public class BalanceTransfer extends AppCompatActivity {

    public static BalanceTransfer instance ;

    RequestQueue requestQueue ;

    public static String balance ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_transfer);

        instance = this ;

       requestQueue = Volley.newRequestQueue(BalanceTransfer.this);

        getSupportActionBar().setTitle("Transfer Balance");

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get_user_info();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return  true;
    }

    public void verify_user(View view) {

        final EditText username_et = findViewById(R.id.username_et);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module" , "verify_user");
            jsonObject.put("username" , username_et.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);


                SharedPreferences sp = getSharedPreferences("user_info" , MODE_PRIVATE);
                String savedid = sp.getString("dis_id","");

                try {
                    if(response.getJSONObject("result").getString("dis_id").equals(savedid))
                    {
                        Toast.makeText(BalanceTransfer.this , "you can transfer balance" , Toast.LENGTH_SHORT).show();

                        new BalanceTransferDialog(BalanceTransfer.this , R.style.Theme_Dialog , username_et.getText().toString()).show();

                    }
                    else {

                        Toast.makeText(BalanceTransfer.this , "username is not under same distributor" , Toast.LENGTH_SHORT).show();
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


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000 , 2,2 ));



        requestQueue.add(jsonObjectRequest);

    }

    public  void get_user_info()
    {
        SharedPreferences sp = getSharedPreferences("user_info" , MODE_PRIVATE);
        String username = sp.getString("username","");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module" , "get_user_info");
            jsonObject.put("username" , username );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);




                try {

                    balance = response.getString("bal");

                   // limits = response.getString("limit_");

                  //  actives = response.getString("status");





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


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000 , 2,2 ));




        requestQueue.add(jsonObjectRequest);

    }
}
