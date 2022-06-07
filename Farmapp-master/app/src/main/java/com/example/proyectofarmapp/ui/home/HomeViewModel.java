package com.example.proyectofarmapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    public HomeViewModel() {
        MutableLiveData<Object> mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
}