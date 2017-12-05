package co.martinshaw.apps.android.geochat;

import android.content.Context;
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

        Button vBottomsheetTriggerButton = (Button) v.findViewById(R.id.regist_bottomsheet_triggerbutton);

        vBottomsheetTriggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ((RegistrationActivity) getActivity()).activateBottomSheet();
            }
        });

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
