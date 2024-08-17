package com.example.foodplanner.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    static Retrofit retrofit=null;
    final static String BASE_URL="https://themealdb.com/";
    public static Retrofit getInstance(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

                    .build();
        }
        return retrofit;
    }
    public static WebService getApi(){
        return getInstance().create(WebService.class);
    }


}
