package com.android.davidlin.rentbike.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.davidlin.rentbike.FinishOrderActivity;
import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.model.Bike;
import com.android.davidlin.rentbike.model.Order;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewOrderActivity extends AppCompatActivity {

    private TextView bikeNameTv, ownerTv, attrsTv, daysTv, priceTv;
    private Button confirmBt, fromBt, toBt;

    private int startYear, startMonth, startDay, endYear, endMonth, endDay, price;
    private Date startDate, endDate;
    private long days = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bike bike = Bike.from((AVObject) getIntent().getParcelableExtra("bike"));

        bikeNameTv = (TextView) findViewById(R.id.new_order_bikename_tv);
        ownerTv = (TextView) findViewById(R.id.new_order_owner_tv);
        attrsTv = (TextView) findViewById(R.id.new_order_attrs_tv);

        bikeNameTv.setText(bike.getBrand() + " " + bike.getType());
        final AVQuery<AVUser> findOwner = AVUser.getQuery();
        findOwner.getInBackground(bike.getOwnerId(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    ownerTv.setText(avUser.getUsername());
                } else {
                    Toast.makeText(NewOrderActivity.this, "获取车主失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        attrsTv.setText(bike.getAttrs());

        fromBt = (Button) findViewById(R.id.new_order_from_bt);
        toBt = (Button) findViewById(R.id.new_order_to_bt);
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String todayStr = simpleDateFormat.format(c.getTime());
        fromBt.setText(todayStr);
        toBt.setText(todayStr);

        daysTv = (TextView) findViewById(R.id.new_order_days_tv);
        priceTv = (TextView) findViewById(R.id.new_order_price_tv);
        priceTv.setText(String.valueOf(bike.getPrice()));
        price = bike.getPrice();

        fromBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar fromCal = Calendar.getInstance();
                try {
                    Date fromDate = simpleDateFormat.parse(fromBt.getText().toString());
                    fromCal.setTime(fromDate);
                } catch (ParseException e) {
                    Log.e("NewOrderActivity", "date parse error");
                    fromCal = c;
                }
                new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year == c.get(Calendar.YEAR)) {
                            if (monthOfYear == c.get(Calendar.MONTH)) {
                                if (dayOfMonth < c.get(Calendar.DAY_OF_MONTH)) {
                                    Toast.makeText(NewOrderActivity.this, "借车日期不能早于今天", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else if ((monthOfYear + 1) < c.get(Calendar.MONTH)) {
                                Toast.makeText(NewOrderActivity.this, "借车日期不能早于今天", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else if (year < c.get(Calendar.YEAR)) {
                            Toast.makeText(NewOrderActivity.this, "借车日期不能早于今天", Toast.LENGTH_LONG).show();
                            return;
                        }
                        startYear = year;
                        startMonth = monthOfYear + 1;
                        startDay = dayOfMonth;
                        fromBt.setText(startYear + "-" + startMonth + "-" + startDay);
                        toBt.setText(startYear + "-" + startMonth + "-" + startDay);
                    }
                }, fromCal.get(Calendar.YEAR), fromCal.get(Calendar.MONTH), fromCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar toCal = Calendar.getInstance();
                try {
                    Date fromDate = simpleDateFormat.parse(toBt.getText().toString());
                    toCal.setTime(fromDate);
                } catch (ParseException e) {
                    Log.e("NewOrderActivity", "date parse error");
                    toCal = c;
                }
                new DatePickerDialog(NewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year == startYear) {
                            if ((monthOfYear + 1) == startMonth) {
                                if (dayOfMonth < startDay) {
                                    Toast.makeText(NewOrderActivity.this, "还车日期不能早于借车日期", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } else if (monthOfYear < startMonth) {
                                Toast.makeText(NewOrderActivity.this, "还车月份不能早于借车月份", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else if (year < startYear) {
                            Toast.makeText(NewOrderActivity.this, "还车年份不能早于借车年份", Toast.LENGTH_LONG).show();
                            return;
                        }
                        endYear = year;
                        endMonth = monthOfYear + 1;
                        endDay = dayOfMonth;
                        toBt.setText(endYear + "-" + endMonth + "-" + endDay);
                        try {
                            startDate = simpleDateFormat.parse(fromBt.getText().toString());
                            endDate = simpleDateFormat.parse(toBt.getText().toString());
                            days = ((endDate.getTime() - startDate.getTime() + 1000000) / (3600 * 24 * 1000)) + 1;
                            daysTv.setText(String.valueOf(days));
                            price = (int) days * bike.getPrice();
                            priceTv.setText(String.valueOf(price));
                        } catch (ParseException pe) {
                            Log.e("NewOrderActivity", "parse date error");
                            Toast.makeText(NewOrderActivity.this, "日期解析错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, toCal.get(Calendar.YEAR), toCal.get(Calendar.MONTH), toCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        confirmBt = (Button) findViewById(R.id.new_order_confirm_bt);
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate == null) {
                    try {
                        startDate = simpleDateFormat.parse(fromBt.getText().toString());
                    } catch (ParseException e) {
                        Log.e("NewOrderActivity", e.getMessage());
                    }
                }
                if (endDate == null) {
                    try {
                        endDate = new Date();
                        endDate.setTime(simpleDateFormat.parse(fromBt.getText().toString()).getTime() + 86399999);
                    } catch (ParseException e) {
                        Log.e("NewOrderActivity", e.getMessage());
                    }
                }
                Order order = new Order();
                order.setBikeId(bike.getObjectId());
                order.setCustomerId(RentBike.currentUser.getObjectId());
                order.setOwnerId(bike.getOwnerId());
                order.setDays((int) days);
                order.setTotalPrice(price);
                order.setCreatedAt(c.getTime());
                order.setStartDate(startDate);
                order.setEndDate(endDate);
                order.setFinishedAt(null);
                final AVObject avObject = Order.to(order);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Intent intent = new Intent(NewOrderActivity.this, FinishOrderActivity.class);
                            intent.putExtra("order", avObject);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(NewOrderActivity.this, "提交订单失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
