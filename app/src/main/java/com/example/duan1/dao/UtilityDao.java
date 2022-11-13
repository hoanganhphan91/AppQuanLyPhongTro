package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.Utility;

import java.util.List;

@Dao
public interface UtilityDao {
    @Insert
    void insert(Utility o);
    @Update
    void update(Utility o);
    @Delete
    void delete(Utility o);
    @Query("select*from Utility")
    List<Utility> getAll();
}
