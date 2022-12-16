package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.duan1.adapter.FloorAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityRoomManageBinding;
import com.example.duan1.model.Floor;
import com.example.duan1.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomManageActivity extends AppCompatActivity {
    ActivityRoomManageBinding binding;
    List<Room> listRoom;
    List<Floor> listFloor;
    FloorAdapter floorAdapter;
    DbMotel db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = DbMotel.getInstance(this);
        binding.imgBack.setOnClickListener(view -> finish());
        listRoom = db.roomDao().getAll();
        listFloor = new ArrayList<>();
        subListRoom();
        floorAdapter = new FloorAdapter(this, listFloor, (room, status) -> {
            //status true - room avalible , false - room empty
            if(status){
                Intent intent = new Intent(this,RoomAvalibleActivity.class);
                intent.putExtra("room",room);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this,RoomEmptyActivity.class);
                intent.putExtra("room",room);
                startActivity(intent);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rv.setLayoutManager(manager);
        binding.rv.setAdapter(floorAdapter);

        binding.cboFilter.setOnCheckedChangeListener((compoundButton, b) -> {
            listRoom.clear();
            String keyword = binding.edSearch.getText().toString();
            if(b){
                listRoom.addAll(db.roomDao().filterRoom("%" + binding.edSearch.getText().toString() + "%"));
            }else {
                listRoom.addAll(db.roomDao().searchRoom("%" + binding.edSearch.getText().toString() + "%"));
            }
            subListRoom();
            floorAdapter.notifyDataSetChanged();
        });

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listRoom.clear();
                if(binding.cboFilter.isChecked()){
                    listRoom.addAll(db.roomDao().filterRoom("%" + charSequence + "%"));
                }else {
                    listRoom.addAll(db.roomDao().searchRoom("%" + charSequence + "%"));
                }
                subListRoom();
                floorAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.fabAdd.setOnClickListener(v -> startActivity(new Intent(this,CreateRoomActivity.class)));

    }

    private void subListRoom() {
        listFloor.clear();
        if(listRoom.size() != 0){
            List<Room> listSub = new ArrayList<>();
            int floor = -999;
            for (Room room : listRoom) {
                if(room.getFloor() != floor){
                    if(listSub.size() != 0){
                        listFloor.add(new Floor(floor,listSub));
                    }
                    listSub = new ArrayList<>();
                    listSub.add(room);
                    floor = room.getFloor();
                }else {
                    listSub.add(room);
                }
            }
            listFloor.add(new Floor(floor,listSub));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        listRoom = db.roomDao().getAll();
        subListRoom();
        floorAdapter.notifyDataSetChanged();
    }
}