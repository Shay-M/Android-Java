package com.pinky.sharerecipebook.view.fragments.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.adapters.CommentAdapter;
import com.pinky.sharerecipebook.models.Comment;
import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.repositories.FirebaseDatabaseRepository;
import com.pinky.sharerecipebook.utils.HidesKeyboard;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class uiCommentsFragment extends Fragment {

    private TextInputLayout commentEditText;
    private CommentAdapter commentAdapter;
    private Map<String, Comment> mcommentHashMap;
    private String mrecipeId;
    private String mfirebaseUserIdCommentl;
    private User muserSender;
    private Recipe mownerRecipe;
    final String API_TOKEN_KEY = "AAAAnhV6-hM:APA91bGf-3U4CbbpRZWDOXa_jRLp4fmGCrj8C2qdWMF7q82umHfj5-aVsJI_jj_8mGFDbyh3v_dpg_9EuMIf4ePq0aiJ7isGVbE8eiO_kxgjwC2t_HCqipD3poyvSRfOuOE0LA-M5LXq";


    public uiCommentsFragment(String recipeId, Map<String, Comment> commentHashMap, String firebaseUserIdComment, User userSender, Recipe ownerRecipe) {

        if (commentHashMap == null)
            mcommentHashMap = new HashMap<>();
        else
            mcommentHashMap = commentHashMap;

        mfirebaseUserIdCommentl = firebaseUserIdComment;
        mrecipeId = recipeId;
        muserSender = userSender;
        mownerRecipe = ownerRecipe;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Collections.sort(arrayList, new Comparator<HashMap< String,Comment >>() {});

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_comments, container, false);
        commentEditText = view.findViewById(R.id.ui_fragment_comments_textinput);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_of_comments);
        recyclerView.setHasFixedSize(true);

        Collection<Comment> values = mcommentHashMap.values();
        ArrayList<Comment> listOfValues = new ArrayList<>(values);
        listOfValues.sort(Comparator.comparing(Comment::getPostDate).reversed());
        commentAdapter = new CommentAdapter(listOfValues, getActivity());


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(commentAdapter);

        if(muserSender.getName().equals("Guest")) {
            commentEditText = view.findViewById(R.id.ui_fragment_comments_textinput);
            commentEditText.setEnabled(false);
            commentEditText.setAlpha(0.5f);
            commentEditText.setHint(getString(R.string.comments_login));
        } else {
            commentEditText.setEndIconOnClickListener(v -> {
                String commentTxt = commentEditText.getEditText().getText().toString();

                if (commentEditText.getEditText().getText().toString().length() > 2) {
                    sendComment(commentTxt);
                    commentEditText.getEditText().setText("");
                } //else commentEditText.setError("Write a comment");

                if (!muserSender.getDeviceTokenId().equals(mownerRecipe.getFirebaseDeviceTokenMade())) {
                    sendCommentNotification(muserSender.getName(), mownerRecipe.getFirebaseDeviceTokenMade(), mownerRecipe.getTitle());
                }
            });
        }
    }

    private void sendComment(String commentTxt) {
        HidesKeyboard.hideKeyboard(this.getActivity());

        Comment comment = new Comment(commentTxt, mfirebaseUserIdCommentl);

        String folder = "recipe";
        String fieldToChange = "commentArrayListHashMap";
        FirebaseDatabaseRepository.getInstance().changeDataFirebase(folder, mrecipeId, fieldToChange, comment);
        mcommentHashMap.put("temp", comment);
        commentAdapter.UpdateAdapter(comment);
//        commentAdapter.notifyDataSetChanged();
        commentAdapter.notifyItemInserted(0);

    }

    public void sendCommentNotification (String senderName, String ownerToken, String recipeName)  {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "key=" + API_TOKEN_KEY);
                    conn.setDoOutput(true);

                    final JSONObject rootObject  = new JSONObject();
                    rootObject.put("to", ownerToken);
                    rootObject.put("data",
                            new JSONObject().put("senderName", senderName)
                                    .put("recipeName", recipeName)
                                    .put("type", "comment"));
                    rootObject.put("priority", "high");

                    OutputStream os = conn.getOutputStream();
                    os.write(rootObject.toString().getBytes());
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.d("CommentNotification", "\nSending 'POST' request to URL : " + url);
                    Log.d("CommentNotification", "Post parameters : " + rootObject.toString());
                    Log.d("CommentNotification", "Post parameters : " + rootObject.toString());
                    Log.d("CommentNotification", "Response Code : " + responseCode);
                    Log.d("CommentNotification", "Response Code : " + conn.getResponseMessage());

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AsyncTask.execute(runnable);
    }
}