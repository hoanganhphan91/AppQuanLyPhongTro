package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duan1.databinding.ActivityRoomManageBinding;

public class RoomManageActivity extends AppCompatActivity {
    ActivityRoomManageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}