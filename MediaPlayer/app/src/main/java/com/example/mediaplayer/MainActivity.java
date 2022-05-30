package com.example.mediaplayer;/* Created by Shay Mualem 22/07/2021 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.astritveliu.boom.Boom;
import com.example.mediaplayer.ActionsMediaPlayer.Actions;
import com.example.mediaplayer.ActionsMediaPlayer.ActionsPlayer;
import com.example.mediaplayer.Dialogs.AddSongDialog;
import com.example.mediaplayer.ManagerSongs.ManagerListSongs;
import com.example.mediaplayer.ManagerSongs.ManagerSaveSongs;
import com.example.mediaplayer.SongsRecyclerView.SongAdapter;
import com.example.mediaplayer.SongsRecyclerView.SongRecyclerView_UpdateUI_Fragment;
import com.example.mediaplayer.SongsRecyclerView.SoundBigFragment;
import com.example.mediaplayer.utils.CameraManagerUrl;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity implements ActionsPlayer, AddSongDialog.AddSongDialogListener {
    final String REGISTER_FRAGMENT_TAG = "register_fragment";
    boolean isPlaying = false;
    private Intent intent;
    private ManagerListSongs managerListSongs;
    private ImageView playBtn;
    // pause and close from notification
    private final BroadcastReceiver pausePlayingAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("pausePlayingAudio", "Intent: " + intent);
            playBtn.setImageResource(R.drawable.playxhdpi);

            isPlaying = false;


        }
    };
    private final BroadcastReceiver closePlayingAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("closePlayingAudio", "Intent: " + intent);
            playBtn.setImageResource(R.drawable.playxhdpi);
            isPlaying = false;

        }
    };
    private ProgressBar LoadSongProgressBar;

    private final BroadcastReceiver playPlayingAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("playPlayingAudio", "Intent: " + intent);
            playBtn.setImageResource(R.drawable.pausexhdpi);
//            playBtn.setAlpha(1f);
            isPlaying = true;
            LoadSongProgressBar.setVisibility(View.GONE);

        }
    };
    private ImageView backBigPic;
    private SongAdapter.RecyclerViewListener recyclerViewListener;

    //register to actions
    private void register_pausePlayingAudio() {
        IntentFilter intentFilter = new IntentFilter(Actions.PAUSE_SONG);
        registerReceiver(pausePlayingAudio, intentFilter);

    }

    private void register_closePlayingAudio() {
        IntentFilter intentFilter = new IntentFilter(Actions.PLAY_SONG);
        registerReceiver(playPlayingAudio, intentFilter);
    }

    private void register_playPlayingAudio() {
        IntentFilter intentFilter = new IntentFilter(Actions.CLOSE_SONG);
        registerReceiver(closePlayingAudio, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        managerListSongs.SaveList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        // Show Status Bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
//        decorView.setSystemUiVisibility(uiOptions);


        setTheme(R.style.MysplashScreen);

        // Removing top action bar: https://www.geeksforgeeks.org/different-ways-to-hide-action-bar-in-android-with-examples/
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playBtn = findViewById(R.id.btn_play_main);
        backBigPic = findViewById(R.id.back_big_pic);
        final ImageView nextBtn = findViewById(R.id.btn_next_main);
        LoadSongProgressBar = findViewById(R.id.progressBarLoadSong);
        final ImageView backBtn = findViewById(R.id.btn_prev_main);
        final ImageView addBtn = findViewById(R.id.addLinkBtn);

        ManagerSaveSongs.static_context = this; // init save manager
        managerListSongs = ManagerListSongs.getInstance();

        CameraManagerUrl.init(this);


        /////////////////
        recyclerViewListener = new SongAdapter.RecyclerViewListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.d("onItemClick", "position: " + position);
                //pauseClick();
                LoadSongProgressBar.setVisibility(View.VISIBLE);
                managerListSongs.setCurrentPlaying(position - 1); // update list of songs items
                nextClick();
            }

//            @Override
//            public void onLongClick(int position, View view) {
//                Log.d("onLongClick", "position: " + position);
//            }

            // image song Button
            @Override
            public void onImgClick(int position, View view) {
                Log.d("onImgClick", "position: " + position);
                String songName = managerListSongs.getListOfSongsItems().get(position).getName();

                SoundBigFragment soundBigFragment = SoundBigFragment.newInstance(songName, Uri.parse(managerListSongs.getListOfSongsItems().get(position).getUri()));
                backBigPic.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragment_list_and_big, soundBigFragment, REGISTER_FRAGMENT_TAG);

                fragmentTransaction.commit();

            }
        };

        ////
        backBigPic.setOnClickListener(v -> showListOfSongFragment());




        //play Button
        new Boom(playBtn);
        playBtn.setOnClickListener(view -> {
            if (!isPlaying) {
                LoadSongProgressBar.setVisibility(View.VISIBLE);

                if (intent == null) { //first time
                    intent = new Intent(MainActivity.this, MusicPlayerService.class);
                    intent.putExtra("command", "new_instance");
                    startService(intent);

                } else playClick();
//                playBtn.setAlpha(0.4f);
                //isPlaying = true;
                //playBtn.setImageResource(R.drawable.pausexhdpi);
            } else pauseClick();

        });
        //next Button
        new Boom(nextBtn);
        nextBtn.setOnClickListener(view -> nextClick());

        //back Button
        new Boom(backBtn);
        backBtn.setOnClickListener(view -> prevClick());

        // add a song Button
        new Boom(addBtn);
        addBtn.setOnClickListener(view -> {
            AddSongDialog addSongDialog = new AddSongDialog();
            addSongDialog.show(getSupportFragmentManager(), "add song dialog");
            // get from applyAddSong

        });

        register_pausePlayingAudio();
        register_playPlayingAudio();
        register_closePlayingAudio();

        showListOfSongFragment();

//        Log.d("isPlaying", "isMyServiceRunning: "+isMyServiceRunning(MusicPlayerService.class));


    }
   /* private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/

    //list fragment
    private void showListOfSongFragment() {

        SongRecyclerView_UpdateUI_Fragment songRecyclerViewFragment = new SongRecyclerView_UpdateUI_Fragment(recyclerViewListener, isPlaying);
        backBigPic.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_list_and_big, songRecyclerViewFragment, REGISTER_FRAGMENT_TAG);

        fragmentTransaction.commit();

    }


    @Override
    public void nextClick() {

        intent = new Intent(MainActivity.this, MusicPlayerService.class);
        intent.putExtra("command", Actions.NEXT_SONG);

        startService(intent);
    }

    @Override
    public void prevClick() {

        intent = new Intent(MainActivity.this, MusicPlayerService.class);
        intent.putExtra("command", Actions.PREV_SONG);

        startService(intent);
    }

    @Override
    public void playClick() {
        Log.d("TAG", "playClick: ");
        //LoadSongProgressBar.setVisibility(View.VISIBLE);

//        isPlaying = true;
        intent = new Intent(MainActivity.this, MusicPlayerService.class);
        intent.putExtra("command", Actions.PAUSE_SONG);

//        playBtn.setImageResource(R.drawable.pausexhdpi);

        startService(intent);
    }

    @Override
    public void pauseClick() {
        Log.d("TAG", "pauseClick: ");
//        isPlaying = false;
        intent = new Intent(MainActivity.this, MusicPlayerService.class);
        intent.putExtra("command", Actions.PAUSE_SONG);

//        playBtn.setImageResource(R.drawable.playxhdpi);

        startService(intent);
    }


    /**
     * parameter from the interface AddSongDialogListener
     */
    @Override
    public void applyAddSong(String songName, String songUrl, Uri imgUri) {

        try {
            managerListSongs.addSong(songUrl, imgUri.toString(), songName);

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(R.id.RelativeLayout),
                            "" + e.getMessage(), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        managerListSongs.SaveList();

        //Hide the Keyboard
        com.example.shiftmanagerhit.Utility.HidesKeyboard.hideKeyboard(this);
    }

}

