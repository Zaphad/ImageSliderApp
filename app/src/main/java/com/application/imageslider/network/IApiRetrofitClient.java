package com.application.imageslider.network;

import retrofit2.Call;
import retrofit2.http.GET;
@Deprecated
public interface IApiRetrofitClient {

@GET()
Call<String> getPhpList();

}
