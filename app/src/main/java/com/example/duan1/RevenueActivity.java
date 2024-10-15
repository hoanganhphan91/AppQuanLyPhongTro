package com.example.duan1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.adapter.RevenueAdapter;
import com.example.duan1.database.DbMotel;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Revenue;
import com.example.duan1.util.Utils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RevenueActivity extends AppCompatActivity {

    List<Invoice> invoiceList;
    DbMotel db;

    String firstDayText, lastDayText;

    int totalPaidAmount = 0, totalUnpaidAmount = 0, totalAmount = 0;

    TextView paidAmount, unpaidAmount, amount, dateRange;

    int showType = 0;

    RecyclerView recyclerView;

    Spinner spinner;
    String[] spinnerValues = {"Tất cả", "Trả hết", "Còn nợ"};


    RevenueAdapter adapter;

    ImageView imageBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        db = DbMotel.getInstance(this);

        initUi(); // Ánh xạ

        setDefaultSpinner();
        getData();
        spinnerSelectionListener();
        backEvent();
        initDatePicker();
        getLabelDateRangePicker();

    }


    private void initUi() {
        recyclerView = findViewById(R.id.listRevenue);
        spinner = findViewById(R.id.spinner);
        paidAmount = findViewById(R.id.paid_amount);
        unpaidAmount = findViewById(R.id.unpaid_amount);
        amount = findViewById(R.id.amount);
        imageBack = findViewById(R.id.imgBack);
        dateRange = findViewById(R.id.date_range);
    }

    private void getLabelDateRangePicker () {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar firstDayOfMonth = getFirstDayOfCurrentMonth();
        Calendar lastDayOfMonth = getCurrentDay();
        firstDayText = simpleDateFormat.format(firstDayOfMonth.getTime());
        lastDayText = simpleDateFormat.format(lastDayOfMonth.getTime());
        setLabelDateRangePicker();

    }

    private void setLabelDateRangePicker () {
        dateRange.setText(firstDayText + " - " + lastDayText);
    }




    private void initDatePicker () {
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())).build();

        dateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), dateRange.toString());
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Long startDate = selection.first;
                        Long endDate = selection.second;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        firstDayText = simpleDateFormat.format(startDate);
                        lastDayText = simpleDateFormat.format(endDate);
                        setLabelDateRangePicker();
                        getData();
                    }
                });
            }
        });

    }

    private void backEvent () {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setDefaultSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void spinnerSelectionListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showType = position;
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private Calendar getFirstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setTimeToStartOfDay(calendar);
        return calendar;
    }

    private void setTimeToStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public Calendar getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        setTimeToStartOfDay(calendar);
        return calendar;
    }


    private void getData() {

        totalAmount = 0;
        totalPaidAmount = 0;
        totalUnpaidAmount = 0;

        invoiceList = db.invoiceDao().getInvoicesBetweenDates(firstDayText, lastDayText);

        List<Revenue> combinedInvoices = new ArrayList<>();

        Map<Integer, Revenue> revenueMap = new HashMap<>();

        for (Invoice invoice : invoiceList) {
            int idContract = invoice.getIdContract();

            // Check IdContract existing
            if (revenueMap.containsKey(idContract)) {
                Revenue existingRevenue = revenueMap.get(idContract);

                if (invoice.getStatus() == 0) {
                    existingRevenue.setUnpaidAmount(existingRevenue.getUnpaidAmount() + invoice.getTotal());
                } else if (invoice.getStatus() == 1) {
                    existingRevenue.setPaidAmount(existingRevenue.getPaidAmount() + invoice.getTotal());
                }

            } else {
                Revenue newRevenue = new Revenue();
                newRevenue.setIdContract(invoice.getIdContract());

                if (invoice.getStatus() == 0) {
                    newRevenue.setUnpaidAmount(invoice.getTotal());
                } else if (invoice.getStatus() == 1) {
                    newRevenue.setPaidAmount(invoice.getTotal());
                }

                revenueMap.put(idContract, newRevenue);
            }
        }

        combinedInvoices.addAll(revenueMap.values());


        List<Revenue> filteredList = new ArrayList<>();

        if (showType == 0) {
            filteredList = combinedInvoices;
        } else {
            for (Revenue revenue : combinedInvoices) {

                switch (showType) {
                    case 1:
                        if (revenue.getUnpaidAmount() == 0) {
                            filteredList.add(revenue);
                        }
                        break;
                    case 2:
                        if (revenue.getUnpaidAmount() != 0) {
                            filteredList.add(revenue);
                        }
                        break;

                }
            }
        }

        for (Revenue revenue : filteredList) {

            totalPaidAmount = revenue.getPaidAmount() + totalPaidAmount;
            totalUnpaidAmount = revenue.getUnpaidAmount() + totalUnpaidAmount;
        }

        totalAmount = totalPaidAmount + totalUnpaidAmount;
        showLabelTotal();

        adapter = new RevenueAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void showLabelTotal(){
        paidAmount.setText("Đã trả: " + Utils.formatMoney(totalPaidAmount));
        unpaidAmount.setText("Chưa trả: " + Utils.formatMoney(totalUnpaidAmount));
        amount.setText("Tổng: " + Utils.formatMoney(totalAmount));
    }
}