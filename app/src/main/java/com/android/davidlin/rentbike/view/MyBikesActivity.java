package com.android.davidlin.rentbike.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.adapter.BikesArrayListAdapter;
import com.android.davidlin.rentbike.model.Bike;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

public class MyBikesActivity extends AppCompatActivity {

    private ListView listView;
    private List<Bike> bikes;
    private BikesArrayListAdapter bikesArrayListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bikes);

        listView = (ListView) findViewById(R.id.my_bikes_listview);
        bikes = new ArrayList<>();
        bikesArrayListAdapter = new BikesArrayListAdapter(this, bikes);
        listView.setAdapter(bikesArrayListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyBikesActivity.this, BikeDetailActivity.class);
                intent.putExtra("bike", Bike.to(bikes.get(position)));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String[] items = {"删除"};
                new AlertDialog.Builder(MyBikesActivity.this)
                        .setTitle("请选择操作：")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (items[which].equals("删除")) {
                                    AVObject avObject = Bike.to(bikes.get(position));
                                    avObject.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                bikes.remove(position);
                                                bikesArrayListAdapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(MyBikesActivity.this, "删除失败",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return true;
            }
        });
        queryMyBikes();
    }

    private void queryMyBikes() {
        AVQuery<AVObject> query = new AVQuery<>("BikeRecord");
        query.whereEqualTo("ownerId", RentBike.currentUser.getObjectId());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject object : list) {
                        bikes.add(Bike.from(object));
                    }
                    bikesArrayListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyBikesActivity.this, "下载数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
