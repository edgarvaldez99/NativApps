package com.example.nativapps.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.nativapps.database.Report;
import com.example.nativapps.repository.ReportRepository;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateReportViewModel extends ViewModel {

    private final ReportRepository repository;

    @Inject
    CreateReportViewModel(ReportRepository repository)
    {
        this.repository = repository;
    }

    public void insert(String description, File image) {
        Report report = new Report();
        report.description = description;
        report.image = image.getAbsolutePath();
        this.repository.insert(report);
    }
}
