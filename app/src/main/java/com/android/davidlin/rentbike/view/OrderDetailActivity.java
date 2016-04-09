package com.android.davidlin.rentbike.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.model.Order;
import com.avos.avoscloud.AVObject;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Order order = Order.from((AVObject) getIntent().getParcelableExtra("order"));
        Log.d("OrderDetailActivity", order.getObjectId());
    }
}
