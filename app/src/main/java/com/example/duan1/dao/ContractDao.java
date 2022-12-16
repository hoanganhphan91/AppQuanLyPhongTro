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
    @Query("select*from Contract order by idContract desc limit 1")
    Contract getContractNewest();
    @Query("select*from Contract where roomCode = :roomCode and status = 1 limit 1")
    Contract getContractByRoomCode(String roomCode);
    @Query("select*from Contract where idContract = :idContract limit 1")
    Contract getContractById(int idContract);

    @Query("select*from Contract where status = 1 and endingDate <= :date and roomCode like :keyword")
    List<Contract> getSearchWait(String date, String keyword);

    @Query("select*from Contract where status = 1 and endingDate<= :date and roomCode like :keyword and statingDate >= :date2 and endingDate <= :date2")
    List<Contract> getSearchFilterWait(String date, String date2, String keyword);

    @Query("select*from Contract where status = 1 and endingDate > :date and roomCode like :keyword")
    List<Contract> getSearchEffect(String date, String keyword);

    @Query("select*from Contract where status = 1 and endingDate > :date and roomCode like :keyword and statingDate >= :date2 and endingDate <= :date2")
    List<Contract> getSearchFilterEffect(String date, String date2, String keyword);

    @Query("select*from Contract where status = 0 and roomCode like :keyword")
    List<Contract> getSearchExpire(String keyword);

    @Query("select*from Contract where status = 0 and roomCode like :keyword and statingDate >= :date2 and endingDate <= :date2")
    List<Contract> getSearchFilterExpire(String date2, String keyword);
}