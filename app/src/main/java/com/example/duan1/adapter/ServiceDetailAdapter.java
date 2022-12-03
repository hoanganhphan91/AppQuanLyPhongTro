package com.example.duan1.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.Interface.IClickItemServiceDetail;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ItemServiceDetailBinding;
import com.example.duan1.model.Service;
import com.example.duan1.model.ServiceDetail;

import java.util.List;

public class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.MyViewHolder> {
    Context context;
    List<ServiceDetail> list;
    DbMotel db;
    IClickItemServiceDetail iClickItemServiceDetail;

    public ServiceDetailAdapter(Context context, List<ServiceDetail> list, IClickItemServiceDetail listener) {
        this.context = context;
        this.list = list;
        this.db = DbMotel.getInstance(context);
        this.iClickItemServiceDetail = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemServiceDetailBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ServiceDetail detail = list.get(position);
        Service service = db.serviceDao().getServiceById(detail.getIdService());
        holder.binding.setServiceDetail(detail);
        holder.binding.setService(service);
        holder.binding.img.setImageURI(Uri.parse(service.getImage()));
        holder.binding.imgDelete.setOnClickListener(view -> iClickItemServiceDetail.onClickDelete(detail,position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ItemServiceDetailBinding binding;
        public MyViewHolder(@NonNull ItemServiceDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
