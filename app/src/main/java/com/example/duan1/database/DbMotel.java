package com.example.duan1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.duan1.dao.AccountDao;
import com.example.duan1.dao.ContractDao;
import com.example.duan1.dao.InvoiceDao;
import com.example.duan1.dao.MemberDao;
import com.example.duan1.dao.RoomDao;
import com.example.duan1.dao.RoomTypeDao;
import com.example.duan1.dao.ServiceDao;
import com.example.duan1.dao.ServiceDetailDao;
import com.example.duan1.dao.UtilityDao;
import com.example.duan1.dao.UtilityDetailDao;
import com.example.duan1.model.Account;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Member;
import com.example.duan1.model.Room;
import com.example.duan1.model.RoomType;
import com.example.duan1.model.Service;
import com.example.duan1.model.ServiceDetail;
import com.example.duan1.model.Utility;
import com.example.duan1.model.UtilityDetail;

@Database(entities = {Account.class, Contract.class, Invoice.class, Member.class, Room.class,
        RoomType.class, Service.class, ServiceDetail.class, Utility.class, UtilityDetail.class},
        version = 2, exportSchema = false)
public abstract class DbMotel extends RoomDatabase {
    public abstract AccountDao accountDao();
    public abstract ContractDao contractDao();
    public abstract InvoiceDao invoiceDao();
    public abstract MemberDao memberDao();
    public abstract RoomDao roomDao();
    public abstract RoomTypeDao roomTypeDao();
    public abstract ServiceDao serviceDao();
    public abstract ServiceDetailDao serviceDetailDao();
    public abstract UtilityDao utilityDao();
    public abstract UtilityDetailDao utilityDetailDao();

    private static final String DB_NAME = "motel.db";
    private static DbMotel instance;
    public static synchronized DbMotel getInstance(Context context){
        if(instance==null){
            instance = androidx.room.Room.databaseBuilder(context.getApplicationContext(),DbMotel.class,DB_NAME)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public SQLiteDatabase getReadableDatabase() {
        return null;
    }

    public Boolean updatepassword(String password) {
        return null;
    }
}
