package com.pinky.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pic = findViewById(R.id.ling);

        CamTry cam = new CamTry(this);
        Uri name = cam.dispatchTakePictureIntent();

        Log.d("name", "onCreate: " + name);
//        https://github.com/bumptech/glide
        Glide.with(this).load(name).into(pic);

    }


}

