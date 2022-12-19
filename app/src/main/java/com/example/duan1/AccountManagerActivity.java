package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.duan1.adapter.AccountAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityAccountManagerBinding;
import com.example.duan1.model.Account;

import java.util.List;


public class AccountManagerActivity extends AppCompatActivity {
    ActivityAccountManagerBinding binding;
    List<Account> accountList;
    AccountAdapter accountAdapter;
    DbMotel dbMotel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        binding = ActivityAccountManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imgBack.setOnClickListener(view -> finish());
        dbMotel = DbMotel.getInstance(this);
        accountList = dbMotel.accountDao().getAll();
        loaddata();
        binding.floatAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountManagerActivity.this, AddAccountActivity.class);
                startActivity(intent);

            }
        });
        binding.rvAccount.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.floatAddAccount.hide();
                } else {
                    binding.floatAddAccount.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void loaddata() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvAccount.setLayoutManager(linearLayoutManager);
        accountAdapter = new AccountAdapter(this, accountList);
        binding.rvAccount.setAdapter(accountAdapter);
        accountAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        accountList.clear();
        accountList.addAll(dbMotel.accountDao().getAll());
        accountAdapter.notifyDataSetChanged();
    }
}