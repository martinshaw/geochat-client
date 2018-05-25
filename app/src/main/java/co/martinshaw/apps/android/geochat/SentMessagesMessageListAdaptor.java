/*
 * SentMessagesMessageListAdaptor
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/SentMessagesMessageListAdaptor.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 25/05/18 03:24 <martin>
 * Last Compilation: 25/05/18 03:32
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class SentMessagesMessageListAdaptor extends ArrayAdapter {

    List<Message> messagesList;
    Context context;

    String googleAPIKey;

    TextView nextTextViewForGeocoding;

    public SentMessagesMessageListAdaptor(Context context, List<Message> list)
    {
        super(context,0,list);
        this.messagesList = list;
        this.context = context;

        getGoogleAPIKey();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(getContext());
            convertView = mInflater.inflate(R.layout.component_messagelist_item,parent,false);

            holder = new ViewHolder();
            holder.icon_cont =(ImageView) convertView.findViewById(R.id.icon_cont);
            holder.firstLine =(TextView) convertView.findViewById(R.id.firstLine);
            holder.secondLine =(TextView) convertView.findViewById(R.id.secondLine);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        Message in = (Message) messagesList.get(position);

        processImageWithStaticMap(holder.icon_cont, in);

        holder.firstLine.setText(in.origin_lat+" "+in.origin_long);
//        nextTextViewForGeocoding = holder.firstLine;
//        getGeocodingInfo(String.valueOf(in.origin_lat), String.valueOf(in.origin_long));

        holder.secondLine.setText(in.contents);
        // set the name to the text;

        return convertView;

    }

    static class ViewHolder
    {
        ImageView icon_cont;
        TextView firstLine;
        TextView secondLine;
    }


    public void getGoogleAPIKey(){

        // Get GOOOLE MAP API KEY for later use in URL
        try {
            ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            this.googleAPIKey = bundle.getString("com.google.android.geo.API_KEY");
            Log.i("GETTINGAPIKEY", "Found Google Maps API Key: "+this.googleAPIKey);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GETTINGAPIKEY", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("GETTINGAPIKEY", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }





    public void processImageWithStaticMap(final ImageView _image, Message _msg){
        // Construct Google Map Static image request URL
        String mapButtonStaticImageURL ="https://maps.googleapis.com/maps/api/staticmap" +
                "?center=" + _msg.origin_lat + "," + _msg.origin_long +
                "&format=png" +
                "&zoom=18" +
                "&size=180x180" +
                "&maptype=road" +
                "&key="+googleAPIKey +
                "&style=feature:poi|visibility:off" +
                "&style=feature:administrative|visibility:off" +
                "&style=feature:road|visibility:simplified" +
                "&style=feature:transit|visibility:off";

        Picasso
                .with(getContext())
                .load(mapButtonStaticImageURL)
                .into(new Target(){
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        _image.setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        Log.d("PICASSO", "Failed!");
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                        Log.d("PICASSO", "Prepare Load");
                    }
                });
    }





//    public void getGeocodingInfo(String _lat, String _long){
//        String _url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+_lat+","+_long+"&key="+googleAPIKey;
//
//        doOkHTTPRequestCall(_url, new Callback() {
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//
//                if (response.isSuccessful()) {
//
//                    // Got geocoding data, will process into Object
//                    String _data= response.body().string();
//                    try {
//
//                        JSONObject reader = new JSONObject(_data);
//
//
//                        Log.i("GEOCODE", reader.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
//                        Log.i("GEOCODE",
//                                String.valueOf(reader.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"))
//                                        + ", "+
//                                String.valueOf(reader.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"))
//                        );
//
//
//                        setGeocodingInfoToView(reader.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                } else {
//                    // Request not successful
//                }
//
//            }
//
//        });
//    }
//
//
//    public void setGeocodingInfoToView(String address){
//        nextTextViewForGeocoding.setText(address);
//        runOnUiThread(new Runnable() {
//            public void run() {
//                ui.myMethod(message);
//            }
//        });
//    }








    MediaType OkHTTP_DataType_JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient OkHTTP_Client = new OkHttpClient();

    Call doOkHTTPRequestCall(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = OkHTTP_Client.newCall(request);
        call.enqueue(callback);
        return call;
    }


}