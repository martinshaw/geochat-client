/*
 * MainExploreFragment
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/MainExploreFragment.java
 *
 * Project: GeoChat
 * Module: app
 * Last Modified: 23/05/18 06:23 <martin>
 * Last Compilation: 23/05/18 06:23
 *
 * Copyright (c) 2018. Martin David Shaw. All rights reserved.
 */

package co.martinshaw.apps.android.geochat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainExploreFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;

    View rootView;






    public MainExploreFragment() {
        // Required empty public constructor
    }


    public static android.support.v4.app.Fragment newInstance() {
        android.support.v4.app.Fragment fragment = new MainExploreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_explore, container, false);


        return rootView;
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
