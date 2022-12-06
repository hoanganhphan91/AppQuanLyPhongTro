package com.example.duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Interface.IClickItemInvoice;
import com.example.duan1.databinding.ItemRoomInvoiceBinding;
import com.example.duan1.model.Invoice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder>{
    Context context;
    List<Invoice> invoiceList;
    IClickItemInvoice listener;
    SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    public InvoiceAdapter(Context context, List<Invoice> invoiceList, IClickItemInvoice listener) {
        this.context = context;
        this.invoiceList = invoiceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemRoomInvoiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);
        try {
            Date date = format2.parse(invoice.getDate());
            holder.binding.tvContract.setText("Hóa đơn tháng " + format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.binding.tvElectronic.setText((invoice.getNewPowerIndicator() - invoice.getOldPowerIndicator())+"");
        holder.binding.tvElectronic.setText((invoice.getNewWaterIndex() - invoice.getOldWaterIndex())+"");
        holder.binding.tvTotal.setText(invoice.getTotal()+"");
        if(invoice.getStatus() == 0){
            holder.binding.layoutPayed.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(view -> listener.onClickItem(invoice,position));
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ItemRoomInvoiceBinding binding;
        public MyViewHolder(@NonNull ItemRoomInvoiceBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

    }
}