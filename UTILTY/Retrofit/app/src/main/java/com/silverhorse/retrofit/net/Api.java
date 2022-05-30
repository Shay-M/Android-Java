package com.silverhorse.retrofit.net;
/* Created by Shay Mualem on 16/12/2021 */

import com.silverhorse.retrofit.model.animl;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {

//    @GET("Shay-M/Android-Java/db")
//    Call<Post> getPost();

    @GET("v1/d6b97695-4694-40cb-b89b-26c2b1f09341")
    Call<animl> getPost();

//    @GET("{url}")
//    Call<Post> getPostUrl(@Path("url") String postUrl);

    @GET
    public Call<List<animl>> getListAnimals(@Url String url);

}
//https://futurestud.io/tutorials/retrofit-2-how-to-use-dynamic-urls-for-requests