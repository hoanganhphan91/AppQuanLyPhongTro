package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.opengl.GLDebugHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.database.DbMotel;
import com.example.duan1.database.DbMotel_Impl;
import com.example.duan1.model.Account;
import com.example.duan1.model.Users;

import java.util.List;

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
//        glDebugHelper =  new GLDebugHelper(this);
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

//        mUsers= (Users) getIntent().getExtras().get("Object password");
//        if(mUsers!=null){
//            pass.setText(mUsers.getPassword());
//        }
//
//        confom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                confom();
//            }
//        });
//    }
//
//    private void confom() {
//
//        String password = pass.getText().toString();
//        if (TextUtils.isEmpty( password)) {
//            return;
//        }
//
//        mUsers.setPassword(password);
//        DbMotel.getInstance(this).accountDao().updatepassword(mUsers);
//        Toast.makeText(this,"Up date password thành công",Toast.LENGTH_LONG).show();
//        Intent intent = new Intent();
//        setResult(Activity.RESULT_OK,intent);
//        finish();



//    public  boolean uercheck(Account account){
//        List<Account> list=db.getInstance(this).accountDao().checkTk(account.getUsername());
//        return  list!=null && !list.isEmpty();
//    }



//
//        Intent intent = new Intent(this,RegistrationActivity.class);
//                   startActivity(intent);
    }
}