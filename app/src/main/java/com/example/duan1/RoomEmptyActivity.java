package com.example.duan1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityRoomEmptyBinding;
import com.example.duan1.model.Room;
import com.example.duan1.model.RoomType;
import com.example.duan1.model.Utility;
import com.example.duan1.model.UtilityStatus;

import java.net.URI;
import java.util.List;

public class RoomEmptyActivity extends AppCompatActivity {
    ActivityRoomEmptyBinding binding;
    DbMotel db;
    RoomType roomType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomEmptyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Room room = (Room) getIntent().getSerializableExtra("room");
        db = DbMotel.getInstance(this);
        roomType = db.roomTypeDao().getRoomTypeById(room.getIdRoomType());
        binding.setRoom(room);
        binding.setRoomType(roomType);
        //btn Back
        binding.imgBack.setOnClickListener(view -> finish());
        //image room
        Glide.with(this).load(Uri.parse(room.getImage())).into(binding.imgRoom);
        //Show utility
        showUtilyti();
        //Delete
        binding.btnDeleteRoom.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            builder.setTitle("Xóa phòng");
            builder.setMessage("Bạn có chắc xóa phòng?");
            builder.setNegativeButton("Xác nhận", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                try{
                    db.roomDao().delete(room);
                }catch (Exception e){
                    Toast.makeText(this, "Lỗi xóa phòng", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                finish();
            });
            builder.setPositiveButton("Hủy bỏ", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            builder.create().show();
        });
        //create contract
        binding.btnCreateContract.setOnClickListener(view -> {
            Intent intent = new Intent(this,CreateContractActivity.class);
            intent.putExtra("room",room);
            startActivity(intent);
        });
    }

    private void showUtilyti() {
        List<Utility> utilityList = db.utilityDetailDao().getUtility(roomType.getIdRoomType());
        for (Utility u: utilityList) {
            switch (u.getIdUtility()){
                case 1:
                    binding.imgMoneyBag.setImageResource(R.drawable.money_bag2);
                    break;
                case 2:
                    binding.imgAlarm.setImageResource(R.drawable.alarm2);
                    break;
                case 3:
                    binding.imgCCTV.setImageResource(R.drawable.cctv_camera2);
                    break;
                case 4:
                    binding.imgCooking.setImageResource(R.drawable.cooking2);
                    break;
                case 5:
                    binding.imgFan.setImageResource(R.drawable.fan2);
                    break;
                case 6:
                    binding.imgBed.setImageResource(R.drawable.bed2);
                    break;
                case 7:
                    binding.imgFidge.setImageResource(R.drawable.fridge2);
                    break;
                case 8:
                    binding.imgTelevision.setImageResource(R.drawable.television2);
                    break;
                case 9:
                    binding.imgWifi.setImageResource(R.drawable.wifi2);
                    break;
                case 10:
                    binding.imgMotorcycle.setImageResource(R.drawable.motorcycle2);
                    break;
                case 11:
                    binding.imgWashingMachine.setImageResource(R.drawable.washing_machine2);
                    break;
                case 12:
                    binding.imgAirConditioner.setImageResource(R.drawable.air_conditioner2);
                    break;
            }
        }
    }
}