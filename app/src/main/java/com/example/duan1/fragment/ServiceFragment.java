package com.example.duan1.fragment;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duan1.Interface.IClickItemService;
import com.example.duan1.Interface.IClickItemServiceDetail;
import com.example.duan1.R;
import com.example.duan1.adapter.ServiceDetailAdapter;
import com.example.duan1.adapter.ServiceRoomAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.DialogNumberServiceDetailBinding;
import com.example.duan1.databinding.FragmentServiceBinding;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Service;
import com.example.duan1.model.ServiceDetail;
import com.example.duan1.viewmodel.InvoiceViewModel;
import com.example.duan1.viewmodel.TotalViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ServiceFragment extends Fragment {
    FragmentServiceBinding binding;
    List<ServiceDetail> listServiceDetail;
    Invoice invoice;
    DbMotel db;
    List<Service> listSevice;
    ServiceRoomAdapter serviceAdapter;
    ServiceDetailAdapter detailAdapter;
    TotalViewModel model2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.db = DbMotel.getInstance(getContext());
        listSevice = db.serviceDao().getAll();
        Log.i("servi", "onViewCreated: " + listSevice.size() );
        binding.rvService.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        //Total viewmodel
        model2 = new ViewModelProvider(getActivity()).get(TotalViewModel.class);
        //Invoice viewmodel
        new ViewModelProvider(getActivity()).get(InvoiceViewModel.class).getInvoice().observe(getViewLifecycleOwner(),o -> {
            invoice = (Invoice) o;
            handleObserve();
        });
    }

    private void handleObserve(){
        if(invoice == null)
            return;
        listServiceDetail = db.serviceDetailDao().getServiceDetailByIdInvoidce(invoice.getIdInvoice());
        Log.d("Detail", "handleObserve: " + listServiceDetail.size());
        detailAdapter = new ServiceDetailAdapter(getContext(), listServiceDetail, new IClickItemServiceDetail() {
            @Override
            public void onClickDelete(ServiceDetail serviceDetail, int i) {
                handleItemDelete(serviceDetail,i);
            }
        });
        binding.rvDetail.setAdapter(detailAdapter);
        handleTotal();
        //init adapter service
        serviceAdapter = new ServiceRoomAdapter(getContext(), listSevice, new IClickItemService() {
            //add service
            @Override
            public void onClickAdd(Service service, int i) {
                handleItemAddService(service, i);
            }
        });
        //rv set adapter service
        binding.rvService.setAdapter(serviceAdapter);
        //search service
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listSevice.clear();
                listSevice.addAll(db.serviceDao().search("%"+charSequence+"%" )) ;
                serviceAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void handleTotal() {
        int total = 0;
        for (ServiceDetail o:listServiceDetail) {
            Service service = db.serviceDao().getServiceById(o.getIdService());
            total+=o.getNumber()*service.getPrice();
        }
        model2.setTotal(total);
        binding.tvTotal.setText("Tổng tiền : " + String.format("%,d", total));
    }

    private void handleItemDelete(ServiceDetail serviceDetail, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
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

    private void handleItemAddService(Service service, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        DialogNumberServiceDetailBinding binding2 = DialogNumberServiceDetailBinding.inflate(getLayoutInflater());
        builder.setView(binding2.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();
        binding2.setService(service);
        binding2.img.setImageURI(Uri.parse(service.getImage()));
        binding2.imgNext.setOnClickListener(view -> {
            String s = binding2.edNumber.getText().toString();
            if(s!=""){
                int number = Integer.parseInt(s) + 1;
                binding2.edNumber.setText(number+"");
            }
        });
        binding2.imgPrevious.setOnClickListener(view -> {
            String s = binding2.edNumber.getText().toString();
            if(s!=""){
                int number = Integer.parseInt(s);
                if(number > 0){
                    number --;
                }
                binding2.edNumber.setText(number+"");
            }
        });
        binding2.btnAdd.setOnClickListener(view -> {
            try {
                String stringNumble = binding2.edNumber.getText().toString();
                if(stringNumble != ""){
                    int number = Integer.parseInt(stringNumble);
                    if(number != 0){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String date = format.format(new Date());
                        ServiceDetail detail = new ServiceDetail(invoice.getIdInvoice(), service.getIdService(),date,number);
                        db.serviceDetailDao().insert(detail);
                        listServiceDetail.add(0,detail);
                        detailAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        handleTotal();
                    }
                }else {
                    Toast.makeText(getContext(), "Số lượng trống!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getContext(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}