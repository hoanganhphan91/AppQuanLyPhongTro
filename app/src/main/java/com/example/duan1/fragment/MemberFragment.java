package com.example.duan1.fragment;

import static com.example.duan1.base.BaseCheckValid.checkEmptyString;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1.Interface.IClickItemMember;
import com.example.duan1.R;
import com.example.duan1.adapter.MemberAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.DialogMemberBinding;
import com.example.duan1.databinding.DialogMemberDetailBinding;
import com.example.duan1.databinding.FragmentMemberBinding;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Member;
import com.example.duan1.viewmodel.ContractViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;


public class MemberFragment extends Fragment {
    FragmentMemberBinding binding;
    Contract contract;
    DbMotel db;
    List<Member> listMember;
    MemberAdapter adapterMember;
    String pathImage;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentMemberBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Contract viewmodel
       new ViewModelProvider(getActivity()).get(ContractViewModel.class).getContract().observe(getViewLifecycleOwner(),o -> {
            this.contract = (Contract) o;
            handleObserve();
        });
        this.db = DbMotel.getInstance(getContext());
        binding.rvMember.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void handleObserve() {
        listMember = db.memberDao().getMemberByIdContract(contract.getIdContract());
        adapterMember = new MemberAdapter(getContext(), listMember, new IClickItemMember() {
            @Override
            public void onClickDelete(Member member, int position) {
                handleItemMemberDelete(member, position);
            }

            @Override
            public void onClickEdit(Member member, int position) {
                handleItemMemberEdit(member, position);
            }

            @Override
            public void onClickItem(Member member, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
                DialogMemberDetailBinding bindingDialog = DialogMemberDetailBinding.inflate(getLayoutInflater());
                builder.setView(bindingDialog.getRoot());
                AlertDialog dialog = builder.create();
                bindingDialog.setMember(member);
                bindingDialog.imgAvatar.setImageURI(Uri.parse(member.getImage()));
                bindingDialog.btnClose.setOnClickListener(view -> dialog.dismiss());
                dialog.show();
            }
        });
        binding.rvMember.setAdapter(adapterMember);
        binding.imgAdd.setOnClickListener(view1 -> handleAddMember());
    }
    private void handleItemMemberDelete(Member member, int i) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa thành viên?");
        builder.setPositiveButton("Xác nhận", (dialogInterface, i1) -> {
            try {
                listMember.remove(i);
                db.memberDao().delete(member);
                adapterMember.notifyItemRemoved(i);
                adapterMember.notifyItemRangeRemoved(i, adapterMember.getItemCount());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Lỗi xóa thành viên", Toast.LENGTH_SHORT).show();
            }
            dialogInterface.dismiss();
        });

        builder.setNegativeButton("Hủy", (dialogInterface, i1) -> {
            dialogInterface.dismiss();
        });

        builder.create().show();
    }

    private void handleItemMemberEdit(Member member, int postion) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        DialogMemberBinding bindingMember = DialogMemberBinding.inflate(getLayoutInflater());
        bindingMember.btnAdd.setText("Cập nhật");
        builder.setView(bindingMember.getRoot());
        bindingMember.edName.setText(member.getName());
        bindingMember.edBirthday.setText(member.getBirthday());
        Glide.with(getContext()).load(Uri.parse(member.getImage())).into(bindingMember.imgMember);
        bindingMember.imgMember.setMaxHeight(150);
        bindingMember.edHometown.setText(member.getHometown());
        bindingMember.edPhone.setText(member.getPhone());
        bindingMember.edCitizenIdentification.setText(member.getCitizenIdentification());
        AlertDialog dialog = builder.create();
        dialog.show();

        bindingMember.edBirthday.setOnClickListener(view -> {
            DatePickerDialog dp = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
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
                TedImagePicker.with(getContext()).start(uri -> {
                    Glide.with(this).load(uri).into(bindingMember.imgMember);
                    bindingMember.imgMember.setMaxHeight(200);
                    pathImage = uri.toString();
                }));

        bindingMember.btnAdd.setOnClickListener(view -> {
            String name = bindingMember.edName.getText().toString();
            String birthday = bindingMember.edBirthday.getText().toString();
            String citizenIdentification = bindingMember.edCitizenIdentification.getText().toString();
            String phone = bindingMember.edPhone.getText().toString();
            String hometown = bindingMember.edHometown.getText().toString();
            if (checkEmptyString(name, birthday, citizenIdentification, phone, hometown)) {
                try {
                    member.setName(name);
                    member.setBirthday(birthday);
                    member.setCitizenIdentification(citizenIdentification);
                    member.setPhone(phone);
                    member.setHometown(hometown);
                    member.setImage(pathImage);
                    db.memberDao().update(member);
                    Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    listMember.set(postion,member);
                    adapterMember.notifyItemChanged(postion);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Lỗi insert dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
            }
        });
        bindingMember.btnCancel.setOnClickListener(view -> dialog.dismiss());
    }

    private void handleAddMember() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        DialogMemberBinding bindingMember = DialogMemberBinding.inflate(getLayoutInflater());
        builder.setView(bindingMember.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();
        bindingMember.edBirthday.setOnClickListener(view1 ->{
            DatePickerDialog dp = new DatePickerDialog(getContext(),(datePicker, i, i1, i2) -> {
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
                TedImagePicker.with(getContext()).start(uri -> {
                    Glide.with(this).load(uri).into(bindingMember.imgMember);
                    bindingMember.imgMember.setMaxHeight(200);
                    pathImage = uri.toString();
                }));

        bindingMember.btnAdd.setOnClickListener(view1 ->{
            String name = bindingMember.edName.getText().toString();
            String birthday = bindingMember.edBirthday.getText().toString();
            String citizenIdentification = bindingMember.edCitizenIdentification.getText().toString();
            String phone = bindingMember.edPhone.getText().toString();
            String hometown = bindingMember.edHometown.getText().toString();
            if(!checkEmptyString(name, birthday, citizenIdentification, phone,hometown)){
                Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                return;

            }
            try {
                Member member = new Member(name,birthday,citizenIdentification,pathImage,phone,hometown,contract.getIdContract());
                db.memberDao().insert(member);
                Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                listMember.clear();
                listMember.addAll(db.memberDao().getMemberByIdContract(contract.getIdContract()));
                adapterMember.notifyDataSetChanged();
                dialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getContext(), "Lỗi insert dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
        bindingMember.btnCancel.setOnClickListener(view1 -> dialog.dismiss());
    }
}