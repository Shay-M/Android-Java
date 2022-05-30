package com.example.mediaplayer.SongsRecyclerView;/* Created by Shay Mualem 23/07/2021 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayer.R;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongsViewHolder> {
    private ArrayList<SongItem> songItemList;

    //callback fun
    private RecyclerViewListener listener;
    private Context context;

    public SongAdapter(ArrayList<SongItem> listOfSongsItems, Context context) {
        this.songItemList = listOfSongsItems;
        this.context = context;
    }


    public void setListener(RecyclerViewListener listener) {

        this.listener = listener;
    }

    /**
     * @param parent   recyclerview
     * @param viewType cell type
     * @return Inflated the view xml
     */
    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_of_song, parent, false);

        return new SongsViewHolder(view);
    }

    /**
     * call to load a cell ,this fun get parameter for a cell
     *
     * @param holder   type
     * @param position position of cell
     */
    @Override
    public void onBindViewHolder(SongAdapter.SongsViewHolder holder, int position) {
        SongItem SongItem = songItemList.get(position);

        //set song titale
        holder.songTitleTv.setText(SongItem.getName());

        //set song Duration
        holder.songDetailsTv.setText(SongItem.getSongDuration());

        //update img song
        ImageView imageView = holder.songImageIv;

        if (SongItem.getUri() != null)
            Glide.with(context).load(SongItem.getUri()).thumbnail(0.10f).centerCrop().into(imageView);
        else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.with(context).clear(holder.songImageIv);
            // remove the placeholder (optional); read comments below
            holder.songImageIv.setImageDrawable(null);
            Log.d("Glide", "onBindViewHolder: is null");
        }

    }

    /**
     * @return how many item have
     */
    @Override
    public int getItemCount() {
        return songItemList.size();
    }

    /**
     * when we have only one type of cell, this fun first call.
     *
     * @param position the next cell to show
     * @return type of cell
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    /**
     * interface to manager all RecyclerView events
     */
    public interface RecyclerViewListener {

        void onItemClick(int position, View view);

        //void onLongClick(int position, View view);

        void onImgClick(int position, View view);
    }

    //crete view holder, for hold reference in a cell
    public class SongsViewHolder extends RecyclerView.ViewHolder {

        TextView songTitleTv;
        TextView songDetailsTv;
        ImageView songImageIv;


        public SongsViewHolder(View itemView) {
            super(itemView);
            songTitleTv = itemView.findViewById(R.id.song_title);
            songDetailsTv = itemView.findViewById(R.id.song_details);
            songImageIv = itemView.findViewById(R.id.song_image);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(getAdapterPosition(), v);
            });

//            itemView.setOnLongClickListener(v -> {
//                if (listener != null)
//                    listener.onLongClick(getAdapterPosition(), v);
//                return false;
//            });

            songImageIv.setOnClickListener(v -> {
                if (listener != null)
                    listener.onImgClick(getAdapterPosition(), v);
            });


        }
    }

}

