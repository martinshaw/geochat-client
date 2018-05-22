/*
 * MainThisAreaFragment
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/MainThisAreaFragment.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 21/05/18 15:03 <martin>
 * Last Compilation: 21/05/18 15:03
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainThisAreaFragment extends android.support.v4.app.Fragment {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private OnReceiveDataFromFragmentListener mActivityListener;

    public MainActivity mainActivity;

    MediaType OkHTTP_DataType_JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient OkHTTP_Client = new OkHttpClient();

    public String googleAPIKey = "";

    public View rootView;
    public LinearLayout mMapButton;
    public TextView mMapButtonTitle;
    public TextView mMapButtonSubtitle;






    public MainThisAreaFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance() {
//    public static MainThisAreaFragment newInstance(String param1, String param2) {
        android.support.v4.app.Fragment _fragment = new MainThisAreaFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        _fragment.setArguments(args);
        return _fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_this_area, container, false);

        mMapButton = rootView.findViewById(R.id.main_this_area_mapbutton);
        mainActivity = (MainActivity) getActivity();



        // Get and store Google API key
        getGoogleAPIKey();


        // Set background of MapButton as Google Map Static image of current location
        setupMapButtonClickAndBackground();


        // Set MapButton text as result of Geocoding
        try {
            setupMapButtonGeocoding();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return rootView;
    }






    void getGoogleAPIKey () {
        // Get GOOOLE MAP API KEY for later use in URL
        try {
            ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            this.googleAPIKey = bundle.getString("com.google.android.geo.API_KEY");
            Log.i("GETTINGAPIKEY", "Found Google Maps API Key: "+this.googleAPIKey);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GETTINGAPIKEY", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("GETTINGAPIKEY", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }






    private void setupMapButtonClickAndBackground() {

        // Add click event to open MapActivity
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivityListener.onReceiveDataFromFragment("openMapView", "");

            }
        });

        //      View.post runs after the view element has been laid out according to layout_width and layout_height.
        //      _mapButton_width & _mapButton_height should be actual pixels for use with Picasso image cropping
        mMapButton.post(new Runnable() {

            @Override
            public void run() {

                // Construct Google Map Static image request URL
                String mapButtonStaticImageURL ="https://maps.googleapis.com/maps/api/staticmap" +
                        "?center=Manchester" +
                        "&format=png" +
                        "&zoom=16" +
                        "&size=600x300" +
                        "&maptype=road" +
                        "&key="+googleAPIKey +
                        "&style=feature:poi|visibility:off" +
                        "&style=feature:administrative|visibility:off" +
                        "&style=feature:road|visibility:simplified" +
                        "&style=feature:transit|visibility:off";

                int _mapButton_width = mMapButton.getWidth();
                int _mapButton_height = mMapButton.getHeight();

                Picasso
                        .with(getActivity())
                        .load(mapButtonStaticImageURL)
                        .resize(_mapButton_width, _mapButton_height)
                        .centerCrop()
                        .into(new Target(){
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                mMapButton.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
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

        });

    }





    //
    public void setupMapButtonGeocoding() throws IOException {

        String _url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key="+googleAPIKey;

        final String[] tempStrings = {"", ""};
        final TextView mMapButtonTitle = this.rootView.findViewById(R.id.main_this_area_mapbutton_title);
        final TextView mMapButtonSubtitle = this.rootView.findViewById(R.id.main_this_area_mapbutton_subtitle);

        doOkHTTPRequestCall(_url, new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    // Got geocoding data, will process into Object
                    String _data= response.body().string();
                    try {

                        JSONObject reader = new JSONObject(_data);


                        tempStrings[0] = (reader.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
                        tempStrings[1] = (
                                String.valueOf(reader.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"))
                                        + ", "+
                                String.valueOf(reader.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"))
                        );


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    // Request not successful
                }

            }

        });

        mMapButtonTitle.setText(tempStrings[0]);
        mMapButtonSubtitle.setText(tempStrings[1]);


    }







    Call doOkHTTPRequestCall(String url, Callback callback) {
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        Call call = OkHTTP_Client.newCall(request);
        call.enqueue(callback);
        return call;
    }








    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }








    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }

        if(context instanceof OnReceiveDataFromFragmentListener) {
            mActivityListener = (OnReceiveDataFromFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnReceiveDataFromFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mActivityListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // I created this method to allow sending data from this fragment, back to the activity
    // See: https://gist.github.com/blackcj/6244204
    public interface OnReceiveDataFromFragmentListener {
        void onReceiveDataFromFragment(String action, Object data);
    }

}
