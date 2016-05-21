package com.android.davidlin.rentbike.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.davidlin.rentbike.adapter.BikesArrayListAdapter;
import com.android.davidlin.rentbike.model.Bike;
import com.android.davidlin.rentbike.usercontrol.xlistview.XListView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Controller of Bike
 * Created by David Lin on 2015/11/3.
 */
public class BikesLab {

    private static final String TAG = "controller.BikesLab";

    private List<Bike> bikes = new ArrayList<>();
    private BikesArrayListAdapter adapter;
    private XListView listView;
    private AVQuery<AVObject> query = new AVQuery<>("BikeRecord");
    private int hasQueried = 0;
    private ProgressDialog dialog;

    public BikesLab(Context context, XListView listView) {
        this.listView = listView;
        this.dialog = new ProgressDialog(context);
    }

    public void queryData() {
        dialog.create();
        query.setLimit(20);
        query.setSkip(hasQueried);
        query.orderByDescending("updatedAt");
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(600000);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject avObject : list) {
                        bikes.add(Bike.from(avObject));
                    }
                    setHasQueried(getHasQueried() + list.size());
                    if (list.size() < 20) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    listView.setRefreshTime(new SimpleDateFormat("MM/dd hh:mm", Locale.CHINA).format(new Date()));
                } else {
                    Log.e(TAG, e.getMessage());
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    public void refresh(Context context) {
        setHasQueried(0);
        bikes.clear();
        adapter = new BikesArrayListAdapter(context, bikes);
        listView.setAdapter(adapter);
        queryData();
    }

    public int getHasQueried() {
        return hasQueried;
    }

    public void setHasQueried(int hasQueried) {
        this.hasQueried = hasQueried;
    }

    public AVObject getItem(int i) {
        return Bike.to(bikes.get(i));
    }

}
