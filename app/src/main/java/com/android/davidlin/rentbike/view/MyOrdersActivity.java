package com.android.davidlin.rentbike.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.controller.OrdersLab;

public class MyOrdersActivity extends AppCompatActivity {

    public static OrdersLab ordersLab;
    private ListView listView;
    private TextView emptyTv;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getStringExtra("type");

        listView = (ListView) findViewById(android.R.id.list);
        emptyTv = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyTv);

        ordersLab = new OrdersLab(this, listView);
        ordersLab.queryData(RentBike.currentUser.getObjectId(), type);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyOrdersActivity.this, OrderDetailActivity.class);
                intent.putExtra("order", ordersLab.getItem(position));
                intent.putExtra("type", type);
                Log.d("MyOrdersActivity", "ItemId = " + position);
                startActivity(intent);
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
