/*
 * MessageListAdaptor
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/MessageListAdaptor.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 25/05/18 00:16 <martin>
 * Last Compilation: 25/05/18 00:16
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
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

class ThisAreaMessageListAdaptor extends ArrayAdapter {

    List<Message> messagesList;
    Context context;

    String googleAPIKey;

    public ThisAreaMessageListAdaptor(Context context, List<Message> list)
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
        holder.firstLine.setText(in.first_name + " " + in.last_name);
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


}