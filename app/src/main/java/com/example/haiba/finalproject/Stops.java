package com.example.haiba.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Stops extends Activity {

    protected static final String ACTIVITY_NAME = "Stops";
    ListView stopList;
    EditText edit;
    Button addNumber;
    ArrayList<String> stopNumber = new ArrayList<>();
    StopsAdapter messageAdapter;
    StopsDatabaseHelper dbHelper = new StopsDatabaseHelper(this);
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);
        addNumber = (Button)findViewById(R.id.addStop);
        stopList = (ListView)findViewById(R.id.stopList);
        edit = (EditText)findViewById(R.id.stopText);

        database = dbHelper.getWritableDatabase();
        cursor = database.query(StopsDatabaseHelper.TABLE_NAME,new String[]{StopsDatabaseHelper.KEY_STOP},null,null,null,null,null);
        cursor.moveToNext();

        while(!cursor.isAfterLast()){
            String content = cursor.getString(cursor.getColumnIndex(StopsDatabaseHelper.KEY_STOP));
            stopNumber.add(content);
            Log.i(ACTIVITY_NAME,"SQL MESSAGE:" + content);
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME,"Cursor's column count = " + cursor.getColumnCount());
        for(int i = 0;i<cursor.getColumnCount();i++){
            Log.i(ACTIVITY_NAME,"The column name is "+cursor.getColumnName(i));
        }

        messageAdapter = new StopsAdapter(this);
        stopList.setAdapter(messageAdapter);

        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stops = edit.getText().toString();
                stopNumber.add(stops);

                ContentValues cv = new ContentValues();
                cv.put(StopsDatabaseHelper.KEY_STOP,stops);
                database.insert(StopsDatabaseHelper.TABLE_NAME,null,cv);
                messageAdapter.notifyDataSetChanged();
                edit.setText("");
            }
        });

        stopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Stops.this,Route.class);
                Bundle bundle = new Bundle();
                bundle.putString("stopList",stopNumber.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        stopList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(Stops.this);
                builder.setTitle("Do you want to delete this stop?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(stopNumber.remove(position)!=null){
                            System.out.println("success");
                        }else {
                            System.out.println("failed");
                        }
                        stopList.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return true;
            }
        });


    }

    private class StopsAdapter extends ArrayAdapter<String> {
        private StopsAdapter(Context ctx){
            super(ctx,0);
        }

        @Override
        public int getCount(){return stopNumber.size();}

        @Override
        public String getItem(int position){return stopNumber.get(position);}

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = Stops.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.stop_row,null);
            TextView message = (TextView)result.findViewById(R.id.stopNo);
            message.setText(getItem(position));
            return result;
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        database.close();
    }


}



