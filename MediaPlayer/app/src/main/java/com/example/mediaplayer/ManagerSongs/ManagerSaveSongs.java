package com.example.mediaplayer.ManagerSongs;/* Created by Shay Mualem 03/08/2021 */

import android.content.Context;
import android.util.Log;

import com.example.mediaplayer.SongsRecyclerView.SongItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ManagerSaveSongs {
    static public Context static_context = null;

    public static void saveSongList(Context context, List<SongItem> songs) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = static_context.openFileOutput("songs_list", Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(songs);
                    oos.close();
                    Log.d("saveSongList", "saveSongList!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static ArrayList<SongItem> readSongList(Context context) throws Exception {

        ArrayList<SongItem> songs;

        try {
            FileInputStream fis = static_context.openFileInput("songs_list");
            ObjectInputStream ois = new ObjectInputStream(fis);
            songs = (ArrayList<SongItem>) ois.readObject();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception(e);

        }

        return songs;
    }
}
