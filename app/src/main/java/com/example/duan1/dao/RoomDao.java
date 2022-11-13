package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.Room;

import java.util.List;

@Dao
public interface RoomDao {
    @Insert
    void insert(Room o);
    @Update
    void update(Room o);
    @Delete
    void delete(Room o);
    @Query("select*from Room")
    List<Room> getAll();
}
