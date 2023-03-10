package com.example.nativapps.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CameraViewModel extends ViewModel {

    private final MutableLiveData<File> _file = new MutableLiveData<>();

    @Inject
    CameraViewModel() {}

    public void setImage(File image) {
        _file.setValue(image);
    }

    public LiveData<File> getImage() {
        return _file;
    }
}
