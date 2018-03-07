package com.inception.betappuser.fragments;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.inception.betappuser.R;

import static android.content.Context.MODE_PRIVATE;


public class UserDetails extends Fragment {
     TextView balance,name,limit;
    ImageView active;
    String names,balances,actives,limits;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_user_details, container, false);
        name = v.findViewById(R.id.name_txt);
        active=v.findViewById(R.id.active);
        balance= v.findViewById(R.id.balance);
        limit=v.findViewById(R.id.limit);
        SharedPreferences sp = getActivity().getSharedPreferences("user_info" , MODE_PRIVATE);
       names= sp.getString("username","");
       actives=sp.getString("status","");
       balances =sp.getString("balance","");
        limits =sp.getString("limit","");
        name.setText(names);
        balance.setText(balances);
        limit.setText(limits);
        if(actives.equals("1"))
        {
            active.setImageDrawable(getResources().getDrawable(R.drawable.active_user));

        }

        else {
            active.setImageDrawable(getResources().getDrawable(R.drawable.block_user));

        }
        return  v;
    }



}
