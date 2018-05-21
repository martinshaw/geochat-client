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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class MainThisAreaFragment extends android.support.v4.app.Fragment {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;







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
        View rootView = inflater.inflate(R.layout.fragment_main_this_area, container, false);




        //
        setupMapButtonBackground(rootView);






        return rootView;
    }






    private void setupMapButtonBackground(View rootView) {

        // Set background of MapButton as Google Map Static image of current location
        final LinearLayout mapbutton = rootView.findViewById(R.id.main_this_area_mapbutton);

        //      View.post runs after the view element has been laid out according to layout_width and layout_height.
        //      _mapButton_width & _mapButton_height should be actual pixels for use with Picasso image cropping
        mapbutton.post(new Runnable() {

            @Override
            public void run() {
                // Get GOOOLE MAP API KEY for later use in URL
                String googleAPIKey = "";
                try {
                    ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
                    Bundle bundle = ai.metaData;
                    googleAPIKey = bundle.getString("com.google.android.geo.API_KEY");
                    Log.e("GETTINGAPIKEY", "Found Google Maps API Key: "+googleAPIKey);
                    googleAPIKey = "&key="+googleAPIKey;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("GETTINGAPIKEY", "Failed to load meta-data, NameNotFound: " + e.getMessage());
                } catch (NullPointerException e) {
                    Log.e("GETTINGAPIKEY", "Failed to load meta-data, NullPointer: " + e.getMessage());
                }

                // Construct Google Map Static image request URL
                String mapButtonStaticImageURL ="https://maps.googleapis.com/maps/api/staticmap" +
                        "?center=Manchester" +
                        "&format=png" +
                        "&zoom=16" +
                        "&size=600x300" +
                        "&maptype=road" +
                        googleAPIKey +
                        "&style=feature:poi|visibility:off" +
                        "&style=feature:administrative|visibility:off" +
                        "&style=feature:road|visibility:simplified" +
                        "&style=feature:transit|visibility:off";

                int _mapButton_width = mapbutton.getWidth();
                int _mapButton_height = mapbutton.getHeight();

                Picasso
                        .with(getActivity())
                        .load(mapButtonStaticImageURL)
                        .resize(_mapButton_width, _mapButton_height)
                        .centerCrop()
                        .into(new Target(){
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                mapbutton.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
