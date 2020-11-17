package com.ahmetcet.travel_route_optimization_app.ui.createRoute;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateRouteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateRouteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}