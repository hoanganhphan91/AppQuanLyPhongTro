package com.example.duan1;

import static com.example.duan1.base.BaseCheckValid.checkEmptyString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1.adapter.SpRoomTypeAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityCreateRoomBinding;
import com.example.duan1.model.Room;
import com.example.duan1.model.RoomType;

import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class CreateRoomActivity extends AppCompatActivity {

    ActivityCreateRoomBinding binding;
    DbMotel db;
    String pathImage = "";
    SpRoomTypeAdapter spRoomTypeAdapter;
    List<RoomType> roomTypeList;
    List<String> listUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = DbMotel.getInstance(this);
        roomTypeList = db.roomTypeDao().getAll();
        spRoomTypeAdapter = new SpRoomTypeAdapter(this, roomTypeList);
        binding.spRoomType.setAdapter(spRoomTypeAdapter);
        binding.spRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               hanleItemSpinnerSelect(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       if(roomTypeList.size() > 0) {
           hanleItemSpinnerSelect(0);
       }

        binding.imgBack.setOnClickListener(view -> finish());
        binding.btnType.setOnClickListener(v -> startActivity(new Intent(this, CreateRoomTypeActivity.class)));

        binding.imgRoom.setOnClickListener(v -> {
            TedImagePicker.with(this).start(uri -> {
                Glide.with(this).load(uri).into(binding.imgRoom);
                binding.imgRoom.setMaxHeight(250);
                pathImage = uri.toString();
            });
        });

        binding.btnCreateRoom.setOnClickListener(v -> {
            String floor = binding.edtang.getText().toString();
            String codeRoom = binding.edRoomcode.getText().toString();
            String describe = binding.edDescribe.getText().toString();
            RoomType roomType = binding.getRoomType();
            if(checkEmptyString(floor,codeRoom,describe,pathImage)){
                Room room = new Room(codeRoom,Integer.parseInt(floor),describe,pathImage,roomType.getIdRoomType());
                try {
                    db.roomDao().insert(room);
                    finish();
                }catch (Exception e){
                    Toast.makeText(this, "Mã phòng bị trùng!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Bạn vui lòng không bỏ trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hanleItemSpinnerSelect(int i) {
        RoomType roomType = roomTypeList.get(i);
        binding.setRoomType(roomType);
        listUtility = db.utilityDetailDao().getListNameUtility(roomType.getIdRoomType());
        String utility = "";
        for (String s:listUtility) {
            utility += s+", ";
        }
        utility = utility.substring(0,utility.length()-2);
        binding.tvListUtility.setText("Tiện ích: " + utility);
    }

    @Override
    protected void onResume() {
        super.onResume();
        roomTypeList.clear();
        roomTypeList.addAll(db.roomTypeDao().getAll());
        spRoomTypeAdapter.notifyDataSetChanged();
    }
}