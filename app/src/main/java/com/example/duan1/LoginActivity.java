package com.example.duan1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Update;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityLoginBinding;
import com.example.duan1.model.Account;
import com.example.duan1.model.SessionManage;
import com.example.duan1.model.Users;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
//    private static final int MY_REQUEST_CODE = 10;
    ActivityLoginBinding binding;
    DbMotel db ;
    TextView tvquenmatkhau;
    SessionManage sessionManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManage = new SessionManage(this);
        Account account2 = sessionManage.fetchAccount();
        binding.edUserName.setText(account2.getUsername());
        binding.edPassword.setText(account2.getPassword());

        db = DbMotel.getInstance(this);
        tvquenmatkhau = findViewById(R.id.tv_quenmk);
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.edUserName.getText().toString();
            String password = binding.edPassword.getText().toString();
            if(checkValidate(username,password )){
                Account account = db.accountDao().checkLogin(username,password);
                if(account == null){
                    Toast.makeText(this, "Thông tin tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }else {

                    sessionManage.saveAccount(account);
//                    Intent intent = new Intent(this,RegistrationActivity.class);
//                    startActivity(intent);
                }
            }else {
                Toast.makeText(this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
            }
        });


        tvquenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPassWord.class);
                startActivity(intent);


            }
        });

    }

//    private void loaddata(){
//
//
//    }

    private boolean checkValidate(String username, String password) {
        return !(username.isEmpty() || password.isEmpty());
    }

//    private void clickupdatepassword(Users users){
//        Intent intent = new Intent(LoginActivity.this,ForgotPassWord.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("Object password", users);
//        intent.putExtras(bundle);
//        startActivityForResult(intent,MY_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode== MY_REQUEST_CODE && resultCode == Activity.RESULT_OK);
//        loadData();
//    }
}





