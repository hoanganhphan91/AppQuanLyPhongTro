package com.example.duan1.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.Interface.iClickItemMember;
import com.example.duan1.databinding.ItemMemberContractBinding;
import com.example.duan1.model.Member;
import com.example.duan1.R;

import java.util.List;

public class MemberContractAdapter extends RecyclerView.Adapter<MemberContractAdapter.MyViewHolder> {
    Context context;
    List<Member> list;
    iClickItemMember onClickItemMember;
    public MemberContractAdapter(Context context, List<Member> list, iClickItemMember listener) {
        this.context = context;
        this.list = list;
        this.onClickItemMember = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemMemberContractBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       try{
           Member member = list.get(position);
           holder.binding.setMember(member);
           holder.binding.executePendingBindings();
           Glide.with(context).load(Uri.parse(member.getImage())).into(holder.binding.imgAvatar);
           holder.binding.imgDelete.setOnClickListener(view -> onClickItemMember.onClickDelete(member,position));
           holder.binding.imgEdit.setOnClickListener(view -> onClickItemMember.onClickEdit(member,position));
       }catch (Exception e){
           e.printStackTrace();
           Toast.makeText(context, "Lỗi link image member", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ItemMemberContractBinding binding;
        public MyViewHolder( ItemMemberContractBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
