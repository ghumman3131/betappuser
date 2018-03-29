package com.inception.betappuser.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inception.betappuser.BalanceTransfer;
import com.inception.betappuser.BalanceTransferDialog;
import com.inception.betappuser.R;
import com.inception.betappuser.url;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class UserDetails extends Fragment {
     TextView balance,name,limit;
    ImageView active;
    EditText change_password;
    String names,actives,limits ;

    public static String bal ;

    Button change_pass ;

    RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_user_details, container, false);
        name = v.findViewById(R.id.name_txt);
        active=v.findViewById(R.id.active);
        balance= v.findViewById(R.id.balance);
        change_password=v.findViewById(R.id.chnge_pass);
        limit=v.findViewById(R.id.limit);


        requestQueue = Volley.newRequestQueue(getActivity());

        SharedPreferences sp = getActivity().getSharedPreferences("user_info" , MODE_PRIVATE);
       names= sp.getString("username","");

        name.setText(names);



        change_pass = v.findViewById(R.id.change_pass);


        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                change_pass();
            }
        });
        return  v;
    }

    public void change_pass() {
        final String pass = change_password.getText().toString();
        if (pass.equals(""))
        {
            Toast.makeText(getActivity(), "enter password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DialogInterface.OnClickListener dialogClickListener9 = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("module", "change_password");
                                jsonObject.put("username", name.getText().toString());
                                jsonObject.put("password", pass);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url.ip, jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    System.out.println(response);

                                    try {

                                        if (response.getString("result").equals("done")) {


                                            Toast.makeText(getActivity(), "password changed successfully", Toast.LENGTH_SHORT).show();


                                        } else {

                                            Toast.makeText(getActivity(), "error try again", Toast.LENGTH_SHORT).show();
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

                            Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }

                }
            };
            AlertDialog.Builder ab9 = new AlertDialog.Builder(getActivity());
            ab9.setMessage("Do you want to change password?").setPositiveButton("Yes", dialogClickListener9)
                    .setNegativeButton("No", dialogClickListener9).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        get_user_info();
    }

    public  void get_user_info()
    {
        SharedPreferences sp = getActivity().getSharedPreferences("user_info" , MODE_PRIVATE);
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

                    bal = response.getString("bal");

                    limits = response.getString("limit_");

                    actives = response.getString("status");

                    limit.setText(limits);

                    if(actives.equals("1"))
                    {
                        active.setImageDrawable(getResources().getDrawable(R.drawable.active_user));

                    }

                    else {
                        active.setImageDrawable(getResources().getDrawable(R.drawable.block_user));

                    }

                    balance.setText(bal);

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
