package com.example.duan1.fragmentContract;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.example.duan1.Interface.IClickItemContract;
import com.example.duan1.R;
import com.example.duan1.ShowMemberContract;
import com.example.duan1.adapter.ContractAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.DialogExtensionContractBinding;
import com.example.duan1.databinding.FragmentWaitBinding;
import com.example.duan1.model.Contract;
import com.example.duan1.model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WaitFragment extends Fragment {
    FragmentWaitBinding binding;
    DialogExtensionContractBinding binding2;
    DbMotel db;
    ContractAdapter adapter;
    List<Contract> list = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(new Date());
    RoomType roomType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaitBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DbMotel.getInstance(getContext());
        adapter = new ContractAdapter(list, new IClickItemContract() {
            @Override
            public void onClickItem(Contract contract, int i) {
                Intent intent = new Intent(getActivity(), ShowMemberContract.class);
                intent.putExtra("contract",contract);
                startActivity(intent);
            }

            @Override
            public void onClickExtend(Contract contract, int i) {
                handleExtention(contract);
            }

            @Override
            public void onClickCancel(Contract contract, int i) {
                handleCancel(contract);
            }
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
        list.addAll(db.contractDao().getSearchWait(date,"%" + keyword + "%"));
        adapter.notifyDataSetChanged();
    }
    private void handleExtention(Contract contract) {
        roomType = db.roomTypeDao().getRTByIdContract(contract.getIdContract());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        binding2 = DialogExtensionContractBinding.inflate(getLayoutInflater());
        builder.setView(binding2.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();
        total();
        binding2.imgNext.setOnClickListener(view2 -> {
            String s = binding2.edNumber.getText().toString();
            if(s!=""){
                int number = Integer.parseInt(s) + 1;
                binding2.edNumber.setText(number+"");
            }
            total();
        });
        binding2.imgPrevious.setOnClickListener(view2-> {
            String s = binding2.edNumber.getText().toString();
            if(s!=""){
                int number = Integer.parseInt(s);
                if(number > 0){
                    number --;
                }
                binding2.edNumber.setText(number+"");
            }
            total();
        });

        binding2.edNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {total();}

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding2.btnConfirm.setOnClickListener(view2 -> {
            String number = binding2.edNumber.getText().toString();
            if(number.isEmpty()){
                Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(format.parse(contract.getEndingDate()));
                calendar.add(Calendar.MONTH,Integer.parseInt(number));
                contract.setEndingDate(format.format(calendar.getTime()));
                db.contractDao().update(contract);
                dialog.dismiss();
                loadRecycleview();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void total() {
        String number = binding2.edNumber.getText().toString();
        if(number.isEmpty())
            binding2.tvTotal.setText("Số tiền: 0");
        else {
            int num = Integer.parseInt(number);
            int total = num*roomType.getRentCode();
            binding2.tvTotal.setText("Số tiền: "+String.format("%,d",total));
        }
    }

    private void handleCancel(Contract contract) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        builder.setMessage("Bạn có chắc chắn trả phòng " + contract.getRoomCode() + " ?");
        builder.setTitle("Trả phòng");
        builder.setNegativeButton("Xác nhận",(dialogInterface, i) -> {
            contract.setStatus(0);
            db.contractDao().update(contract);
            loadRecycleview();
            dialogInterface.dismiss();
        });
        builder.setPositiveButton("Hủy bỏ",(dialogInterface, i) ->dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}