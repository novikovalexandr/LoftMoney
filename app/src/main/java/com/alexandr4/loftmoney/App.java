package com.alexandr4.loftmoney;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String TAG = "App";
    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        HttpLoggingInterceptor.Level level;
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY;
        } else {
            level = HttpLoggingInterceptor.Level.NONE;
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(level);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://android.loftschool.com/basic/v1/")
                //.baseUrl("http://loftschoolandroid.getsandbox.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }
}
