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
    //get invoice newest by idContract
    @Query("select*from Invoice where idContract = :idContract order by date desc limit 1")
    Invoice getInvoiceNewestByIdContract(int idContract);
}
