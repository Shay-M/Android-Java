package com.example.shiftmanagerhit.ui.mangerShifts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MangerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MangerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}