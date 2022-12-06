package com.example.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.base.BaseCheckValid;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityCreateRoomTypeBinding;
import com.example.duan1.model.RoomType;
import com.example.duan1.model.UtilityDetail;
import com.example.duan1.model.UtilityStatus;

public class CreateRoomTypeActivity extends AppCompatActivity {

    ActivityCreateRoomTypeBinding binding;
    DbMotel db;
    RoomType roomType;
    UtilityStatus utilityStatus = new UtilityStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateRoomTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = DbMotel.getInstance(this);

        binding.imgAlarm.setOnClickListener(v -> hanldelUtilityClick(1));
        binding.imgAirConditioner.setOnClickListener(v -> hanldelUtilityClick(2));
        binding.imgMotorcycle.setOnClickListener(v -> hanldelUtilityClick(3));
        binding.imgMoneyBag.setOnClickListener(v -> hanldelUtilityClick(4));
        binding.imgCCTV.setOnClickListener(v -> hanldelUtilityClick(5));
        binding.imgFan.setOnClickListener(v -> hanldelUtilityClick(6));
        binding.imgCooking.setOnClickListener(v -> hanldelUtilityClick(7));
        binding.imgTelevision.setOnClickListener(v -> hanldelUtilityClick(8));
        binding.imgBed.setOnClickListener(v ->hanldelUtilityClick(9));
        binding.imgFidge.setOnClickListener(v -> hanldelUtilityClick(10));
        binding.imgWashingMachine.setOnClickListener(v ->  hanldelUtilityClick(11));
        binding.imgWifi.setOnClickListener(v -> hanldelUtilityClick(12));

        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnSave.setOnClickListener(v -> {

            String nameTypeRoom = binding.edten.getText().toString();
            String numberElectronic = binding.eddien.getText().toString();
            String numberWater = binding.ednuoc.getText().toString();
            String rentCost = binding.edgiaphong.getText().toString();
            String area = binding.edArea.getText().toString();

            if (BaseCheckValid.checkEmptyString(nameTypeRoom,numberElectronic,numberWater,rentCost,area)){
                try {
                    RoomType roomType = new RoomType(nameTypeRoom,Integer.parseInt(rentCost),Integer.parseInt(area),Integer.parseInt(numberElectronic),Integer.parseInt(numberWater));
                    db.roomTypeDao().insert(roomType);
                }catch (Exception e){
                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    return;
                }
                roomType = db.roomTypeDao().getRoomTypeNewest();
                handleInsertUtility(roomType);
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleInsertUtility(RoomType roomType) {

        try {

            if(utilityStatus.deposit){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),1));
            }
            if(utilityStatus.comfortableTime){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),2));
            }
            if(utilityStatus.cctv){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),3));
            }
            if(utilityStatus.cooking){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),4));
            }
            if(utilityStatus.fan){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),5));
            }
            if(utilityStatus.bed){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),6));
            }
            if(utilityStatus.fridge){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),7));
            }
            if(utilityStatus.television){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),8));
            }
            if(utilityStatus.wifi){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),9));
            }
            if(utilityStatus.parking){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),10));
            }
            if(utilityStatus.washingMachine){
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(),11));
            }
            if(utilityStatus.airConditioning) {
                db.utilityDetailDao().insert(new UtilityDetail(roomType.getIdRoomType(), 12));
            }
        } catch (Exception e){
            Toast.makeText(this, "Loi insert utility", Toast.LENGTH_SHORT).show();
        }
    }


    private void hanldelUtilityClick(int i) {
        switch (i){
            case 1:
                if(utilityStatus.comfortableTime){
                    binding.imgAlarm.setImageResource(R.drawable.alarm);
                    utilityStatus.comfortableTime = false;
                }else {
                    binding.imgAlarm.setImageResource(R.drawable.alarm2);
                    utilityStatus.comfortableTime = true;
                }
                break;
            case 2:
                if (utilityStatus.airConditioning){
                    binding.imgAirConditioner.setImageResource(R.drawable.air_conditioner);
                    utilityStatus.airConditioning = false;
                }else {
                    binding.imgAirConditioner.setImageResource(R.drawable.air_conditioner2);
                    utilityStatus.airConditioning = true;
                }
                break;
            case 3:
                if (utilityStatus.parking){
                    binding.imgMotorcycle.setImageResource(R.drawable.motorcycle);
                    utilityStatus.parking = false;
                }else {
                    binding.imgMotorcycle.setImageResource(R.drawable.motorcycle2);
                    utilityStatus.parking = true;
                }
                break;
            case 4:
                if (utilityStatus.deposit){
                    binding.imgMoneyBag.setImageResource(R.drawable.money_bag);
                    utilityStatus.deposit = false;
                }else {
                    binding.imgMoneyBag.setImageResource(R.drawable.money_bag2);
                    utilityStatus.deposit = true;
                }
                break;
            case 5:
                if (utilityStatus.cctv){
                    binding.imgCCTV.setImageResource(R.drawable.cctv_camera);
                    utilityStatus.cctv = false;
                }else {
                    binding.imgCCTV.setImageResource(R.drawable.cctv_camera2);
                    utilityStatus.cctv = true;
                }
                break;
            case 6:
                if (utilityStatus.fan){
                    binding.imgFan.setImageResource(R.drawable.fan);
                    utilityStatus.fan = false;
                }else {
                    binding.imgFan.setImageResource(R.drawable.fan2);
                    utilityStatus.fan = true;
                }

                break;
            case 7:
                if (utilityStatus.cooking) {
                    binding.imgCooking.setImageResource(R.drawable.cooking);
                    utilityStatus.cooking = false;
                } else {
                    binding.imgCooking.setImageResource(R.drawable.cooking2);
                    utilityStatus.cooking = true;
                }
                break;
            case 8:
                if (utilityStatus.television) {
                    binding.imgTelevision.setImageResource(R.drawable.television);
                    utilityStatus.television = false;
                } else {
                    binding.imgTelevision.setImageResource(R.drawable.television2);
                    utilityStatus.television = true;
                }
                break;
            case 9:
                if (utilityStatus.bed) {
                    binding.imgBed.setImageResource(R.drawable.bed);
                    utilityStatus.bed = false;
                } else {
                    binding.imgBed.setImageResource(R.drawable.bed2);
                    utilityStatus.bed = true;
                }
                break;
            case 10:
                if (utilityStatus.fridge) {
                    binding.imgFidge.setImageResource(R.drawable.fridge);
                    utilityStatus.fridge = false;
                } else {
                    binding.imgFidge.setImageResource(R.drawable.fridge2);
                    utilityStatus.fridge = true;
                }
                break;
            case 11:
                if (utilityStatus.washingMachine) {
                    binding.imgWashingMachine.setImageResource(R.drawable.washing_machine);
                    utilityStatus.washingMachine = false;
                } else {
                    binding.imgWashingMachine.setImageResource(R.drawable.washing_machine2);
                    utilityStatus.washingMachine = true;
                }
                break;
            case 12:
                if (utilityStatus.wifi) {
                    binding.imgWifi.setImageResource(R.drawable.wifi);
                    utilityStatus.wifi = false;
                } else {
                    binding.imgWifi.setImageResource(R.drawable.wifi2);
                    utilityStatus.wifi = true;
                }
                break;
        }
    }
}