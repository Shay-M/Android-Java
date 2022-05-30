package com.example.shiftmanagerhit.ui.EmployeeShiftsHave;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployeeShiftsHaveViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EmployeeShiftsHaveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}