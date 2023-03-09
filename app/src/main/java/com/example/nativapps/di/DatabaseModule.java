package com.example.nativapps.di;

import android.content.Context;

import androidx.room.Room;

import com.example.nativapps.database.AppRoomDatabase;
import com.example.nativapps.database.ReportDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Singleton
    @Provides
    public AppRoomDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppRoomDatabase.class, "nativapps").build();
    }

    @Provides
    public ReportDao provideReportDao(AppRoomDatabase appDatabase) {
        return appDatabase.reportDao();
    }
}
