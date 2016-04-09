package com.android.davidlin.rentbike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.davidlin.rentbike.view.MainActivity;
import com.android.davidlin.rentbike.view.OrderDetailActivity;
import com.avos.avoscloud.AVObject;

public class FinishOrderActivity extends AppCompatActivity {

    private Button homeBt, detailBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        final AVObject avObject = getIntent().getParcelableExtra("order");
        homeBt = (Button) findViewById(R.id.finish_order_home_bt);
        detailBt = (Button) findViewById(R.id.finish_order_detail_bt);
        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishOrderActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        detailBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishOrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("order", avObject);
                startActivity(intent);
            }
        });
    }
}
