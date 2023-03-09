package com.example.nativapps.repository;

import androidx.lifecycle.LiveData;

import com.example.nativapps.database.Report;
import com.example.nativapps.database.ReportDao;

import java.util.List;

import javax.inject.Inject;

public class ReportRepository {

    private final ReportDao reportDao;

    @Inject
    ReportRepository(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public LiveData<List<Report>> getAll() {
        return this.reportDao.getAll();
    }

    public void insert(Report item) {
        this.reportDao.insert(item);
    }
}
