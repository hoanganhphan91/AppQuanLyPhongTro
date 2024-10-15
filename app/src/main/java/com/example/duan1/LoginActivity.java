package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityLoginBinding;
import com.example.duan1.model.Account;
import com.example.duan1.model.SessionAccount;

public class
LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DbMotel db ;
    TextView tvquenmatkhau;
    SessionAccount sessionManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManage = new SessionAccount(this);
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
                    Intent intent = new Intent(this,MenuMainActivity.class);
                    sessionManage.saveAccount(account);
                    startActivity(intent);
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
    private boolean checkValidate(String username, String password) {
        return !(username.isEmpty() || password.isEmpty());
    }
}





