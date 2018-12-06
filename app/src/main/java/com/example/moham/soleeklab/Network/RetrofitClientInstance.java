package com.example.moham.soleeklab.Network;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    public static final String BASE_URL = "http://52.174.22.188/soleekappapi/public/";
    private static final String TAG = RetrofitClientInstance.class.getSimpleName();
    private static Retrofit retrofit;


    public static Retrofit getRetrofitInstance() {
        Log.d(TAG, "getRetrofitInstance() has been instantiated");

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
