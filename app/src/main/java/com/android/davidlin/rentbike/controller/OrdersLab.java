package com.android.davidlin.rentbike.controller;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.davidlin.rentbike.adapter.OrdersArrayListAdapter;
import com.android.davidlin.rentbike.model.Order;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller of Order
 * Created by David Lin on 2015/11/3.
 */
public class OrdersLab {
    private static final String TAG = "controller.OrdersLab";

    private List<Order> orders = new ArrayList<>();
    private OrdersArrayListAdapter adapter;
    private ListView listView;
    private int hasQueried = 0;
    private AVQuery<AVObject> query = new AVQuery<>("OrderRecord");

    public OrdersLab(ListView listView) {
        this.listView = listView;
    }

    public void queryData(String userId, String type) {
        query.setLimit(20);
        query.setSkip(hasQueried);
        query.orderByDescending("updatedAt");
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(600000);
        String str;
        switch (type) {
            case "user":
                str = "customerId";
                break;
            case "owner":
                str = "ownerId";
                break;
            default:
                str = "";
        }
        query.whereEqualTo(str, userId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject avObject : list) {
                        orders.add(Order.from(avObject));
                    }
                    if (listView.getAdapter() == null)
                        listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    setHasQueried(getHasQueried() + list.size());
                    Log.d(TAG, "list binding success");
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public void refresh(Context context, String userId, String type) {
        setHasQueried(0);
        orders.clear();
        adapter = new OrdersArrayListAdapter(context, orders);
        listView.setAdapter(adapter);
        queryData(userId, type);
    }

    public int getHasQueried() {
        return hasQueried;
    }

    public void setHasQueried(int hasQueried) {
        this.hasQueried = hasQueried;
    }

    public AVObject getItem(int position) {
        return Order.to(orders.get(position));
    }
}
