package com.example.duan1.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duan1.Interface.IClickItemInvoice;
import com.example.duan1.R;
import com.example.duan1.adapter.InvoiceAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.DialogInvoiceDetailBinding;
import com.example.duan1.databinding.FragmentInvoiceBinding;
import com.example.duan1.model.Account;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Room;
import com.example.duan1.model.SessionAccount;
import com.example.duan1.viewmodel.RoomViewModel;

import java.util.ArrayList;
import java.util.List;

public class InvoiceFragment extends Fragment {

    FragmentInvoiceBinding binding;
    Account account;
    Room room;
    Contract contract;
    List<Invoice> invoiceList = new ArrayList<>();
    DbMotel db;
    InvoiceAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInvoiceBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.db = DbMotel.getInstance(getContext());
        binding.rvInvoice.setLayoutManager(new LinearLayoutManager(getContext()));
        SessionAccount sessionAccount = new SessionAccount(getContext());
        account = sessionAccount.fetchAccount();
        //Room viewmodel
        RoomViewModel model = new ViewModelProvider(getActivity()).get(RoomViewModel.class);
        model.getRoom().observe(getViewLifecycleOwner(),o -> {
            room = (Room) o;
            handleObserve();
        });



    }

    private void handleObserve() {
        contract = db.contractDao().getContractByRoomCode(room.getRoomCode());
        invoiceList.clear();
        invoiceList.addAll(db.invoiceDao().getInvoiceByIdContract(contract.getIdContract()));
        adapter = new InvoiceAdapter(getContext(), invoiceList, new IClickItemInvoice() {
            @Override
            public void onClickItem(Invoice invoice, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
                DialogInvoiceDetailBinding bindingDialog = DialogInvoiceDetailBinding.inflate(getLayoutInflater());
                builder.setView(bindingDialog.getRoot());
                bindingDialog.setInvoice(invoice);
                AlertDialog dialog = builder.create();
                dialog.show();
                bindingDialog.btnClose.setOnClickListener(view -> dialog.dismiss());
            }

            @Override
            public void onClickPay(Invoice invoice, int position) {

            }
        });
        binding.rvInvoice.setAdapter(adapter);

    }
}