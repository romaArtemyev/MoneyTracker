package com.loftschool.moneytracker.api;

import com.loftschool.moneytracker.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
        @GET("items")
        Call<List<Item>> items(@Query("type")String type);

        @POST("items/add")
        Call<AddResult> add(@Query("price") int price, @Query("name") String name, @Query("type") String type);

        @POST("items/remove")
        Call<Result> remove(@Query("id") int id);

        @GET("auth")
        Call<AuthResult> auth(@Query("social_user_id")String socialUserId);

        @GET("balance")
        Call<BalanceResult> balance();

}
