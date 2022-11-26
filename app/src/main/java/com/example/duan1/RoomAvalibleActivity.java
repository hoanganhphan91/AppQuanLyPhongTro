package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.duan1.adapter.ViewPagerAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityRoomAvalibleBinding;
import com.example.duan1.model.Room;
import com.example.duan1.viewmodel.RoomViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class RoomAvalibleActivity extends AppCompatActivity {
    final String arrTitle[] = new String[]{"Dịch vụ","Thành viên", "Hóa đơn","Hợp đồng"};
    ActivityRoomAvalibleBinding binding;
    Room room;
    DbMotel db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomAvalibleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        room = (Room) getIntent().getSerializableExtra("room");
        RoomViewModel model = new ViewModelProvider(this).get(RoomViewModel.class);
        model.setRoom(room);

        db = DbMotel.getInstance(this);
        //Set viewpage tablayout
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        binding.viewPager.setAdapter(pagerAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
           tab.setText(arrTitle[position]);
        });
       mediator.attach();
       binding.imgBack.setOnClickListener(view -> finish());
    }
}