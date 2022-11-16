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

    @Query("select*from Room join Contract on Room.roomCode = Contract.roomCode " +
            "where roomCode = :roomCode and status = 1")
    Room checkRoom(String roomCode);
}
