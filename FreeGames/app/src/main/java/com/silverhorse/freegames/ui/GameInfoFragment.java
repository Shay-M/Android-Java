package com.silverhorse.freegames.ui;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.silverhorse.freegames.R;
import com.silverhorse.freegames.model.Game;


public class GameInfoFragment extends Fragment {

    private Game gameGet;

    private TextView mGameTitle;
    private TextView GameInfo;
    private ImageView mGameImageView;

    public GameInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameGet = GameInfoFragmentArgs.fromBundle(getArguments()).getGameToInfo();

        mGameTitle = view.findViewById(R.id.info_title_game_view);
        GameInfo = view.findViewById(R.id.info_text);
        mGameImageView = view.findViewById(R.id.info_image_game_view);


        mGameTitle.setText(gameGet.getName());
        GameInfo.setText(gameGet.getInfo());
        GameInfo.setMovementMethod(new ScrollingMovementMethod());

        //set img
        Glide.with(this)
                .load(gameGet.getImgUrl())
                .centerCrop()
                .error(android.R.drawable.ic_dialog_info)
                .into(mGameImageView);


    }
}