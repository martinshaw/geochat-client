/*
 * MainSentMessagesFragment
 * C:/Users/martin/Android_Projects/GeoChat/app/src/main/java/co/martinshaw/apps/android/geochat/MainSentMessagesFragment.java
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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainSentMessagesFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;

    View rootView;

    Retrofit retrofit;
    GeochatAPIService service;
    MediaType OkHTTP_DataType_JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient OkHTTP_Client = new OkHttpClient();

    public SharedPreferences prefs;






    public MainSentMessagesFragment() {
        // Required empty public constructor
    }


    public static android.support.v4.app.Fragment newInstance() {
        android.support.v4.app.Fragment fragment = new MainSentMessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_sent_messages, container, false);

        prefs = this.getActivity().getSharedPreferences("co.martinshaw.apps.android.geochat", Context.MODE_PRIVATE);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(prefs.getString("apiUrl", "http://192.169.159.139:8001"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GeochatAPIService.class);




        // Setup MessageList
        setupMessageListView();

        return rootView;
    }








    public void setupMessageListView(){

        retrofit2.Call<GeochatAPIResponse<List<Message>>> messagesReq = service.getMessagesByUserSessionKey(prefs.getString("sessionKey", ""), prefs.getString("sessionKey", ""));
        messagesReq.enqueue(new retrofit2.Callback<GeochatAPIResponse<List<Message>>>() {
            @Override
            public void onResponse(retrofit2.Call<GeochatAPIResponse<List<Message>>> call, retrofit2.Response<GeochatAPIResponse<List<Message>>> response) {
                if (response.code() == 200) {

                    if (response.body().getErrorMsg() != null) {
                        Toast.makeText(getActivity(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        processMessagesIntoListView(response.body().getData());

                    }

                } else {

                    Toast.makeText(getActivity(), R.string.api_message_404, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<GeochatAPIResponse<List<Message>>> call, Throwable t) {
                Log.i("Failure", t.toString());
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }





    public void processMessagesIntoListView(List<Message> _messages){

        ListView mMessageList = rootView.findViewById(R.id.main_sent_messages_messagelist);

        mMessageList.setAdapter(new SentMessagesMessageListAdaptor(getContext(), _messages));

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
