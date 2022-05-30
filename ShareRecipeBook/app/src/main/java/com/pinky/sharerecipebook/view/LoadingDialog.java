package com.pinky.sharerecipebook.view;/* Created by Shay Mualem 29/09/2021 */

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.pinky.sharerecipebook.R;

public class LoadingDialog {
    private Fragment fragment;
    private AlertDialog dialog;
    private ImageView imageLoadingView;

    public LoadingDialog(Fragment mfragment) {
        fragment = mfragment;

    }

    public void startLoadingDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());

        LayoutInflater inflater = fragment.getLayoutInflater();
//        builder.setView(inflater.inflate(R.layout.loading_alert_dialog, null));

        View dialogView = inflater.inflate(R.layout.loading_alert_dialog, null);
        builder.setView(dialogView);

        imageLoadingView = dialogView.findViewById(R.id.imageLoadingView);
        Glide
                .with(fragment)
                .asGif()
                .load(R.drawable.mixer)
                .into(imageLoadingView);

        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }

    public void dismissLoadingDialog() {
        dialog.dismiss();
    }
}
