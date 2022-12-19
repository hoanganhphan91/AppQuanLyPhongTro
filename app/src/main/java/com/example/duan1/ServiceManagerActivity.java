package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.duan1.adapter.ServiceAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityServiceManagerBinding;
import com.example.duan1.model.Service;

import java.util.List;

public class ServiceManagerActivity extends AppCompatActivity {
    ActivityServiceManagerBinding binding;

    List<Service> serviceList;
    ServiceAdapter serviceAdapter;

    DbMotel db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_manager);
        binding = ActivityServiceManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = DbMotel.getInstance(this);
        serviceList = db.serviceDao().getAll();
        loaddata();
        binding.imgBack.setOnClickListener(view -> finish());
        binding.floatAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceManagerActivity.this, AddServiceActivity.class);
                startActivity(intent);

            }
        });
        binding.rvService.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.floatAddService.hide();
                } else {
                    binding.floatAddService.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void loaddata() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvService.setLayoutManager(linearLayoutManager);
        serviceAdapter = new ServiceAdapter(this, serviceList);
        binding.rvService.setAdapter(serviceAdapter);
        serviceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceList.clear();
        serviceList.addAll(db.serviceDao().getAll());
        serviceAdapter.notifyDataSetChanged();
    }
}
