package com.example.duan1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Interface.IClickItemContract;
import com.example.duan1.R;
import com.example.duan1.ShowMemberContract;
import com.example.duan1.dao.ContractDao;
import com.example.duan1.model.Contract;

import java.util.ArrayList;
import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHoler> {
    private List<Contract> list;
    private Context context;
    private ContractDao contractDao;
    private IClickItemContract iClickItemContract;

    public ContractAdapter(List<Contract> list, Context context, IClickItemContract iClickItemContract) {
        this.list = list;
        this.context = context;
        this.iClickItemContract = iClickItemContract;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_show_contract,parent,false);

        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, @SuppressLint("RecyclerView") int position) {
        holder.txt_id.setText("id: " + list.get(position).getIdContract());
        holder.txt_ngaythue.setText("Ngày thuê: " + list.get(position).getStatingDate());
        holder.txt_ngaytra.setText("Ngày trả: " + list.get(position).getEndingDate());
        String trangthai = "";

        Contract contract = list.get(position);


        if(list.get(position).getStatus() == 1){
            trangthai = "Còn hạn";
        } else {
            trangthai = "Hết hạn";
        }
        holder.txt_han.setText("Hợp đồng: " + trangthai);
        holder.txt_phong.setText("Phòng: " + list.get(position).getRoomCode());
        holder.ic_delete.setOnClickListener(view -> iClickItemContract.onClickDelete(contract,position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trangthai = "";
                if(list.get(position).getStatus() == 1){
                    trangthai = "Còn hạn";
                } else {
                    trangthai = "Hết hạn";
                }
                Intent sendDataToShowMember = new Intent(holder.itemView.getContext(), ShowMemberContract.class);
                sendDataToShowMember.putExtra("id",list.get(position).getIdContract());
                sendDataToShowMember.putExtra("statingDate",list.get(position).getStatingDate());
                sendDataToShowMember.putExtra("endingDate",list.get(position).getEndingDate());
                sendDataToShowMember.putExtra("status",trangthai);
                sendDataToShowMember.putExtra("roomCode",list.get(position).getRoomCode());

                holder.itemView.getContext().startActivity(sendDataToShowMember);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void getFillList(ArrayList<Contract> listCont) {
        this.list = listCont;
        notifyDataSetChanged();
    }

    public  class ViewHoler extends RecyclerView.ViewHolder{
        TextView txt_id, txt_ngaythue, txt_ngaytra, txt_han, txt_phong;
        ImageView ic_delete;
        IClickItemContract contract;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);

            txt_id = itemView.findViewById(R.id.id_phong);
            txt_ngaythue = itemView.findViewById(R.id.id_clock_start);
            txt_ngaytra = itemView.findViewById(R.id.id_clock_stop);
            txt_han = itemView.findViewById(R.id.id_contract);
            txt_phong = itemView.findViewById(R.id.id_room);
            ic_delete =itemView.findViewById(R.id.ic_delete);
        }
    }
}
