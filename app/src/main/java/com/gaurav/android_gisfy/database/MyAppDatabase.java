package com.gaurav.android_gisfy.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class},version = 1,exportSchema = true)
public abstract class MyAppDatabase extends RoomDatabase
{

    public abstract MyDao myDao();
}
