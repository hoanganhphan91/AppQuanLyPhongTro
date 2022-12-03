package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.Service;

import java.util.List;

@Dao
public interface ServiceDao {
    @Insert
    void insert(Service o);
    @Update
    void update(Service o);
    @Delete
    void delete(Service o);
    @Query("select*from Service")
    List<Service> getAll();
    @Query("select*from Service where name like :keyword")
    List<Service> search(String keyword);
    @Query("select*from Service where idService = :idService")
    Service getServiceById(int idService);
}
