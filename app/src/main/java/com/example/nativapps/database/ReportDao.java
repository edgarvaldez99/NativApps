package com.example.nativapps.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Report item);

    @Query("SELECT * FROM report")
    public LiveData<List<Report>> getAll();
}
