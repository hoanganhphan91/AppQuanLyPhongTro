package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.duan1.adapter.Member2Adapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Member;

import java.util.List;

public class ShowMemberContract extends AppCompatActivity {
    TextView id_phong,id_clock_start,id_clock_stop,id_contract,id_room;
    Button btn_back_contract;
    RecyclerView rcv;
    DbMotel db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_member_contract);
        id_phong = findViewById(R.id.id_phong);
        id_clock_start = findViewById(R.id.id_clock_start);
        id_clock_stop = findViewById(R.id.id_clock_stop);
        id_contract = findViewById(R.id.id_contract);
        id_room = findViewById(R.id.id_room);
        btn_back_contract = findViewById(R.id.btn_back_contract);
        rcv = findViewById(R.id.rcvHopDong);
        db = DbMotel.getInstance(this);
        Contract contract = (Contract) getIntent().getSerializableExtra("contract");
        List<Member> memberList = db.memberDao().getMemberByIdContract(contract.getIdContract());
        Member2Adapter adapter = new Member2Adapter(this, memberList);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(adapter);
        id_phong.setText(contract.getIdContract()+"");
        id_clock_start.setText(contract.getStatingDate());
        id_clock_stop.setText(contract.getEndingDate());
        id_contract.setText(contract.getStatus()==1?"hiệu lực":"hết hạn");
        id_room.setText(contract.getRoomCode());

        btn_back_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}