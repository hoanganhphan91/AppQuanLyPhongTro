package com.example.duan1.fragment;

import static com.example.duan1.base.BaseCheckValid.checkEmptyString;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duan1.Interface.IClickItemInvoice;
import com.example.duan1.R;
import com.example.duan1.ServiceDetailActivity;
import com.example.duan1.adapter.InvoiceAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.DialogCreateInvoiceBinding;
import com.example.duan1.databinding.DialogInvoiceDetailBinding;
import com.example.duan1.databinding.FragmentInvoiceBinding;
import com.example.duan1.model.Account;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Room;
import com.example.duan1.model.RoomType;
import com.example.duan1.model.SessionAccount;
import com.example.duan1.viewmodel.InvoiceViewModel;
import com.example.duan1.viewmodel.TotalViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceFragment extends Fragment {

    FragmentInvoiceBinding binding;
    DialogCreateInvoiceBinding binding2;
    Account account;
    RoomType roomType;
    Contract contract;
    Invoice invoice;
    List<Invoice> invoiceList = new ArrayList<>();
    DbMotel db;
    InvoiceAdapter adapter;
    Integer totalService= 0;
    InvoiceViewModel model;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
        //Invoice viewmodel
        model = new ViewModelProvider(getActivity()).get(InvoiceViewModel.class);
        model.getInvoice().observe(getViewLifecycleOwner(),o -> {
            invoice = (Invoice) o;
            handleObserve();
            binding.btnCreateInvoice.setOnClickListener(view1 -> handleCreateInvoice());
        });
        account = sessionAccount.fetchAccount();
    }
    private void handleObserve() {
        contract = db.contractDao().getContractById(invoice.getIdContract());
        roomType = db.roomTypeDao().getRTByIdContract(invoice.getIdContract());
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
                bindingDialog.tvDeltais.setOnClickListener(view -> {
                    Intent intent = new Intent(getContext(), ServiceDetailActivity.class);
                    intent.putExtra("invoice",invoice);
                    startActivity(intent);
                });
                bindingDialog.btnClose.setOnClickListener(view -> dialog.dismiss());
                dialog.show();
            }

            @Override
            public void onClickPay(Invoice invoice, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
                builder.setTitle("Thanh toán");
                builder.setMessage("Bạn có chắc chắn muốn thanh toán?");
                builder.setPositiveButton("Xác nhận",(dialogInterface, i) -> {
                    invoice.setStatus(1);
                    db.invoiceDao().update(invoice);
                    Toast.makeText(getContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    Invoice invoiceTemp = new Invoice(invoice.getNewPowerIndicator(),invoice.getNewWaterIndex(),2,contract.getIdContract(),account.getUsername());
                    db.invoiceDao().insert(invoiceTemp);
                    invoiceTemp = db.invoiceDao().getInvoiceNow(contract.getIdContract());
                    model.setInvoice(invoiceTemp);
                    dialogInterface.cancel();
                });
                builder.setNegativeButton("Hủy",(dialogInterface, i) -> dialogInterface.cancel());
                builder.create().show();
            }
        });

        binding.rvInvoice.setAdapter(adapter);
    }
    @SuppressLint("DefaultLocale")
    private void handleCreateInvoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        binding2 = DialogCreateInvoiceBinding.inflate(getLayoutInflater());
        builder.setView(binding2.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();
        //Event Editext Change
        binding2.edElectric.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    binding2.setElectric(null);
                    return;
                }
                invoice.setNewPowerIndicator(Integer.parseInt(charSequence.toString()));
                int number = invoice.getNewPowerIndicator() - invoice.getOldPowerIndicator();
                int price = number*roomType.getElectronicFee();
                binding2.setElectric(price);
                binding2.setNumberElectric(number);
                binding2.setPriceElectric(roomType.getElectronicFee());
                total();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        //Event Editext Change
        binding2.edWater.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    binding2.setWater(null);
                    return;
                }else {
                    invoice.setNewWaterIndex(Integer.parseInt(charSequence.toString()));
                    int number = invoice.getNewWaterIndex() - invoice.getOldWaterIndex();
                    int price = number*roomType.getWaterFee();
                    binding2.setWater(price);
                    binding2.setPriceWater(roomType.getWaterFee());
                    binding2.setNumberWater(number);
                    total();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        //Event Editext Change
        binding2.edFeeOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                total();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        //Total service ViewModel
        new ViewModelProvider(getActivity()).get(TotalViewModel.class).getTotal().observe(getViewLifecycleOwner(),o ->{
            totalService = (Integer) o;
            binding2.tvPriceService.setText(String.format("%,d",o));
            total();
        });

        binding2.btnCancel.setOnClickListener(view -> dialog.cancel());

        binding2.btnConfirm.setOnClickListener(view -> {
            if(binding2.getWater() == null || binding2.getElectric() == null){
                Toast.makeText(getContext(), "Bạn vui lòng không bỏ trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(binding2.getElectric() < 0 || binding2.getWater()<0){
                Toast.makeText(getContext(), "Bạn nhập chỉ số điện nước không chính xác!", Toast.LENGTH_SHORT).show();
                return;
            }
            invoice.setStatus(0);
            invoice.setDate(format.format(new Date()));
            invoice.setUsername(account.getUsername());
            invoice.setTotal(binding2.getTotal());
            invoice.setOtherFeesDescribe(binding2.edNameFeeOther.getText().toString());
            String feeOther = binding2.edFeeOther.getText().toString();
            if(!feeOther.isEmpty())
                invoice.setOtherFees(Integer.parseInt(feeOther));
            db.invoiceDao().update(invoice);
            Invoice invoiceTemp = new Invoice(invoice.getNewWaterIndex(),invoice.getNewPowerIndicator(),2,contract.getIdContract(), account.getUsername());
            db.invoiceDao().insert(invoiceTemp);
            invoiceTemp = db.invoiceDao().getInvoiceNow(invoice.getIdContract());
            model.setInvoice(invoiceTemp);
            dialog.cancel();
            Toast.makeText(getContext(), "Tạo hóa đơn thành công!", Toast.LENGTH_SHORT).show();
        });
    }

    private void total(){
        int total = 0;
        total += totalService;
        String feeOrther = binding2.edFeeOther.getText().toString();
        if(!feeOrther.isEmpty())
            total += Integer.parseInt(feeOrther);
        if(binding2.getElectric()!=null)
            total +=binding2.getElectric();
        if (binding2.getWater()!=null)
            total +=binding2.getWater();
       binding2.setTotal(total);
    }
}