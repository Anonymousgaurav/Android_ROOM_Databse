package com.gaurav.android_gisfy.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addUser(User user);

    @Query("select * from users")
    public List<User> getUsers();

    @Insert
    void insert(User user);


}
