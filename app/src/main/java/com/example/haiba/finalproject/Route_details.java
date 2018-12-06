package com.example.haiba.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;

public class Route_details extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        RouteFragment rf = new RouteFragment();

        bundle.putString("stopList",intent.getStringExtra("stopList"));
        bundle.putString("routeList",intent.getStringExtra("routeList"));
        rf.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.detail,rf).commit();
    }
}
