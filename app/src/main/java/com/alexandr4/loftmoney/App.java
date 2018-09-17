package com.alexandr4.loftmoney;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private Api api;

    private static final String PREFERENCES_SESSION = "session";
    private static final String KEY_AUTH_TOKEN = "auth-token";

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor.Level level;
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY;
        } else {
            level = HttpLoggingInterceptor.Level.NONE;
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(level);
/*
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
*/

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.NONE))
                .addInterceptor(new AuthInterceptor())
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

    public void saveAuthToken(String token) {
        getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE).edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    public String getAuthToken() {
        return getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE).getString(KEY_AUTH_TOKEN, "");
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(getAuthToken());
    }

    private class AuthInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();
            String authToken = getAuthToken();
            HttpUrl url = originalRequest.url().newBuilder().addQueryParameter("auth-token", authToken).build();
            return chain.proceed(originalRequest.newBuilder().url(url).build());
        }
    }

}
