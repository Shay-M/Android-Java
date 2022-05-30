package com.example.mediaplayer.ManagerSongs;/* Created by Shay Mualem 22/07/2021 */

import static com.example.mediaplayer.ManagerSongs.ManagerSaveSongs.saveSongList;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.example.mediaplayer.SongsRecyclerView.SongItem;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerListSongs {


    private static ManagerListSongs instead;
    private final ArrayList<String> listOfUrlSongs;
    private ArrayList<SongItem> listOfSongsItems;
    private int currentPlaying;
    private RecyclerViewUpdateUIListener listener;


    private ManagerListSongs() {
        currentPlaying = 0;
        listOfUrlSongs = new ArrayList<>();
        listOfSongsItems = new ArrayList<>();

        boolean firstTime = false;
        try {
            //try to get storage list
            listOfSongsItems = ManagerSaveSongs.readSongList(null);

        } catch (Exception e) {
            //if not make fresh list
            firstTime = true;
            Log.d("ManagerListSongs", "make fresh list | " + e.getMessage());

            for (int i = 0; i < 3; i++) {
                listOfSongsItems.add(new SongItem("https://www.syntax.org.il/xtra/bob.m4a", "One More Cup Of Coffee", "file:///android_asset/bob_img_0.jpg", "03:47"));
                listOfSongsItems.add(new SongItem("https://www.syntax.org.il/xtra/bob1.m4a", "The Main In me", "file:///android_asset/bob_img_1.jpg", "05:31"));
                listOfSongsItems.add(new SongItem("https://www.syntax.org.il/xtra/bob2.mp3", "Sara", "file:///android_asset/bob_img_2.jpg", "03:06"));
            }

            for (SongItem songItm : listOfSongsItems) {
                listOfUrlSongs.add(songItm.getUrl());
            }
        }

        // load from Device
        if (!firstTime)
            for (SongItem songItm : listOfSongsItems) {
                listOfUrlSongs.add(songItm.getUrl());
            }

    }


    //singleton
    public static ManagerListSongs getInstance() {
        if (instead == null)
            instead = new ManagerListSongs();
        return instead;
    }

    public void setCurrentPlaying(int currentPlaying) {
        this.currentPlaying = currentPlaying;
    }

    public int getCurrentPlaying(boolean next_p) {

        if (next_p) {
            currentPlaying++;
            if (currentPlaying == listOfUrlSongs.size())
                currentPlaying = 0;
        } else {
            currentPlaying--;
            if (currentPlaying < 0)
                currentPlaying = listOfUrlSongs.size() - 1;

        }

        return currentPlaying;
    }

    public ArrayList<SongItem> getListOfSongsItems() {
        return listOfSongsItems;
    }

    public ArrayList<String> getListOfUrlSongs() {
        return listOfUrlSongs;
    }

    public void SaveList() {
        saveSongList(null, listOfSongsItems);
    }

    public void MoveSongList(int fromPosition, int toPosition) {

//        Log.d("MoveSongList", "fromPosition: " + fromPosition);
//        Log.d("MoveSongList", "toPosition: " + toPosition);
//        Log.d("MoveSongList", "currentPlaying: " + currentPlaying);

        if (currentPlaying >= toPosition && currentPlaying < fromPosition)
            currentPlaying++;
        else if (currentPlaying == fromPosition)
            currentPlaying = toPosition;
       else if (currentPlaying == toPosition)
            currentPlaying--;


//        Log.d("MoveSongList", "new currentPlaying: " + currentPlaying);


        SaveList();
    }

    public void RemovingSongFromList(int position) {
        listOfSongsItems.remove(position);
        listOfUrlSongs.remove(position);
        if (position == currentPlaying)
            currentPlaying = currentPlaying - 1;
        SaveList();
    }

    /**
     * @param stringUrl song url to add
     * @throws Exception if the url is not in a valid
     */
    public void addSong(String stringUrl, String imgUri, String songName) throws Exception {

        //get song name
        String NameOfSongFromUrl = stringUrl.substring(stringUrl.lastIndexOf('/') + 1);
        if (!NameOfSongFromUrl.contains("."))
            throw new Exception("Missing file in link, Unsupported file");

        // Url Validator
        try {
            new URL(stringUrl).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            Log.d("addSong", "the URL is not in a valid form: " + e.getMessage());
            throw new Exception("the URL is not in a valid form. " + e.getMessage());
        }
        Log.d("ManagerListSongs", "addSong: " + listOfUrlSongs);

        //add song to url list
        listOfUrlSongs.add(stringUrl);

        // if no name given, get from url
        if (songName.isEmpty())
            songName = NameOfSongFromUrl;

        listOfSongsItems.add(new SongItem(stringUrl, songName, imgUri, SongDuration(stringUrl)));
        SaveList();
        Log.d("add", "listOfUrlSongs: " + listOfUrlSongs);
        listener.onUpdateListItem();

    }

    private String SongDuration(String pathStr) {
        // load data file, filePath is of type String which holds the path of file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

        try {
            metaRetriever.setDataSource(pathStr, new HashMap<>());
        } catch (Exception e) {
            Log.d("TAG", "SongDuration: "+e.getMessage());
            return "0";
        }

        // get mp3 info
        String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        long dur = Long.parseLong(duration);

        // convert duration to minute:seconds
        String seconds = String.valueOf((dur % 60000) / 1000);
        String minutes = String.valueOf(dur / 60000);
        String out; //= minutes + ":" + seconds;

        if (seconds.length() == 1)
            out = "0" + minutes + ":0" + seconds;
        else
            out = "0" + minutes + ":" + seconds;

        // close object
        metaRetriever.release();

        return out;

    }

    public void setListener(RecyclerViewUpdateUIListener listener) {
        this.listener = listener;
    }

    public interface RecyclerViewUpdateUIListener {
        void onUpdateListItem();
    }


}

//            listOfUrlSongsToAddFirstTime.add("https://www.mboxdrive.com/Raptor-Call%20of%20The%20Shadows.mp3");

//            https://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist

