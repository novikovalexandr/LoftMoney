package com.alexandr4.loftmoney;

import android.content.ClipData;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("items")
    Call<List<ItemActivity>> getItems(@Query("type") String type);
}
