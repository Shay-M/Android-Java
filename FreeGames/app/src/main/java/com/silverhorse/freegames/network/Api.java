package com.silverhorse.freegames.network;

import com.silverhorse.freegames.model.Game;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    /*
    Call An invocation of a Retrofit method that sends a request to a webserver and returns a response.
    Each call yields its own HTTP request and response pair.
    Calls may be executed synchronously with execute, or asynchronously with enqueue.
    In either case the call can be canceled at any time with cancel. */

    @GET("marvel")
    Call<List<Game>> getGames();

    @GET("{which}")
    Call<List<Game>> getGames(
            @Path("which") String path
//            @Query("key") String key,
//            @Query("rId") String recipe_id

    );

    //@Path("user") annotation on the userId parameter marks it as a
    //replacement for the {user} placeholder in the @GET path

    //@GET("/users/{user}")
    //Call<Item> getUser(@Path("user") String userId);

    /*
    Retrofit get parameters
    @GET("/maps/api/geocode/json?")
    Call<JsonObject> getLocationInfo(@Query("address") String zipCode,
                                             @Query("sensor") boolean sensor,
                                             @Query("client") String client,
                                             @Query("signature") String signature);
    Retrofit generates:
    &address=90210&sensor=false&client=gme-client&signature=signkey
     */

}
