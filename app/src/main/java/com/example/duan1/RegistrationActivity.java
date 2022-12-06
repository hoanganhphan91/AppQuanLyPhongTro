package com.example.duan1;

import static com.example.duan1.base.BaseCheckValid.checkEmptyString;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.example.duan1.database.DbMotel;
import com.example.duan1.model.Account;
import com.example.duan1.model.SessionAccount;

import java.util.ArrayList;

//import gun0912.tedbottompicker.TedBottomPicker;
//import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import gun0912.tedimagepicker.builder.TedImagePicker;

public class RegistrationActivity extends AppCompatActivity {


    EditText edntk, edpass, edsdt, edname;
    Button btndk;
    ImageView imganh;
    String taikhoan, ten, matkhau, sdt;
    ArrayList<Account> list=new ArrayList<>();
    String pathImage="";
    DbMotel db;
    RequestManager requestManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        db = DbMotel.getInstance(this);
        edntk = findViewById(R.id.dktk);
        edpass = findViewById(R.id.dkmk);
        edname = findViewById(R.id.dkname);
        edsdt = findViewById(R.id.dksdt);
        btndk = findViewById(R.id.btn_dkk);
        requestManager = Glide.with(this);
        imganh = findViewById(R.id.img_anh);

        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account  account = new Account();
                account.setUsername(edntk.getText().toString());
                account.setName(edname.getText().toString());
                account.setPassword(edpass.getText().toString());
                account.setPhone(edsdt.getText().toString());

                ten = edname.getText().toString().trim();
                matkhau = edpass.getText().toString().trim();
                sdt = edsdt.getText().toString().trim();
                taikhoan = edntk.getText().toString().trim();
                if (!checkEmptyString(ten,matkhau,sdt,taikhoan,pathImage)) {
                    Toast.makeText(RegistrationActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        db.accountDao().insert(account);
                        Toast.makeText(RegistrationActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        SessionAccount sessionManage = new SessionAccount(RegistrationActivity.this);
                        sessionManage.saveAccount(account);
                        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class) );
                    }catch (Exception e){
                        Toast.makeText(RegistrationActivity.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        imganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedImagePicker.with(RegistrationActivity.this).start(uri -> {
                    requestManager.load(uri).into(imganh);
                    pathImage = uri.toString();

                });
            }
        });

    }

///


//    private void openImagePicker(){
//        TedBottomSheetDialogFragment.OnImageSelectedListener listener = new TedBottomSheetDialogFragment.OnImageSelectedListener() {
//            @Override
//            public void onImageSelected(Uri uri) {
//                try {
//                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                    imganh.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        TedBottomSheetDialogFragment tedBottomPicker = new TedBottomSheetDialogFragment.Builder(RegistrationActivity.this)
//                .setOnImageSelectedListener(listener).create();
//        tedBottomPicker.show(getSupportFragmentManager());

//        TedBottomPicker tedBottom = new TedBottomPicker.Builder(RegistrationActivity.this)
//                .setOnImageSelectedListener(listener).create();
//            tedBottom.show(getSupportFragmentManager());
//    }
}



