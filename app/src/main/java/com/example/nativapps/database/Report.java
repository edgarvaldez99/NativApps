package com.example.nativapps.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Report {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "description")
    public String description;
}
