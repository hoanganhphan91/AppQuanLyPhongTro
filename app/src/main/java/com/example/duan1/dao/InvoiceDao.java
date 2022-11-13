package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.Invoice;

import java.util.List;

@Dao
public interface InvoiceDao {
    @Insert
    void insert(Invoice o);
    @Update
    void update(Invoice o);
    @Delete
    void delete(Invoice o);
    @Query("select*from Invoice")
    List<Invoice> getAll();
}
