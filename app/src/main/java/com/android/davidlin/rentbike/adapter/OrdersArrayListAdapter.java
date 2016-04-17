package com.android.davidlin.rentbike.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.model.Bike;
import com.android.davidlin.rentbike.model.Order;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import java.util.List;

/**
 * Adapter of Order
 * Created by David Lin on 2015/11/4.
 */
public class OrdersArrayListAdapter extends BaseAdapter {

    private static final String TAG = "OrdersArrayListAdapter";

    private List<Order> orders;
    private LayoutInflater inflater;

    public OrdersArrayListAdapter(Context context, List<Order> orders) {
        inflater = LayoutInflater.from(context);
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_order, null);
        }
        final TextView title = (TextView) convertView.findViewById(R.id.order_list_item_title);
        TextView state = (TextView) convertView.findViewById(R.id.order_list_item_state);
        final ImageView thumbnail = (ImageView) convertView.findViewById(R.id.order_list_item_thumbnail);
        TextView createdat = (TextView) convertView.findViewById(R.id.order_list_item_createdat);
        TextView finishedat = (TextView) convertView.findViewById(R.id.order_list_item_finishedat);
        TextView totalPrice = (TextView) convertView.findViewById(R.id.order_list_item_totalprice);

        Order order = orders.get(position);
        String bikeId = order.getBikeId();
        AVQuery<AVObject> query = new AVQuery<>("BikeRecord");
        query.getInBackground(bikeId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Bike bike = Bike.from(avObject);
                    title.setText(bike.getBrand() + " " + bike.getType());
                    AVFile picture = bike.getPic1();
                    if (picture != null && RentBike.isLoadImage) {
                        String url = picture.getThumbnailUrl(true, 200, 200);
                        RentBike.mImageLoader.displayImage(url, thumbnail);
                    } else {
                        thumbnail.setImageResource(R.mipmap.ic_launcher);
                    }
                    Log.d(TAG, "get object success");
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        switch (order.getState()) {
            case Order.ORDER_STATE_NON_PAYED:
                state.setText("未支付");
                break;
            case Order.ORDER_STATE_PAYED:
                state.setText("已支付");
                break;
            case Order.ORDER_STATE_GET_BIKE:
                state.setText("已取车");
                break;
            case Order.ORDER_STATE_RETURN_BIKE:
                state.setText("已还车");
                break;
            case Order.ORDER_STATE_WAITING_COMMENT:
                state.setText("待评价");
                break;
            case Order.ORDER_STATE_HAVE_COMMENT:
                state.setText("已评价");
                break;
        }
        createdat.setText("下单时间：" + order.getCreatedAt());
        finishedat.setText("完成时间：" + order.getFinishedAt());
        totalPrice.setText("总价：" + order.getTotalPrice());
        return convertView;
    }
}
