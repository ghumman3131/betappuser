package com.inception.betappuser.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.inception.betappuser.MatchOddDetails;
import com.inception.betappuser.R;
import com.inception.betappuser.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFragments extends Fragment {

    private JSONArray jsonArray;
    String saved_disid;
    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;

    private RequestQueue mRequestQueue;

    ArrayList<String> blocked_id;

    public ShowFragments() {
// Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show_fragments, container, false);


        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        SharedPreferences sp = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        saved_disid = sp.getString("dis_id", "");

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait");
        progressDialog.show();


        mRequestQueue = Volley.newRequestQueue(getContext());


        get_data();

        return v;
    }


    private void get_data() {
        block_detail();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://www.lotusbook.com/api/exchange/eventType/4", new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);

                progressDialog.hide();

                try {
                    jsonArray = response.getJSONArray("result");


                    Log.i("adapter to recycler --", "");
                    Adapter adapter = new Adapter();
                    recyclerView.setAdapter(adapter);

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


        mRequestQueue.add(jsonObjectRequest);


    }

    public void block_detail() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("module", "block_detail");
            jsonObject.put("dis_id", saved_disid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url.ip, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(response);

                try {

                    JSONArray event = response.getJSONArray("result");
                    for (int i = 0; i < event.length(); i++) {
                        blocked_id.add(event.getJSONObject(i).getString("event_id"));
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
    }

    private class Adapter extends RecyclerView.Adapter<view_holder> {
        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.betting_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(final view_holder holder, int position) {

            try {
                final JSONObject jsonObject = jsonArray.getJSONObject(position).getJSONObject("event");

                holder.match_vs.setText(jsonObject.getString("name"));
                holder.date_time.setText(getDate(jsonObject.getString("openDate")));
                if (blocked_id.contains(jsonObject.getString("id"))) {
                    holder.match_vs.setOnClickListener(null);
                    holder.cell_layout.setAlpha((float) 0.7);
                    holder.block_status.setBackgroundColor(Color.RED);
                    holder.block_status.setText("BLOCKED");
                } else {
                    holder.cell_layout.setAlpha(1);
                    holder.match_vs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(getContext(), MatchOddDetails.class);
                            try {
                                i.putExtra("event_id", jsonObject.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            startActivity(i);

                        }
                    });
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


    private class view_holder extends RecyclerView.ViewHolder {
        TextView match_vs, date_time, open_status, block_status;
        RelativeLayout cell_layout;

        public view_holder(View itemView) {
            super(itemView);
            cell_layout = itemView.findViewById(R.id.cell_layout);
            match_vs = itemView.findViewById(R.id.match_vs);
            date_time = itemView.findViewById(R.id.date_time);
            open_status = itemView.findViewById(R.id.open_status);
            block_status = itemView.findViewById(R.id.block_status);
        }
    }

    private String getDate(String OurDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date value = formatter.parse(OurDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM, dd, yyyy, hh:mm:ss aa"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);

            //Log.d("OurDate", OurDate);
        } catch (Exception e) {
            OurDate = "00-00-0000 00:00";
        }
        return OurDate;
    }

}
