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

    @Query("select*from RoomType where idRoomType = :idRoomType")
    RoomType getRoomTypeById(int idRoomType);

    @Query("select*from RoomType order by idRoomType Desc limit 1")
    RoomType getRoomTypeNewest();

    @Query("select * from RoomType rt join Room r on rt.idRoomType = r.idRoomType " +
            "join Contract ct on ct.roomCode= r.roomCode where ct.idContract = :idContract limit 1")
    RoomType getRTByIdContract(int idContract);
}
