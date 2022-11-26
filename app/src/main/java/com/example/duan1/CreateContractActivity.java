package com.example.duan1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1.Interface.iClickItemMember;
import com.example.duan1.adapter.MemberContractAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityCreateContractBinding;
import com.example.duan1.databinding.DialogMemberBinding;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Member;
import com.example.duan1.model.Room;
import com.example.duan1.model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class CreateContractActivity extends AppCompatActivity {
    ActivityCreateContractBinding binding;
    DbMotel db;
    MemberContractAdapter adapterMember;
    List<Member> listMember = new ArrayList<>();
    Contract contract;
    String pathImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_contract);
        db = DbMotel.getInstance(this);
        Intent intent = getIntent();
        Room room = (Room) intent.getSerializableExtra("room");
        RoomType roomType = db.roomTypeDao().getRoomTypeById(room.getIdRoomType());
        binding.setRoom(room);
        binding.setRoomType(roomType);
        //create Contract temp
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = format.format(date);
        db.contractDao().insert(new Contract(startDate,"",1,room.getRoomCode()));
        //get Contract create
        contract = db.contractDao().getContractNewest();
        adapterMember = new MemberContractAdapter(this, listMember, new iClickItemMember() {
            @Override
            public void onClickDelete(Member member, int i) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateContractActivity.this);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa thành viên?");
                builder.setPositiveButton("Xác nhận", (dialogInterface, i1) -> {
                    try {
                        listMember.remove(i);
                        db.memberDao().delete(member);
                        adapterMember.notifyItemRemoved(i);
                        adapterMember.notifyItemRangeRemoved(i, adapterMember.getItemCount());
                    } catch (Exception e) {
                        Toast.makeText(CreateContractActivity.this, "Lỗi xóa thành viên", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();
                });

                builder.setNegativeButton("Hủy", (dialogInterface, i1) -> {
                    dialogInterface.dismiss();
                });

                builder.create().show();
            }

            @Override
            public void onClickEdit(Member member, int postion) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateContractActivity.this);
                DialogMemberBinding bindingMember = DialogMemberBinding.inflate(getLayoutInflater());
                bindingMember.btnAdd.setText("Cập nhật");
                builder.setView(bindingMember.getRoot());
                bindingMember.edName.setText(member.getName());
                bindingMember.edBirthday.setText(member.getBirthday());
                Glide.with(CreateContractActivity.this).load(Uri.parse(member.getImage())).into(bindingMember.imgMember);
                bindingMember.imgMember.setMaxHeight(150);
                bindingMember.edHometown.setText(member.getHometown());
                bindingMember.edPhone.setText(member.getPhone());
                AlertDialog dialog = builder.create();
                dialog.show();

                bindingMember.edBirthday.setOnClickListener(view -> {
                    DatePickerDialog dp = new DatePickerDialog(CreateContractActivity.this, (datePicker, i, i1, i2) -> {
                        try {
                            String date2 = i + "-" + (i1 + 1) + "-" + i2;
                            Date date1 = format.parse(date2);
                            bindingMember.edBirthday.setText(format.format(date1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, 1999, 1, 1);
                    dp.show();
                });

                bindingMember.imgMember.setOnClickListener(view ->
                        TedImagePicker.with(CreateContractActivity.this).start(uri -> {
                            Glide.with(CreateContractActivity.this).load(uri).into(bindingMember.imgMember);
                            bindingMember.imgMember.setMaxHeight(150);
                            pathImage = uri.toString();
                        }));

                bindingMember.btnAdd.setOnClickListener(view -> {
                    String name = bindingMember.edName.getText().toString();
                    String birthday = bindingMember.edBirthday.getText().toString();
                    String citizenIdentification = bindingMember.edCitizenIdentification.getText().toString();
                    String phone = bindingMember.edPhone.getText().toString();
                    String hometown = bindingMember.edHometown.getText().toString();
                    boolean check = checkValidate(name, birthday, citizenIdentification, phone, hometown);
                    if (check) {
                        try {
                            member.setName(name);
                            member.setBirthday(birthday);
                            member.setCitizenIdentification(citizenIdentification);
                            member.setPhone(phone);
                            member.setHometown(hometown);
                            db.memberDao().update(member);
                            Toast.makeText(CreateContractActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            listMember.set(postion,member);
                            adapterMember.notifyItemChanged(postion);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(CreateContractActivity.this, "Lỗi insert dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bindingMember.btnCancel.setOnClickListener(view -> dialog.dismiss());
            }
        });

        binding.rvMember.setAdapter(adapterMember);
        binding.rvMember.setLayoutManager(new LinearLayoutManager(this));
        //add member
        binding.imgAdd.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            DialogMemberBinding bindingMember = DialogMemberBinding.inflate(getLayoutInflater());
            builder.setView(bindingMember.getRoot());
            AlertDialog dialog = builder.create();
            dialog.show();
            bindingMember.edBirthday.setOnClickListener(view1 ->{
                DatePickerDialog dp = new DatePickerDialog(this,(datePicker, i, i1, i2) -> {
                    try {
                        String date2 = i + "-" + (i1+1) + "-" +i2;
                        Date date1 = format.parse(date2);
                        bindingMember.edBirthday.setText(format.format(date1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                },1999,1,1);
                dp.show();
            });
            bindingMember.imgMember.setOnClickListener(view1 ->
                TedImagePicker.with(this).start(uri -> {
                    Glide.with(this).load(uri).into(bindingMember.imgMember);
                    bindingMember.imgMember.setMaxHeight(150);
                    pathImage = uri.toString();
                }));

            bindingMember.btnAdd.setOnClickListener(view1 ->{
                String name = bindingMember.edName.getText().toString();
                String birthday = bindingMember.edBirthday.getText().toString();
                String citizenIdentification = bindingMember.edCitizenIdentification.getText().toString();
                String phone = bindingMember.edPhone.getText().toString();
                String hometown = bindingMember.edHometown.getText().toString();
                boolean check = checkValidate(name, birthday, citizenIdentification, phone,hometown);
                if(check){
                    try {
                        Member member = new Member(name,birthday,citizenIdentification,pathImage,phone,hometown,contract.getIdContract());
                        db.memberDao().insert(member);
                        Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        listMember.clear();
                        listMember.addAll(db.memberDao().getMemberByIdContract(contract.getIdContract()));
                        adapterMember.notifyDataSetChanged();
                        dialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi insert dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            bindingMember.btnCancel.setOnClickListener(view1 -> dialog.dismiss());
        });

        binding.btnCancel.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.btnCreateContract.setOnClickListener(view -> {
            startActivity(new Intent(this,RoomManageActivity.class));
        });
    }
    @Override
    public void onBackPressed() {
        try {
            db.contractDao().delete(contract);
        }catch (Exception e){
            Toast.makeText(this, "Lỗi hủy bỏ hợp đồng", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    public boolean checkValidate(String name, String birthday, String citizenIdentification, String phone, String hometown) {
        if(name.isEmpty() || birthday.isEmpty() || citizenIdentification.isEmpty() || phone.isEmpty() || hometown.isEmpty() || pathImage.isEmpty()){
            Toast.makeText(this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}