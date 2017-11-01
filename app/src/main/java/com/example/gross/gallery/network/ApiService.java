package com.example.gross.gallery.network;


import com.example.gross.gallery.model.PixabayResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiService {

    @GET("api")
    Observable<PixabayResponse> getPixabay(@Query("key") String apiKey,
                                           @Query("q") String query,
                                           @Query("per_page") int perPage);

    @GET("{imageUrl}")
    @Streaming
    Call<ResponseBody> downloadFile(@Path("imageUrl") String imageUrl);

}
