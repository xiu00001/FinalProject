package com.example.haiba.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 Main menu
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        in
         */
        Button food = findViewById(R.id.food);
        Button news = findViewById(R.id.news);
        Button movie = findViewById(R.id.startMovie);
        Button oc = findViewById(R.id.ocTranspo);

        oc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OCTranspo.class);
                startActivity(intent);
            }
        });

    }
}
