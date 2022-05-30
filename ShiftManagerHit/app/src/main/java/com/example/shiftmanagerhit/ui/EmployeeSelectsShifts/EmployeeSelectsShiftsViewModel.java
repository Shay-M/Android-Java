package com.example.shiftmanagerhit.ui.EmployeeSelectsShifts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployeeSelectsShiftsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EmployeeSelectsShiftsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}