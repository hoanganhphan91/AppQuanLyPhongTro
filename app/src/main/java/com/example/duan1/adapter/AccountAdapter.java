package com.example.duan1.adapter;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.duan1.R;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ItemAccountBinding;
import com.example.duan1.model.Account;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {
    Context context;
    ImageView editImgAccount;
    String strImage;
    List<Account> list;
    DbMotel dbMotel;


    public AccountAdapter(Context context, List<Account> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountAdapter.MyViewHolder(ItemAccountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Account account = list.get(position);
        dbMotel = DbMotel.getInstance(context);
        try {
            holder.binding.imgAccount.setImageURI(Uri.parse(account.getImage().trim()));
        } catch (Exception e) {
            Toast.makeText(context, "False"+e, Toast.LENGTH_SHORT).show();
            Log.i("TAG", "Uri: " + e.toString());
        }
        holder.binding.tvuser.setText(account.getUsername());
        holder.binding.tvpass.setText(account.getPassword());
        holder.binding.tvname.setText(account.getName());
        holder.binding.tvphone.setText(account.getPhone());
        holder.binding.tvtitle.setText(account.getTitle());
        holder.binding.imvDelete.setOnClickListener(v -> {
            try {
                dbMotel.accountDao().delete(account);
                list.remove(position);
                this.notifyItemRemoved(position);
                this.notifyItemRangeRemoved(position, getItemCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        holder.binding.imgEdit.setOnClickListener(v -> {
            editAccount(account,position);
        });

    }

    private void editAccount(Account account, int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_edit_account, null);
        builder.setView(view);
        EditText edtuser = view.findViewById(R.id.edtuser);
        EditText edtpass = view.findViewById(R.id.edtPass);
        EditText edtname = view.findViewById(R.id.edtName);
        EditText edtphone = view.findViewById(R.id.edtPhone);
        editImgAccount = view.findViewById(R.id.editImgAccount);

        edtuser.setText(account.getUsername());
        edtpass.setText(account.getPassword());
        edtname.setText(account.getName());
        edtphone.setText(account.getPhone());
        editImgAccount.setImageURI(Uri.parse(account.getImage()));
        editImgAccount.setOnClickListener(v -> {
            addImage();
        });

        builder.setNegativeButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String edituser = edtuser.getText().toString().trim();
                String editpass = edtpass.getText().toString().trim();
                String editname = edtname.getText().toString().trim();
                String editphone = edtphone.getText().toString().trim();
                String strImg = strImage;
                account.setImage(strImg);
                account.setUsername(edituser);
                account.setPassword(editpass);
                account.setName(editname);
                account.setPhone(editphone);
                dbMotel.accountDao().update(account);
                list.set(position,account);
                loadData();
            }
        });
        builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAccountBinding binding;

        public MyViewHolder(ItemAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void addImage() {

        RequestManager requestManager = Glide.with(context);
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedImagePicker.with(context).start(uri -> {
                    requestManager.load(uri).into(editImgAccount);
                    Log.i("TAG", "Uri: " + uri.toString());
                    strImage = uri.toString();
                });
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
                .check();
    }
    private void loadData(){


        notifyDataSetChanged();
    }
}
