package com.example.gross.gallery.network;


import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.gross.gallery.Consts.BASE_URL;

public class RestManager {

    private static ApiService apiService;

    @NonNull
    public static ApiService getApiService(){
        //I know that double checked locking is not a good pattern, but it's enough here
        ApiService service = apiService;
        if (service == null){
            synchronized (RestManager.class){
                service = apiService;
                if (service == null){
                    service = apiService = createService();
                }
            }
        }
        return service;
    }

    @NonNull
    private static ApiService createService() {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }

}
