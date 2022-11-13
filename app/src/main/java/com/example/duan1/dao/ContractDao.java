package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.Contract;

import java.util.List;

@Dao
public interface ContractDao {
    @Insert
    void insert(Contract contract);
    @Update
    void update(Contract contract);
    @Delete
    void delete(Contract contract);
    @Query("select*from Contract")
    List<Contract> getAll();
}
