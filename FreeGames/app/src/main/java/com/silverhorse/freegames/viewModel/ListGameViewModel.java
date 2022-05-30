package com.silverhorse.freegames.viewModel;
/* Created by Shay Mualem on 09/12/2021 */

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.silverhorse.freegames.model.Game;
import com.silverhorse.freegames.network.Api;
import com.silverhorse.freegames.network.ApiUtil;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListGameViewModel extends ViewModel {


    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Game>> listOfGames;


    public LiveData<List<Game>> getGamesList() {

        //if the list is null
        if (listOfGames == null) {
            listOfGames = new MutableLiveData<>();

            //we will load it asynchronously from server in this method
            loadGames();
        }
        //finally we will return the list
        return listOfGames;
    }

    //This method is using Retrofit to get the JSON data from URL
    private void loadGames() {

        Api api = ApiUtil.getRetrofitApi();
/////
        Call<List<Game>> call = api.getGames("marvel");
//        Call<List<Game>> call = api.getGames("api/v2/recipes/","","36498");

        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {

                //finally we are setting the list to our MutableLiveData
//                response.body().sort(Comparator.comparing(Game::getName)); // game1, game2) -> game1.getName().compareTo(game2.getName())
                listOfGames.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {

                Log.d("Error", "" + t.getMessage());
            }
        });

    }


    public void sortByName() {

        List<Game> sortedList = listOfGames.getValue();
        sortedList.sort(Comparator.comparing(Game::getName).reversed());
        listOfGames.postValue(sortedList);
    }
}
