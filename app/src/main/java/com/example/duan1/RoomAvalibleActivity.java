package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.duan1.databinding.ActivityRoomAvalibleBinding;

public class RoomAvalibleActivity extends AppCompatActivity {
    final String arrTitle[] = new String[]{"Dịch vụ","Thành viên", "Hóa đơn",""};
    ActivityRoomAvalibleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomAvalibleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




    }
}