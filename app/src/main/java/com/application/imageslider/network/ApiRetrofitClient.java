package com.application.imageslider.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
@Deprecated
public class ApiRetrofitClient {

    private final static String BASE_URL = "http://dev-tasks.alef.im/task-m-001/list.php/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
