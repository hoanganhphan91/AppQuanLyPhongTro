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
    @Query("select*from Room order by floor ")
    List<Room> getAll();

    @Query("select*from Room join Contract on Room.roomCode = Contract.roomCode " +
            "where Room.roomCode = :roomCode and status = 1")
    Room checkRoom(String roomCode);

    @Query("select*from Room where roomCode like :search order by floor ")
    List<Room> searchRoom(String search);

    @Query("select*from Room where roomCode like :search and roomCode not in (select roomCode from Contract where status = 1)")
    List<Room> filterRoom(String search);
}