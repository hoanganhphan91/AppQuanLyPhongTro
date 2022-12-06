package com.example.duan1.FragmentNav;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duan1.AccountManagerActivity;
import com.example.duan1.ContractManageActivity;
import com.example.duan1.R;
import com.example.duan1.RoomManageActivity;
import com.example.duan1.ServiceManagerActivity;
import com.example.duan1.databinding.FragmentHomeFragmnetBinding;

public class HomeFragmnet extends Fragment {
    FragmentHomeFragmnetBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentHomeFragmnetBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.contractManage.setOnClickListener(v -> startActivity(new Intent(view.getContext(), ContractManageActivity.class)));
        binding.roomManage.setOnClickListener(v -> startActivity(new Intent(view.getContext(), RoomManageActivity.class)));
        binding.seviceManage.setOnClickListener(v -> startActivity(new Intent(view.getContext(), ServiceManagerActivity.class)));
        binding.createAccount.setOnClickListener(v -> startActivity(new Intent(view.getContext(), AccountManagerActivity.class)));
    }
}