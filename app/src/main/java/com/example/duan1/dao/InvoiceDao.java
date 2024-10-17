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
    //get invoice temp
    @Query("select*from Invoice where status = 2 and idContract = :idContract order by date limit 1 ")
    Invoice getInvoiceNow(int idContract);
    //get List Invoice by idContract
    @Query("select*from Invoice where idContract = :idContract and status in (0,1) order by status ,date desc")
    List<Invoice> getInvoiceByIdContract(int idContract);
    //Get invoice not pay - chưa thanh toán bất kì
    @Query("select*from Invoice where idContract = :idContract and status = 0")
    Invoice getInvoiceNotPayByIdContract(int idContract);
}
