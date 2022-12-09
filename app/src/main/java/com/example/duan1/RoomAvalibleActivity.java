package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.duan1.adapter.ViewPagerAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityRoomAvalibleBinding;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Room;
import com.example.duan1.viewmodel.ContractViewModel;
import com.example.duan1.viewmodel.InvoiceViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class RoomAvalibleActivity extends AppCompatActivity {
    final String arrTitle[] = new String[]{"Dịch vụ","Thành viên", "Hóa đơn","Hợp đồng"};
    public ActivityRoomAvalibleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomAvalibleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DbMotel db = DbMotel.getInstance(this);
        Room room = (Room) getIntent().getSerializableExtra("room");

        Contract contract = db.contractDao().getContractByRoomCode(room.getRoomCode());
        //Contract ViewModel
        new ViewModelProvider(this).get(ContractViewModel.class).setContract(contract);

        Invoice invoice = db.invoiceDao().getInvoiceNow(contract.getIdContract());
        //Invoice ViewModel
        new ViewModelProvider(this).get(InvoiceViewModel.class).setInvoice(invoice);

        //Set viewpage tablayout
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        binding.viewPager.setAdapter(pagerAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
           tab.setText(arrTitle[position]);
        });
       mediator.attach();
       binding.imgBack.setOnClickListener(view -> finish());
    }
}