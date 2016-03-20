package com.android.davidlin.rentbike.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.model.Bike;
import com.avos.avoscloud.AVObject;

import java.util.Calendar;

public class NewOrderActivity extends AppCompatActivity {

    private TextView bikeNameTv, ownerTv, attrsTv;
    private DatePicker startDatePicker, endDatePicker;
    private Button confirmBt;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bike bike = Bike.from((AVObject) getIntent().getParcelableExtra("bike"));

        bikeNameTv = (TextView) findViewById(R.id.new_order_bikename_tv);
        ownerTv = (TextView) findViewById(R.id.new_order_owner_tv);
        attrsTv = (TextView) findViewById(R.id.new_order_attrs_tv);

        bikeNameTv.setText(bike.getBrand() + " " + bike.getType());
        ownerTv.setText(bike.getOwnerId());
        attrsTv.setText(bike.getAttrs());

        startDatePicker = (DatePicker) findViewById(R.id.new_order_startdate_picker);
        endDatePicker = (DatePicker) findViewById(R.id.new_order_enddate_picker);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);
        startDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
                view.updateDate(year, monthOfYear, dayOfMonth);
            }
        });
        endDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endYear = year;
                endMonth = monthOfYear;
                endDay = dayOfMonth;
                view.updateDate(year, monthOfYear, dayOfMonth);
            }
        });

        confirmBt = (Button) findViewById(R.id.new_order_confirm_bt);
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create order and enter next page
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
