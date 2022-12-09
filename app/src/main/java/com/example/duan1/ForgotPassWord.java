package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.database.DbMotel;
import com.example.duan1.model.Users;

public class ForgotPassWord extends AppCompatActivity {

    TextView username;
    EditText pass, repass;
    Button confom;
    private Users mUsers;
    DbMotel db;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_word);
        username = findViewById(R.id.password_reset_text);
        pass = findViewById(R.id.password_reset);
        repass = findViewById(R.id.respassword);
        confom = findViewById(R.id.btn_confom);
        db = DbMotel.getInstance(this);
        Intent intent = getIntent();

        confom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pass.getText().toString();
                String respassword = repass.getText().toString();
                if (password.equals(respassword)) {

                Boolean checkpass = db.updatepassword(password);
                if (checkpass = true) {
                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    Toast.makeText(ForgotPassWord.this, "Password update", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPassWord.this, "Password not update", Toast.LENGTH_SHORT).show();
                }
            }else{
                    Toast.makeText(ForgotPassWord.this, "Password not Matching", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}