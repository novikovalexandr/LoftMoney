package com.alexandr4.loftmoney;

import com.google.android.gms.common.api.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("auth")
    Call<AuthResult> auth(@Query("social_user_id") String userId);

    @GET("logout")
    Call<Result> logout();

    @GET("balance")
    Call<BalanceResult> balance();

    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type);

    @POST("items/add")
    Call<PostResults> addItem(@Query("price") String price, @Query("name") String name, @Query("type") String type);

    @POST("items/remove")
    Call<PostResults> removeItem(@Query("id") int id);
}
