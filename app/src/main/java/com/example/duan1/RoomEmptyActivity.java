package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duan1.databinding.ActivityRoomEmptyBinding;

public class RoomEmptyActivity extends AppCompatActivity {
    ActivityRoomEmptyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomEmptyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}