package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.duan1.Interface.IClickItemContract;
import com.example.duan1.adapter.ContractAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.model.Contract;

import java.util.ArrayList;
import java.util.List;

public class ContractManageActivity extends AppCompatActivity {
    SearchView search_bar;
    ContractAdapter contractAdapter;
    List<Contract> listct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_manage);
        RecyclerView recyclerQlHopDong = findViewById(R.id.rcvHopDong);
        search_bar = findViewById(R.id.search_bar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // data
        DbMotel db = DbMotel.getInstance(this);
        listct = db.contractDao().getAll();
//        try {
//            Contract contract = new Contract();
//            db.contractDao().delete(contract);
//        } catch (Exception ex) {
//            Toast.makeText(getContext(), "Xoa that bai", Toast.LENGTH_SHORT).show();
//        }
        //adapaater
        contractAdapter = new ContractAdapter(listct, this, new IClickItemContract() {
            @Override
            public void onClickDelete(Contract contract, int i) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContractManageActivity.this);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa hợp đồng?");
                builder.setPositiveButton("Xác nhận", (dialogInterface, i1) -> {
                    try {
                        listct.remove(i);
                        db.contractDao().delete(contract);
                        contractAdapter.notifyItemRemoved(i);
                        contractAdapter.notifyItemRangeRemoved(i, contractAdapter.getItemCount());
                    } catch (Exception e) {
                        Toast.makeText(ContractManageActivity.this, "Lỗi xóa Hợp Đồng", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();
                });

                builder.setNegativeButton("Hủy", (dialogInterface, i1) -> {
                    dialogInterface.dismiss();
                });

                builder.create().show();
            }
        });
        recyclerQlHopDong.setAdapter(contractAdapter);
        recyclerQlHopDong.setLayoutManager(new LinearLayoutManager(this));

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSearch(newText);
                return false;
            }
        });

    }

    public void getSearch(String text){
        ArrayList<Contract> flist = new ArrayList<>();
        for (Contract contract : listct){
            if(contract.getRoomCode().toLowerCase().contains(text.toLowerCase())){
                flist.add(contract);
            }
        }
        contractAdapter.getFillList(flist);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
