package com.example.dogs;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static ApiService apiService;
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/";

    public static ApiService getApiService(){
        if (apiService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)                                    // создаём базовый адресс
                    .addConverterFactory(GsonConverterFactory.create())   // создаём адаптер json
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // применяем адаптер
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

}
