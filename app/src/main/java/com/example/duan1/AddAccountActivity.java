package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.duan1.adapter.AccountAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.model.Account;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;


public class AddAccountActivity extends AppCompatActivity {
    EditText edtUser,edtPass,edtName,edtPhone;
    Button btnXN,btnExit;
    String strimage;
    public RequestManager requestManager;

    List<Account> accountList;
    AccountAdapter accountAdapter;
    Account account;
    DbMotel db;
    ImageView btnBack;
    ImageView editImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestManager = Glide.with(this);
        setContentView(R.layout.activity_add_account);
        btnBack = findViewById(R.id.imgBack);
        btnExit = findViewById(R.id.btnExit);
        btnXN = findViewById(R.id.btnXn);
        edtUser = findViewById(R.id.edtuser);
        edtPass = findViewById(R.id.edtPass);
        editImage = findViewById(R.id.editImage);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAccountActivity.this, AccountManagerActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(v -> {
            finish();
        });
        accountList = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, accountList);
        btnXN.setOnClickListener(v -> {
            addAccount();

        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
    }

    private void addImage() {
        TedImagePicker.with(AddAccountActivity.this).start(uri -> {
            requestManager.load(uri).into(editImage);
            Log.i("TAG", "Uri: " + uri.toString());

            strimage = uri.toString();
    });
    }
    private void addAccount() {
        if (valiDate() > 0){
            db = DbMotel.getInstance(this);
            String User = edtUser.getText().toString().trim();
            String Pass = edtPass.getText().toString().trim();
            String Phone = edtPhone.getText().toString().trim();
            String Name = edtName.getText().toString().trim();
            String Image = strimage;
            try {
                db.accountDao().insert(new Account(User,Pass,Name,Phone,"manager",Image));
                DbMotel.getInstance(this).accountDao().getAll();

            }catch (Exception e){
                Toast.makeText(this, "False"+e, Toast.LENGTH_SHORT).show();
            }
            finish();
        }


    }
    private int valiDate() {
        int check = 1;
        if (edtUser.getText().length() == 0 || edtPass.getText().length() == 0 || edtPhone.getText().length() == 0||edtName.getText().length()==0) {
            Toast.makeText(AddAccountActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
            check = -1;

        }
        return check;
    }
}