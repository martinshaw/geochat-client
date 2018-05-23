/*
 * RegistWelcomeinfoFragment1
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/RegistWelcomeinfoFragment1.java
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
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RegistWelcomeinfoFragment1 extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;

    public RegistWelcomeinfoFragment1() { }

    // TODO: Rename and change types and number of parameters
    public static RegistWelcomeinfoFragment1 newInstance() {
        RegistWelcomeinfoFragment1 fragment = new RegistWelcomeinfoFragment1();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_regist_welcomeinfo_1, container, false);

        // Create reference to Button which triggers the activation of the Bottomsheet for user registration
        Button vBottomsheetTriggerButton = (Button) v.findViewById(R.id.regist_bottomsheet_triggerbutton);

        // Setup onClick event. Calls function in main activity which graphically expands Bottomsheet component
        vBottomsheetTriggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((RegistrationActivity) getActivity()).activateBottomSheet();
            }
        });

        // Configures and runs background gradient animation applied to button
        AnimationDrawable animationDrawable = (AnimationDrawable) vBottomsheetTriggerButton.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        // Inflate the layout for this fragment
        return v;
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
