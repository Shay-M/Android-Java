package com.silverhorse.freegames.adapters;
/* Created by Shay Mualem on 09/12/2021 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silverhorse.freegames.R;

public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mGameTitle;
    ImageView mGameImageView;

    OnGameListener mOnGameListener;

    public GameViewHolder(@NonNull View itemView, OnGameListener onGameListener) {
        super(itemView);

        mOnGameListener = onGameListener;

        mGameTitle = itemView.findViewById(R.id.title_game_view);
        mGameImageView = itemView.findViewById(R.id.image_game_view);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mOnGameListener.onGameClick(getAdapterPosition(), view);
    }
}
