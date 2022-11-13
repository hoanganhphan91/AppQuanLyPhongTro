package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.RoomType;

import java.util.List;

@Dao
public interface RoomTypeDao {
    @Insert
    void insert(RoomType o);
    @Update
    void update(RoomType o);
    @Delete
    void delete(RoomType o);
    @Query("select*from RoomType")
    List<RoomType> getAll();
}
