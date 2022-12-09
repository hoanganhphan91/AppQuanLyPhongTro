package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;

import com.example.duan1.Interface.IClickItemServiceDetail;
import com.example.duan1.adapter.ServiceDetailAdapter;
import com.example.duan1.adapter.ServiceRoomAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityServiceDetailBinding;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Service;
import com.example.duan1.model.ServiceDetail;

import java.util.List;

public class ServiceDetailActivity extends AppCompatActivity {
    ActivityServiceDetailBinding binding;
    Invoice invoice;
    DbMotel db;
    List<ServiceDetail> listServiceDetail;
    ServiceDetailAdapter detailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        invoice = (Invoice) getIntent().getSerializableExtra("invoice");
        db = DbMotel.getInstance(this);
        binding.rvService.setLayoutManager( new LinearLayoutManager(this));
        listServiceDetail = db.serviceDetailDao().getServiceDetailByIdInvoidce(invoice.getIdInvoice());
        detailAdapter = new ServiceDetailAdapter(this, listServiceDetail, new IClickItemServiceDetail() {
            @Override
            public void onClickDelete(ServiceDetail serviceDetail, int i) {
                handleItemDelete(serviceDetail,i);
            }
        });
        binding.rvService.setAdapter(detailAdapter);
        handleTotal();
        binding.imgBack.setOnClickListener(view -> finish());
    }

    private void handleItemDelete(ServiceDetail serviceDetail, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomAlertDialog);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setNegativeButton("OK",(dialogInterface, i) -> {
            db.serviceDetailDao().delete(serviceDetail);
            listServiceDetail.remove(position);
            detailAdapter.notifyItemRemoved(position);
            detailAdapter.notifyItemRangeRemoved(position, detailAdapter.getItemCount());
            dialogInterface.dismiss();
            handleTotal();
        });
        builder.setPositiveButton("Hủy",(dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.create().show();
    }
    private void handleTotal() {
        int total = 0;
        for (ServiceDetail o:listServiceDetail) {
            Service service = db.serviceDao().getServiceById(o.getIdService());
            total+=o.getNumber()*service.getPrice();
        }
        binding.tvTotal.setText("Tổng tiền : " + String.format("%,d", total));
    }
}