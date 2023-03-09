package com.example.nativapps.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Report.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
}
