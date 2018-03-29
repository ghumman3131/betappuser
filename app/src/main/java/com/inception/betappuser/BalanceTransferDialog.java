package com.inception.betappuser;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inception.betappuser.fragments.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by charanghumman on 19/03/18.
 */

public class BalanceTransferDialog extends Dialog {


    private  String username ;

    private Context context ;

    private Button send_btn , cancel_btn ;

    public BalanceTransferDialog(@NonNull Context context, int themeResId , String username)
    {
        super(context, themeResId);

        this.username = username ;

        this.context = context;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.balance_transfer_dialog);

        send_btn = findViewById(R.id.send_btn);

        cancel_btn = findViewById(R.id.cancel_btn);


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send_balance();

            }
        });
    }

    public void send_balance() {

        SharedPreferences sp = context.getSharedPreferences("user_info" , context.MODE_PRIVATE);


        final EditText balance_et = findViewById(R.id.balance_et);

        if(Integer.parseInt(balance_et.getText().toString()) <= Integer.parseInt(BalanceTransfer.balance))
        {

        }
        else {
            Toast.makeText(context , "you don't have sufficient balance" , Toast.LENGTH_SHORT).show();
            return;

        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module" , "transfer_balance");
            jsonObject.put("to_username" , username);
            jsonObject.put("from_username" , sp.getString("username",""));
            jsonObject.put("balance" , balance_et.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);




                try {
                    if(response.getString("result").equals("done"))
                    {
                        Toast.makeText(context , "done" , Toast.LENGTH_SHORT).show();

                        dismiss();

                        BalanceTransfer.instance.finish();


                    }
                    else {

                        Toast.makeText(context , "error" , Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);


        requestQueue.add(jsonObjectRequest);

    }
}
