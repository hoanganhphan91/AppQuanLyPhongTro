package com.example.duan1.fragmentContract;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duan1.Interface.IClickItemContract;
import com.example.duan1.R;
import com.example.duan1.ShowMemberContract;
import com.example.duan1.adapter.Contract2Adapter;
import com.example.duan1.adapter.ContractAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.FragmentEffectBinding;
import com.example.duan1.databinding.FragmentExpireBinding;
import com.example.duan1.model.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpireFragment extends Fragment {
    FragmentExpireBinding binding;
    DbMotel db;
    Contract2Adapter adapter;
    List<Contract> list = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(new Date());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpireBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DbMotel.getInstance(getContext());
        adapter = new Contract2Adapter(list, new IClickItemContract() {
            @Override
            public void onClickItem(Contract contract, int i) {
                Intent intent = new Intent(getActivity(), ShowMemberContract.class);
                intent.putExtra("contract",contract);
                startActivity(intent);
            }
            @Override
            public void onClickExtend(Contract contract, int i) {}

            @Override
            public void onClickCancel(Contract contract, int i) {}
        });
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.setAdapter(adapter);
        loadRecycleview();
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadRecycleview();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),(datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(i,i1,i2);
                    binding.edDate.setText(format.format(calendar.getTime()));
                },2022,10,11).show();
            }
        });
    }

    private void loadRecycleview() {
        String filterDate = binding.edDate.getText().toString();
        String keyword = binding.edSearch.getText().toString();
        list.clear();
        if(filterDate.isEmpty())
            list.addAll(db.contractDao().getSearchExpire("%" + keyword + "%"));
        else
            list.addAll(db.contractDao().getSearchFilterExpire(filterDate,"%" + keyword + "%"));
        adapter.notifyDataSetChanged();
    }
}