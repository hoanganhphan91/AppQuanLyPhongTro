package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.Utility;
import com.example.duan1.model.UtilityDetail;

import java.util.List;

@Dao
public interface UtilityDetailDao {
    @Insert
    void insert(UtilityDetail o);
    @Update
    void update(UtilityDetail o);
    @Delete
    void delete(UtilityDetail o);
    @Query("select*from UtilityDetail")
    List<UtilityDetail> getAll();

    @Query("select*from UtilityDetail udl join Utility u on udl.idUtility = u.idUtility where idRoomType = :idRoomType")
    List<Utility> getUtility(int idRoomType);

    @Query("select u.name from UtilityDetail udl join Utility u on udl.idUtility = u.idUtility where idRoomType = :idRoomType")
    List<String> getListNameUtility(int idRoomType);
}