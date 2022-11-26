package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.ServiceDetail;

import java.util.List;
@Dao
public interface ServiceDetailDao {
    @Insert
    void insert(ServiceDetail o);
    @Update
    void update(ServiceDetail o);
    @Delete
    void delete(ServiceDetail o);
    @Query("select*from ServiceDetail")
    List<ServiceDetail> getAll();

    @Query("select*from ServiceDetail where idInvoice = :idInvoice order by idServiceDetail desc")
    List<ServiceDetail> getServiceDetailByIdInvoidce(int idInvoice);
}
