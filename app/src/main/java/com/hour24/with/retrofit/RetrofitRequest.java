package com.hour24.with.retrofit;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitRequest {

    //JSON
    public static <T> T createRetrofitJSONService(Context context, final Class<T> classes, final String url) {

        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .client(okHttpClient.build())
                .build();

        T service = retrofit.create(classes);

        return service;
    }

    // ScalarsConverterFactory for String
    public static <T> T createRetrofitScalarsService(Context context, final Class<T> classes, final String url) {

        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(url)
                .client(okHttpClient.build())
                .build();

        T service = retrofit.create(classes);

        return service;
    }

}
