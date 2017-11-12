package com.loftschool.moneytracker;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loftschool.moneytracker.api.Api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        final OkHttpClient client = new OkHttpClient.Builder()
                                    .addInterceptor(new HttpLoggingInterceptor()
                                    .setLevel(BuildConfig.DEBUG ?
                                        HttpLoggingInterceptor.Level.BODY
                                        : HttpLoggingInterceptor.Level.NONE))
                                    .build();
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://moneytracker121117.getsandbox.com/")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(client)
                            .build();
        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }
}
