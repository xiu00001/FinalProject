package com.example.haiba.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RouteFragment extends Fragment {

    private TextView destination;
    private TextView latitude;
    private TextView longitude;
    private TextView speed;
    private TextView startTime;
    private TextView adjusted;

    private Bundle bundle;

    public RouteFragment(){super();}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.route_fragment,container,false);
        destination = view.findViewById(R.id.des);
        latitude = view.findViewById(R.id.lati);
        longitude = view.findViewById(R.id.longi);
        speed = view.findViewById(R.id.speed);
        startTime = view.findViewById(R.id.startTime);
        adjusted = view.findViewById(R.id.adj);

        RouteQuery rq = new RouteQuery();
        rq.execute();

        return view;
    }

    public class RouteQuery extends AsyncTask<String,Integer,String>{
        String destName;
        String latiStr;
        String longiStr;
        String speedStr;
        String startStr;
        String adjStr;

        Route route = new Route();
        String stopNo = bundle.getString("stopList");
        String routeNo = bundle.getString("routeList");

        @Override
        protected String doInBackground(String...strings){
            String urlString = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+stopNo+"&routeNo="+routeNo;


            try{
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream in = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(in, "UTF-8");

                while(parser.next() != XmlPullParser.END_DOCUMENT){
                    if(parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                        String tag = parser.getName();
                        if (tag.equalsIgnoreCase("TripDestination")) {
                            destName = parser.nextText();

                        } else if (tag.equalsIgnoreCase("latitude")) {
                            latiStr = parser.nextText();

                        } else if (tag.equalsIgnoreCase("longitude")) {
                            longiStr = parser.nextText();

                        } else if (tag.equalsIgnoreCase("gpsspeed")) {
                            speedStr = parser.nextText();

                        } else if (tag.equalsIgnoreCase("tripstarttime")) {
                            startStr = parser.nextText();

                        } else if (tag.equalsIgnoreCase("adjustedscheduletime")) {
                            adjStr = parser.nextText();

                        }
                }
                conn.disconnect();
            }catch(IOException e){
                e.printStackTrace();
            }catch(XmlPullParserException e){
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String Result){
            destination.setText("Destination= " + destName);
            latitude.setText("Latitude= " + latiStr);
            longitude.setText("Longitude= " + longiStr);
            speed.setText("Speed= " + speedStr);
            startTime.setText("Start time= " + startStr);
            adjusted.setText("Adjusted schedule time= " + adjStr);
        }
    }



}
