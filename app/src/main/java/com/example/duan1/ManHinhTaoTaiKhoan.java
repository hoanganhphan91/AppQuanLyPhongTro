package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duan1.dao.AccountDao;
import com.example.duan1.model.Account;

import java.util.ArrayList;

public class ManHinhTaoTaiKhoan extends AppCompatActivity {

    EditText edntk,edpass,edsdt,edname;
    Button btndk;
    String taikhoan, ten,matkhau,sdt;
    ArrayList<Account>list;
//    AccountDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_tao_tai_khoan);
        edntk= findViewById(R.id.dktk);
        edpass= findViewById(R.id.dkmk);
        edname=findViewById(R.id.dkname);
        edsdt =findViewById(R.id.dksdt);
        btndk =findViewById(R.id.btn_dkk);

        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account= new Account();
                account.setUsername(edntk.getText().toString());
                account.setName(edname.getText().toString());
                account.setPassword(edpass.getText().toString());
                account.setPhone(edsdt.getText().toString());
//                long kq=dao.insert(account);
//                ten = edname.getText().toString().trim();
//                matkhau = edpass.getText().toString().trim();
//                sdt = edsdt.getText().toString().trim();
//                taikhoan=edntk.getText().toString().trim();
//                if (ten.equals("")||matkhau.equals("")||sdt.equals("")||taikhoan.equals("")){
//                    Toast.makeText(ManHinhTaoTaiKhoan.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
//                }else {
//                    if (kq>0){
//                        list.clear();
//                        list.addAll(dao.getAll());
//                        Toast.makeText(ManHinhTaoTaiKhoan.this, "đăng ký thành công", Toast.LENGTH_SHORT).show();
//
//                    }
//                    else        Toast.makeText(ManHinhTaoTaiKhoan.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
//
//                }


            }
        });



    }
}