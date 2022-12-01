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
import com.example.duan1.adapter.ServiceAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.model.Service;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;


public class AddServiceActivity extends AppCompatActivity {
    ImageView imageViewBack;

    EditText edtDv, edtPrice;
    String strimage;
    Button btnXn, btnExit;
    ImageView imageViewAdd;
    private RequestManager requestManager;


    List<Service> serviceList;
    ServiceAdapter serviceAdapter;
    DbMotel db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        requestManager = Glide.with(this);
        imageViewBack = findViewById(R.id.imgBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddServiceActivity.this, ServiceManagerActivity.class);
                startActivity(intent);
            }
        });
        btnXn = findViewById(R.id.btnXn);
        edtDv = findViewById(R.id.edtAddv);
        edtPrice = findViewById(R.id.edtPricedv);
        imageViewAdd = findViewById(R.id.setImage);
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            finish();
        });
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this, serviceList);
        btnXn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();

            }
        });
    }

    public void addService() {


        if (valiDate() > 0) {
            db = DbMotel.getInstance(this);
            String strService = edtDv.getText().toString().trim();
            int strPrice = Integer.parseInt(edtPrice.getText().toString().trim());
            String strImage = strimage;

            try {
                db.serviceDao().insert(new Service(strService, strPrice, strImage));

                DbMotel.getInstance(this).serviceDao().getAll();
            } catch (Exception e) {
                Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
            }
        }
        finish();


    }

    public void addImage() {

        TedImagePicker.with(AddServiceActivity.this).start(uri -> {
            requestManager.load(uri).into(imageViewAdd);


            strimage = uri.toString();

        });
    }


    private int valiDate() {
        int check = 1;
        if (edtDv.getText().length() == 0 || edtPrice.getText().length() == 0) {
            Toast.makeText(AddServiceActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
            check = -1;

        }
        return check;
    }
}