package com.silverhorse.freegames.network;

 /*
        Create an implementation of the API endpoints defined by the service interface.
        The relative path for a given method is obtained from an annotation on the method describing
        the request type. The built-in methods are GET, PUT, POST, PATCH, HEAD, DELETE and OPTIONS.
        You can use a custom HTTP method with @HTTP. For a dynamic URL, omit the path on the annotation
         and annotate the first parameter with @Url.

         Method parameters can be used to replace parts of the URL by annotating them with @Path.
         Replacement sections are denoted by an identifier surrounded by curly braces (e.g., "{foo}").
         To add items to the query string of a URL use @Query.
         */

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtil {

    static final String BASE_URL = "https://simplifiedcoding.net/demos/";

    public static Api getRetrofitApi() { // API is the interface


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Api api = retrofit.create(Api.class);

        return api;

    }

}
