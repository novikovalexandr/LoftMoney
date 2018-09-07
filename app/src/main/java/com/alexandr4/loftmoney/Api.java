package com.alexandr4.loftmoney;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type);
}
