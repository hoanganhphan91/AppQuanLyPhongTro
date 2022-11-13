package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityMainBinding;
import com.example.duan1.model.Account;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DbMotel db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = DbMotel.getInstance(this);

        //Insert data
        try {
            if(db.accountDao().getAll().size() == 0){
                //insert account
                db.accountDao().insert(new Account("admin","admin","Nguyễn Văn A","0123456789","host"));
                db.accountDao().insert(new Account("user1","user1","Nguyễn Văn B","0123456789","manager"));
                db.accountDao().insert(new Account("user2","user2","Nguyễn Văn C","0123456789","manager"));
                db.accountDao().insert(new Account("user3","user3","Nguyễn Văn D","0123456789","manager"));
                db.accountDao().insert(new Account("user4","user4","Nguyễn Văn E","0123456789","manager"));
            }
        }catch (Exception e){
            Toast.makeText(this, "Loi insert du lieu vao database!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //Lấy bảng Accout từ database
        List<Account> listAcc = db.accountDao().getAll();

        //Day data len list view
        List<String> list = new ArrayList<>();
        for (Account o: listAcc) {
            list.add(o.getUsername() + " - " + o.getPassword() + " - " + o.getName() + " - " + o.getPhone() + " - " + o.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        binding.lv.setAdapter(adapter);
    }
}