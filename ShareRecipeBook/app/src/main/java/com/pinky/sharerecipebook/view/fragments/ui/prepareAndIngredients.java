package com.pinky.sharerecipebook.view.fragments.ui;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pinky.sharerecipebook.R;


public class prepareAndIngredients extends Fragment {


    private final String textGet;
    private TextView prepareAndIngredientsTextView;

    public prepareAndIngredients(String text) {
        this.textGet = text;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_fragment_prepare_and_ingredients, container, false);
        prepareAndIngredientsTextView = view.findViewById(R.id.ui_fragment_prepareAndIngredients_text);
        prepareAndIngredientsTextView.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareAndIngredientsTextView.setText(textGet);
    }
}