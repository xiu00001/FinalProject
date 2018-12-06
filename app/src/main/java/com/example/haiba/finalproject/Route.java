package com.example.haiba.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Route extends Activity {

    protected static final String ACTIVITY_NAME = "Route";
    String number;
    ProgressBar progressBar;
    TextView stopName;
    ListView routeList;
    String stopNameStr;
    Bundle bundle = new Bundle();
    ArrayList<String> resultArr;
    ArrayList<String> routeArr = new ArrayList<>();
    ArrayList<String> destinationArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        routeList = findViewById(R.id.routeList);
        stopName = findViewById(R.id.stopName);

        bundle = this.getIntent().getExtras();
        number = bundle.getString("stopList");

        OCTranspoAPI oct = new OCTranspoAPI();
        String urlString = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=";
        oct.execute(urlString);
    }

    public class OCTranspoAPI extends AsyncTask<String, Integer, String> {

            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                //source: https://blog.csdn.net/sinat_38321889/article/details/72897562

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(Route.this,android.R.layout.simple_list_item_1,resultArr);
                routeList.setAdapter(adapter);
                progressBar.setProgress(100);
                progressBar.setVisibility(View.INVISIBLE);

                routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(Route.this, "You clicked route " + routeArr.get(i), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Route.this,Route_details.class);
                        intent.putExtra("stopList",i);
                        bundle.putString("routeList",routeArr.get(i));
                        intent.putExtras(bundle);
                        startActivityForResult(intent,100);
                    }
                });
            }


            @Override
            protected String doInBackground(String... args) {
                HttpURLConnection conn = null;
                String inputURL = args[0];

                try {
                    resultArr = downloadInfo(conn,inputURL);
                }catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            /*
             * Connect to web server
             * */
            private ArrayList<String> downloadInfo(HttpURLConnection conn,String inputURL) throws IOException{
                ArrayList<String> resultArr = new ArrayList<>();
                URL url;
                InputStream resultStream = null;

                try {

                    url = new URL(inputURL+number);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    //Start the connection with webserver
                    conn.connect();
                    progressBar.setProgress(10);
                    //get the response code
                    //https://developer.android.com/training/basics/network-ops/connecting.html
                    int responseCode = conn.getResponseCode();

                    //handle the connection error
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + responseCode);
                    }
                    //get the output stream
                    resultStream = conn.getInputStream();
                    if(resultStream != null){
                        progressBar.setProgress(30);
                        resultArr = readStream(resultStream);
                    }

                } catch (MalformedURLException e) {
                }
                catch (IOException e) {
                } finally {

                    if (resultStream != null) {
                        resultStream.close();
                    }
                    if (resultStream != null) {
                        conn.disconnect();
                    }
                }
                return resultArr;

            }

            private ArrayList<String> readStream(InputStream resultStream){

                ArrayList <String> result = new ArrayList<>();

                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(resultStream, "UTF-8");
                    progressBar.setProgress(70);

                    while((parser.getEventType())!= XmlPullParser.END_DOCUMENT){
                        if(parser.getEventType() == XmlPullParser.START_TAG){

                            String tag = parser.getName();
                            if(tag.equalsIgnoreCase("stopdescription")){
                                stopNameStr = parser.nextText();

                            }else if(tag.equalsIgnoreCase("routeno")){
                                routeArr.add(parser.nextText());

                            }else if(tag.equalsIgnoreCase("routeheading")){
                                destinationArr.add(parser.nextText());

                            }

                        }

                        parser.next();
                    }


                        for (int i = 0; i<routeArr.size();i++){
                            result.add(routeArr.get(i) + " -> " + destinationArr.get(i));
                        }

                    stopName.setText(stopNameStr);

                }catch(Exception ex){
                    ex.printStackTrace();

                }



                return result;
            }


    }
}
