package com.sbo.retrofit;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebService {

    static WebService create() {
        //"http://app-server.hopto.org:8081"
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://168.235.81.127:8081")
                .build();

        return retrofit.create(WebService.class);
    }

    @POST("stripes")
    Call<GraphicResult> getFullGraphic(
            @Body PostModel model
    );

}
