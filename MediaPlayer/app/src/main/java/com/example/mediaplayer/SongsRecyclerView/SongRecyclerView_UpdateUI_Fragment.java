package com.example.mediaplayer.SongsRecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayer.ActionsMediaPlayer.Actions;
import com.example.mediaplayer.ManagerSongs.ManagerListSongs;
import com.example.mediaplayer.R;

import java.util.Collections;


public class SongRecyclerView_UpdateUI_Fragment extends Fragment {

    private static SongAdapter.RecyclerViewListener recyclerViewListener = null;
    private boolean isPlaying;
    //    private Animation tapeSpinsAni;
    private ValueAnimator animTapeSpin1;
    private ValueAnimator animTapeSpin2;

    private final BroadcastReceiver closePlayingAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //tapeSpin1.clearAnimation();
            AnimTape(false);

        }
    };
    private final BroadcastReceiver playPlayingAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //tapeSpin2.startAnimation(tapeSpinsAni);
            // tapeSpin1.startAnimation(tapeSpinsAni);
            AnimTape(true);
        }
    };
    // pause and close from notification
    private final BroadcastReceiver pausePlayingAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AnimTape(false);
        }
    };
    private ManagerListSongs managerListSongs;
    //////
    private int fromPosition;
    private int toPosition;
    private SongAdapter songAdapter;


    public SongRecyclerView_UpdateUI_Fragment() {
        // Required empty public constructor

    }

    public SongRecyclerView_UpdateUI_Fragment(SongAdapter.RecyclerViewListener recyclerViewListener, boolean isPlaying) {
        // Required empty public constructor
        SongRecyclerView_UpdateUI_Fragment.recyclerViewListener = recyclerViewListener;
        this.isPlaying = isPlaying;

    }


    //register to actions
    private void register_pausePlayingAudio() {
        IntentFilter intentFilter = new IntentFilter(Actions.PAUSE_SONG);
        requireActivity().registerReceiver(pausePlayingAudio, intentFilter);
    }

    private void register_closePlayingAudio() {
        IntentFilter intentFilter = new IntentFilter(Actions.PLAY_SONG);
        requireActivity().registerReceiver(playPlayingAudio, intentFilter);
    }

    private void register_playPlayingAudio() {
        IntentFilter intentFilter = new IntentFilter(Actions.CLOSE_SONG);
        requireActivity().registerReceiver(closePlayingAudio, intentFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        tapeSpinsAni = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_loop);
        register_pausePlayingAudio();
        register_playPlayingAudio();
        register_closePlayingAudio();

    }

    public void AnimTape(boolean toAni) {
        if (toAni) {
            if (animTapeSpin1.isStarted()) {
                animTapeSpin2.resume();
                animTapeSpin1.resume();
            } else {
                animTapeSpin1.start();
                animTapeSpin2.start();
            }

        } else {
            animTapeSpin1.pause();
            animTapeSpin2.pause();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment לנפח
        View rootView = inflater.inflate(R.layout.fragment_song_recycler_view, container, false);

//        final private Animation tapeSpinsAni = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_loop);
        ImageView tapeSpin1 = rootView.findViewById(R.id.spain1);
        ImageView tapeSpin2 = rootView.findViewById(R.id.spain2);

        animTapeSpin1 = ObjectAnimator.ofFloat(tapeSpin1, "rotation", 0, 360);
        animTapeSpin1.setDuration(3300);
        animTapeSpin1.setRepeatCount(ValueAnimator.INFINITE);
        animTapeSpin1.setInterpolator(new LinearInterpolator());

//        anim.setRepeatMode(ObjectAnimator.RESTART);

        animTapeSpin2 = ObjectAnimator.ofFloat(tapeSpin2, "rotation", 0, 360);
        animTapeSpin2.setDuration(3200);
        animTapeSpin2.setRepeatCount(ValueAnimator.INFINITE);
        animTapeSpin2.setInterpolator(new LinearInterpolator());

        if (isPlaying)
            AnimTape(true);


        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_of_songs);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        managerListSongs = ManagerListSongs.getInstance();

        songAdapter = new SongAdapter(managerListSongs.getListOfSongsItems(), getActivity());


        songAdapter.setListener(recyclerViewListener);


        recyclerView.setAdapter(songAdapter);

        managerListSongs.setListener(new ManagerListSongs.RecyclerViewUpdateUIListener() {
            @Override
            public void onUpdateListItem() {
//                songAdapter.notifyDataSetChanged();
                songAdapter.notifyItemInserted(songAdapter.getItemCount() + 1);
            }
        });

        // https://www.youtube.com/watch?v=uvzP8KTz4Fg
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.START | ItemTouchHelper.END) {

            // Load the animation
            final Animation scaleDownAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animate_scale_down);
            final Animation scaleUpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.animate_scale_up);

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();


                // update lists the new Position
                Collections.swap(managerListSongs.getListOfSongsItems(), fromPosition, toPosition);
                Collections.swap(managerListSongs.getListOfUrlSongs(), fromPosition, toPosition);

                // update ui
                songAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                Spanned Message = Html.fromHtml("You are going to delete the song:\n" + "<br>" + "<b>" + managerListSongs.getListOfSongsItems().get(viewHolder.getAdapterPosition()).getName() + "<br>" + "<br>" + "</b>" + "This action cannot be undone!", HtmlCompat.FROM_HTML_MODE_LEGACY);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Please confirm").setMessage(Message).setIcon(android.R.drawable.ic_menu_delete)
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            songAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        })
                        .setPositiveButton("Delete", (dialog, which) -> {
                            managerListSongs.RemovingSongFromList(viewHolder.getAdapterPosition()); //todo
//                            managerListSongs.getListOfSongsItems().remove(viewHolder.getAdapterPosition());
//                            managerListSongs.getListOfUrlSongs().remove(viewHolder.getAdapterPosition());
                            songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            // managerListSongs.SaveSongList();
                        }).show();


            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                Log.d("onSelectedChanged", "");

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    scaleUpAnimation.setFillAfter(true);
                    viewHolder.itemView.startAnimation(scaleUpAnimation);

                }

            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);


                if (viewHolder.itemView.getAnimation() != null) {
                    viewHolder.itemView.startAnimation(scaleDownAnimation);
                    Log.d("clearView", "clearView: ");
                    managerListSongs.MoveSongList(fromPosition, toPosition);
                }

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        return rootView;
    }


}