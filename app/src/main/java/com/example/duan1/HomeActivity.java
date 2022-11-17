package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duan1.model.Account;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Account acc = new Account("admin","admin","Nguyễn Văn A","0123456789","host");
    }
}