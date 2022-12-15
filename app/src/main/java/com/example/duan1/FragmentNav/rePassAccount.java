package com.example.duan1.FragmentNav;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duan1.LoginActivity;
import com.example.duan1.R;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.FragmentInfoAccountBinding;
import com.example.duan1.databinding.FragmentRePassAccountBinding;
import com.example.duan1.model.Account;
import com.example.duan1.model.SessionAccount;



public class rePassAccount extends Fragment {
    FragmentRePassAccountBinding binding;
    Account account;
    DbMotel dbMotel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRePassAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionAccount sessionManage = new SessionAccount(getContext());
        account = sessionManage.fetchAccount();
        dbMotel = DbMotel.getInstance(getContext());
        binding.btnXN.setOnClickListener(v -> {
            if (validate()>0){


                if (check() ==true){
                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    binding.edtReNewPass.setText("");
                    binding.edtOldPass.setText("");
                    binding.edtNewPass.setText("");
                }else {
                    Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
            
        });
    }

    public int validate() {
        int check = 1;
        if (binding.edtNewPass.getText().length() == 0 || binding.edtOldPass.getText().length() == 0 || binding.edtReNewPass.getText().length() == 0) {
            Toast.makeText(getContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
            check = -1;
        }else if (!binding.edtOldPass.getText().toString().equals(account.getPassword())){
            Toast.makeText(getContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
        }else {
            if (!binding.edtNewPass.getText().toString().equals(binding.edtReNewPass.getText().toString())) {
                Toast.makeText(getContext(), "Mật khẩu lặp lại không đúng", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }
    public boolean check(){
        account.setPassword(binding.edtNewPass.getText().toString());
        dbMotel.accountDao().update(account);;
        return true;
    }
}