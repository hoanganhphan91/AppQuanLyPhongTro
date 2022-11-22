package com.example.duan1.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.duan1.model.Member;

import java.util.List;

@Dao
public interface MemberDao {
    @Insert
    void insert(Member o);
    @Update
    void update(Member o);
    @Delete
    void delete(Member o);
    @Query("select*from Member")
    List<Member> getAll();
    @Query("select*from Member where idContract = :idContract")
    List<Member> getMemberByIdContract(int idContract);
}
