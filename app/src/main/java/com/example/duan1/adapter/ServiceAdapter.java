package com.example.duan1.adapter;

import static com.example.duan1.base.BaseCheckValid.checkEmptyString;

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
import com.example.duan1.databinding.ItemServiceBinding;
import com.example.duan1.model.Service;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {
    Context context;
    List<Service> list;
    ImageView imageViewEdit;
    String strimage="";
    DbMotel dbMotel;
    public ServiceAdapter(Context context, List<Service> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceAdapter.MyViewHolder(ItemServiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Service  service = list.get(position);
        dbMotel = DbMotel.getInstance(context);
        try{
            holder.binding.imageService.setImageURI(Uri.parse(service.getImage().trim()));
        }catch (Exception e){
            Toast.makeText(context, "False", Toast.LENGTH_SHORT).show();
        }
        holder.binding.tvService.setText(service.getName());
        holder.binding.priceService.setText(String.valueOf(service.getPrice())+" VND");
        Glide.with(context).load(Uri.parse(service.getImage())).into(holder.binding.imageService);
        holder.binding.imvDelete.setOnClickListener(v -> {
            try {
                dbMotel.serviceDao().delete(service);
                list.remove(position);
                this.notifyItemRemoved(position);
                this.notifyItemRangeRemoved(position,getItemCount());
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        holder.binding.imgEdit.setOnClickListener(v -> {
            this.notifyItemChanged(position);
            showDialogEdit(service,position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ItemServiceBinding binding;


        public MyViewHolder(ItemServiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void showDialogEdit(Service service, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_edit_service,null);
        builder.setView(view);
        EditText edtEditDV = view.findViewById(R.id.edtEditDv);
        EditText edtPriceDV = view.findViewById(R.id.edtEditPricedv);
        imageViewEdit = view .findViewById(R.id.setImageEdit);
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
        edtEditDV.setText(service.getName());
        edtPriceDV.setText(String.valueOf(service.getPrice()));
        imageViewEdit.setImageURI(Uri.parse(service.getImage()));
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String editDV = edtEditDV.getText().toString().trim();
                int editPrice = Integer.parseInt(edtPriceDV.getText().toString().trim());
                String strImage = strimage;
                service.setName(editDV);
                service.setPrice(editPrice);
                if(checkEmptyString(strImage)){
                    service.setImage(strImage);
                }
                dbMotel.serviceDao().update(service);
                list.set(position,service);

                loadData();
            }
        });
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }
    public void addImage(){
                TedImagePicker.with(context).start(uri -> {
                    strimage = uri.toString();
                    Glide.with(context).load(uri).into(imageViewEdit);
                });
            }

    private void loadData(){
        notifyDataSetChanged();
    }
}

