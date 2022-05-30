package com.silverhorse.retrofit;
// https://stackoverflow.com/questions/64095431/calling-a-list-inside-json-two-objects-using-retrofit-in-android-studio
// https://stackoverflow.com/questions/52147481/how-to-parse-array-inside-object-with-retrofit-android

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.silverhorse.retrofit.model.animl;
import com.silverhorse.retrofit.net.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private TextView textResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResponse = findViewById(R.id.text);


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://my-json-server.typicode.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//
//        Api api = retrofit.create(Api.class);
//
//        Call<Post> call = api.getPost();
//        call.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                if (!response.isSuccessful()) {
//                    Log.d("TAG", "onResponse: " + response.code());
//                    textResponse.setText(response.code());
//
//                    return;
//                }
//                List<Post.Animals> posts = response.body().getMatch();
//
//                for (Post.Animals post : posts) {
//                    String content = "";
//
//                    content += "url: " + post.getUrl() + "\n";
//
//                    textResponse.append(content + " ");
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                textResponse.setText(t.getMessage());
//
//            }
//
//        });


        //////////////


/*        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit2 = builder.build();

        String testUrl = "Shay-M/RootShayMu/db";
//
        Api api2 = retrofit2.create(Api.class);

        Call<animl> call = api2.getPost();

        call.enqueue(new Callback<animl>() {

            @Override
            public void onResponse(Call<animl> call, Response<animl> response) {

                if (!response.isSuccessful()) {
                    Log.d("TAG code", "onResponse: " + response.code());
                    textResponse.setText(response.code());

                    return;
                }

                Log.d("TAG", "onResponse: " + response.body().getName());
            }

            @Override
            public void onFailure(Call<animl> call, Throwable t) {
                Log.d("TAG", "onFailure: "+ t.getMessage());
            }
        });*/

        ///


        /* {
            "name": "dog",
                "id": 1,
                "age": 5,
                "imag": "",
                "kind": "ling"
        }*/

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mocki.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Api api = retrofit.create(Api.class);

        Call<animl> call = api.getPost();
        call.enqueue(new Callback<animl>() {
            @Override
            public void onResponse(Call<animl> call, Response<animl> response) {
                if (!response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.code());
                    textResponse.setText(response.code());

                    return;
                }
                Log.d("TAG", "onResponse: " + response.body().getKind());

            }

            @Override
            public void onFailure(Call<animl> call, Throwable t) {
                textResponse.setText(t.getMessage());

            }

        });*/

        ////


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://mocki.io/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit2 = builder.build();

        String testUrl = "https://mocki.io/v1/08089f5d-7eaa-4343-8681-558dc418ae84";
//        String testUrl = "https://my-json-server.typicode.com/Shay-M/Android-Java/db";
//
        Api api2 = retrofit2.create(Api.class);

        Call<List<animl>> call = api2.getListAnimals(testUrl);

        call.enqueue(new Callback<List<animl>>() {


            @Override
            public void onResponse(Call<List<animl>> call, Response<List<animl>> response) {
                if (!response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.code());
                    textResponse.setText(response.code());

                    return;
                }

                Log.d("TAG", "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<List<animl>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}