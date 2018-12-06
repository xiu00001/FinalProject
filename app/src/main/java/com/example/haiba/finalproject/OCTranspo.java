package com.example.haiba.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class OCTranspo extends AppCompatActivity {

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);


        Toolbar toolbar_oc = findViewById(R.id.toolbar);


        Button stop = (Button) findViewById(R.id.stops);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OCTranspo.this, Stops.class);
                startActivity(intent);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_oc, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        mi.getItemId();
        switch(mi.getItemId()){

            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(OCTranspo.this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.activity_help_info, null);
                builder.setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.back:
                  AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                  builder2.setTitle(R.string.goBack);
                  builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Intent intent = new Intent(OCTranspo.this,MainActivity.class);
                          startActivity(intent);
                      }
                  });

                  builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  });
                  AlertDialog dialog2 = builder2.create();
                  dialog2.show();
                  break;
        }
        return true;
    }
}
