package com.example.nativapps.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nativapps.database.Report;
import com.example.nativapps.repository.ReportRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final ReportRepository repository;

    @Inject
    MainViewModel(ReportRepository repository)
    {
        this.repository = repository;
    }

    public LiveData<List<Report>> getAll() {
        return this.repository.getAll();
    }
}
