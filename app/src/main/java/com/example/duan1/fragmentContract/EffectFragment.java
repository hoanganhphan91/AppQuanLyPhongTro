package com.example.duan1.fragmentContract;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.duan1.Interface.IClickItemContract;
import com.example.duan1.ShowMemberContract;
import com.example.duan1.adapter.Contract2Adapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.FragmentEffectBinding;
import com.example.duan1.model.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EffectFragment extends Fragment {
    FragmentEffectBinding binding;
    DbMotel db;
    Contract2Adapter adapter;
    List<Contract> list = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(new Date());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEffectBinding.inflate(inflater,container,false);
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
    }

    private void loadRecycleview() {
        String keyword = binding.edSearch.getText().toString();
        list.clear();
        list.addAll(db.contractDao().getSearchEffect(date,"%" + keyword + "%"));
        adapter.notifyDataSetChanged();
    }
}