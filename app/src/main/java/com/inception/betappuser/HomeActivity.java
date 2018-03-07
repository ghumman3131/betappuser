package com.inception.betappuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.inception.betappuser.fragments.ShowFragments;
import com.inception.betappuser.fragments.UserDetails;

public class HomeActivity extends AppCompatActivity {



    private ActionBarDrawerToggle toggle;

    DrawerLayout drawerLayout;

    FragmentManager fm ;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    open_home();
                    return true;
                case R.id.navigation_refresh:

                    return true;
                case R.id.navigation_user:

                    open_profile();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Betting Admin");

        fm = getSupportFragmentManager();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        open_home();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            if(drawerLayout.isDrawerOpen(Gravity.START))
            {
                drawerLayout.closeDrawer(Gravity.START);
            }
            else {
                drawerLayout.openDrawer(Gravity.START);
            }
        }

        return true;
    }

    private void open_home()
    {
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.main_frame , new ShowFragments());

        ft.commit();
    }

    private void open_profile()
    {

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.main_frame , new UserDetails());

        ft.commit();

    }

    public void logout(View view) {

        SharedPreferences.Editor sp_home = getSharedPreferences("user_info" , MODE_PRIVATE).edit();

        sp_home.clear();
        sp_home.commit();

        startActivity(new Intent(HomeActivity.this , LoginActivity.class));

        finish();
    }

    public void open_profile(View view) {

        drawerLayout.closeDrawer(Gravity.START);

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.main_frame , new UserDetails());

        ft.commit();

    }

    public void open_home(View view) {

        drawerLayout.closeDrawer(Gravity.START);

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.main_frame , new ShowFragments());

        ft.commit();

    }

}
