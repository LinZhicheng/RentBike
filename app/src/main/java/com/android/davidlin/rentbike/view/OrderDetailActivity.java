package com.android.davidlin.rentbike.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.model.Bike;
import com.android.davidlin.rentbike.model.Order;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

import java.util.Date;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView stateTv, customerTv, bikeNameTv, bikePriceTv, rentDaysTv,
            totalPriceTv, createTimeTv, finishTimeTv;
    private ImageView bikePic;
    private Button contactBt, changeStateBt;
    private String type;
    private Order order;
    private AVUser owner, customer;
    private AVObject orderObj;
    private int originState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderObj = getIntent().getParcelableExtra("order");
        order = Order.from(orderObj);
        type = getIntent().getStringExtra("type");

        stateTv = (TextView) findViewById(R.id.order_detail_state_tv);
        customerTv = (TextView) findViewById(R.id.order_detail_customer_tv);
        bikeNameTv = (TextView) findViewById(R.id.order_detail_bike_tv);
        bikePriceTv = (TextView) findViewById(R.id.order_detail_price_tv);
        rentDaysTv = (TextView) findViewById(R.id.order_detail_bike_days_tv);
        totalPriceTv = (TextView) findViewById(R.id.order_detail_totalprice_tv);
        createTimeTv = (TextView) findViewById(R.id.order_detail_createtime_tv);
        finishTimeTv = (TextView) findViewById(R.id.order_detail_finishtime_tv);

        bikePic = (ImageView) findViewById(R.id.order_detail_bike_iv);

        contactBt = (Button) findViewById(R.id.order_detail_contact_bt);
        changeStateBt = (Button) findViewById(R.id.order_detail_changestate_bt);

        final AVQuery<AVUser> queryUser = AVUser.getQuery();
        queryUser.getInBackground(order.getCustomerId(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    customerTv.setText(avUser.getUsername());
                    customer = avUser;
                } else {
                    Log.e("OrderDetailActivity", e.getMessage() + " Get customer failed!");
                }
            }
        });

        AVQuery<AVObject> queryBike = new AVQuery<>("BikeRecord");
        queryBike.getInBackground(order.getBikeId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Bike bike = Bike.from(avObject);
                    bikeNameTv.setText(bike.getBrand() + "\n" + bike.getType());
                    bikePriceTv.setText("￥ " + bike.getPrice());
                    if (RentBike.isLoadImage && bike.getPic1() != null) {
                        RentBike.mImageLoader.displayImage(bike.getPic1()
                                .getThumbnailUrl(true, 200, 200), bikePic);
                    } else {
                        bikePic.setImageResource(R.mipmap.ic_launcher);
                    }
                    queryUser.getInBackground(bike.getOwnerId(), new GetCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {
                                owner = avUser;
                            } else {
                                Log.e("OrderDetailActivity", e.getMessage() + " Get owner failed!");
                            }
                        }
                    });
                } else {
                    Log.e("OrderDetailActivity", e.getMessage() + " Get bike failed!");
                }
            }
        });

        rentDaysTv.setText("x" + order.getDays());
        totalPriceTv.setText("总金额：￥ " + order.getTotalPrice());

        createTimeTv.setText("创建时间: " + order.getStartDate());
        finishTimeTv.setText("完成时间: " + order.getEndDate());

        long startTime = System.currentTimeMillis();
        for (; ; ) {
            if (type != null && type.equals("user")) {
                contactBt.setText("联系车主");
                contactBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                                + owner.getMobilePhoneNumber())));
                    }
                });
                break;
            } else if (type != null && type.equals("owner")) {
                contactBt.setText("联系租客");
                contactBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                                + customer.getMobilePhoneNumber())));
                    }
                });
                break;
            } else {
                long endTime = System.currentTimeMillis();
                if (endTime - startTime > 3000) {
                    contactBt.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        originState = order.getState();
        switch (originState) {
            case Order.ORDER_STATE_NON_PAYED:
                stateTv.setText("未支付");
                break;
            case Order.ORDER_STATE_PAYED:
                stateTv.setText("已支付");
                break;
            case Order.ORDER_STATE_GET_BIKE:
                stateTv.setText("已取车");
                break;
            case Order.ORDER_STATE_RETURN_BIKE:
                stateTv.setText("已还车");
                break;
            case Order.ORDER_STATE_WAITING_COMMENT:
                stateTv.setText("待评价");
                break;
            case Order.ORDER_STATE_HAVE_COMMENT:
                stateTv.setText("已评价");
                break;
        }

        if (type != null && type.equals("user")) {
            switch (originState) {
                case Order.ORDER_STATE_NON_PAYED:
                case Order.ORDER_STATE_PAYED:
                    changeStateBt.setText("确认取车");
                    changeStateBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeStateBt.setText("已取车");
                            stateTv.setText("已取车");
                            changeStateBt.setEnabled(false);
                            order.setState(Order.ORDER_STATE_GET_BIKE);
                            AVObject avObject = Order.to(order);
                            avObject.saveInBackground();
                        }
                    });
                    break;
                case Order.ORDER_STATE_GET_BIKE:
                    changeStateBt.setText("已取车");
                    changeStateBt.setEnabled(false);
                    break;
                case Order.ORDER_STATE_RETURN_BIKE:
                case Order.ORDER_STATE_WAITING_COMMENT:
                    changeStateBt.setText("评价");
                    changeStateBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OrderDetailActivity.this, CommentActivity.class);
                            intent.putExtra("order", orderObj);
                            startActivity(intent);
                        }
                    });
                    break;
                case Order.ORDER_STATE_HAVE_COMMENT:
                    changeStateBt.setText("查看评价");
                    changeStateBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // start view my comment
                        }
                    });
                    break;
            }
        } else if (type != null && type.equals("owner")) {
            switch (originState) {
                case Order.ORDER_STATE_NON_PAYED:
                    changeStateBt.setText("确认支付");
                    changeStateBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeStateBt.setText("已支付");
                            stateTv.setText("已支付");
                            changeStateBt.setEnabled(false);
                            order.setState(Order.ORDER_STATE_PAYED);
                            AVObject avObject = Order.to(order);
                            avObject.saveInBackground();
                        }
                    });
                    break;
                case Order.ORDER_STATE_PAYED:
                    changeStateBt.setText("已支付");
                    changeStateBt.setEnabled(false);
                    break;
                case Order.ORDER_STATE_GET_BIKE:
                    changeStateBt.setText("确认还车");
                    changeStateBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeStateBt.setText("已还车");
                            stateTv.setText("已还车");
                            changeStateBt.setEnabled(false);
                            order.setState(Order.ORDER_STATE_WAITING_COMMENT);
                            order.setEndDate(new Date(System.currentTimeMillis()));
                            AVObject avObject = Order.to(order);
                            avObject.saveInBackground();
                        }
                    });
                    break;
                case Order.ORDER_STATE_RETURN_BIKE:
                case Order.ORDER_STATE_WAITING_COMMENT:
                    changeStateBt.setText("已还车");
                    changeStateBt.setEnabled(false);
                    break;
                case Order.ORDER_STATE_HAVE_COMMENT:
                    changeStateBt.setText("查看评价");
                    changeStateBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // start view my comment
                        }
                    });
                    break;
            }
        } else {
            changeStateBt.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (order.getState() != originState) {
            Intent intent = new Intent(OrderDetailActivity.this, MyOrdersActivity.class);
            intent.putExtra("type", type);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}
