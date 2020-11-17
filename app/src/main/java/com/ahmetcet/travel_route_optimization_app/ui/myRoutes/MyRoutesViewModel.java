package com.ahmetcet.travel_route_optimization_app.ui.myRoutes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyRoutesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyRoutesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}