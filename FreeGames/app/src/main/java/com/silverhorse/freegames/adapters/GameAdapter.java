package com.silverhorse.freegames.adapters;
/* Created by Shay Mualem on 09/12/2021 */

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.silverhorse.freegames.R;
import com.silverhorse.freegames.model.Game;

import java.util.ArrayList;
import java.util.List;

//--------------------------------------------------
//public class GameAdapter extends RecyclerView.Adapter   <> {
// and create the view holder >>> public class GameViewHolder extends RecyclerView.ViewHolder
//--------------------------------------------------

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> implements Filterable {

    private List<Game> mGameList;
    private List<Game> allGameList;

    private OnGameListener mOnGameListener;
    //    create Filter ~ android.widget
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Game> filterResults = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterResults.addAll(allGameList); // show all items
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Game item : allGameList) {
                    if (item.getName().toLowerCase().trim().contains(filterPattern))
                        filterResults.add(item);
                }


            }
            // need to return FilterResults
            FilterResults results = new FilterResults();
            results.values = filterResults;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mGameList.clear();
            mGameList.addAll((ArrayList) filterResults.values);

            notifyDataSetChanged();
        }
    };


    public GameAdapter(OnGameListener onGameListener) {
        this.mOnGameListener = onGameListener;
        mGameList = new ArrayList<>();
    }

    public void setGameList(List<Game> gameList) {
        this.mGameList = gameList;
//        this.mGameList.addAll(gameList);
        // create a Copy
        this.allGameList = new ArrayList<>(mGameList);

        this.notifyDataSetChanged();
    }

    public Game getSelectedGame(int position) {
        if (mGameList != null) {
            if (mGameList.size() > 0) {
                return mGameList.get(position);
            }
        }
        return null;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card, parent, false);
        return new GameViewHolder(view, mOnGameListener); //@return Inflated the view xml
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {

        Game gameItem = mGameList.get(position);

        // set game title
        holder.mGameTitle.setText(gameItem.getName());

        Log.d("gameItem.getImgUrl()", "onBindViewHolder: " + gameItem.getImgUrl());

        //set img
        Glide.with(holder.mGameImageView.getContext())
                .load(gameItem.getImgUrl())
                .centerCrop()
//                .placeholder(MyShimmer.getShimmer())
                .error(android.R.drawable.ic_dialog_info)
                .into(holder.mGameImageView);


    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}