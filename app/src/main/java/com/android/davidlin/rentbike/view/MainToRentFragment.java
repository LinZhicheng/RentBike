package com.android.davidlin.rentbike.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.controller.BikesLab;
import com.android.davidlin.rentbike.usercontrol.xlistview.XListView;
import com.android.davidlin.rentbike.utils.ConnectionUtils;

/**
 * A {@link android.support.v4.app.Fragment} for user to borrow bikes
 * Created by David Lin on 2015/11/4.
 */
public class MainToRentFragment extends Fragment {

    private final static String TAG = "ToRentFragment";
    public static BikesLab bikesLab;
    private View view;
    private XListView xListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_to_rent, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        xListView = (XListView) view.findViewById(R.id.to_rent_xlistview);
        bikesLab = new BikesLab(getActivity(), xListView);

        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if (RentBike.networkState == ConnectionUtils.NO_NETWORK) {
                    Toast.makeText(getActivity(), "没有网络连接，请检查你的设置",
                            Toast.LENGTH_SHORT).show();
                    xListView.stopRefresh();
                    return;
                }
                bikesLab.refresh(getActivity());
            }

            @Override
            public void onLoadMore() {
                if (RentBike.networkState == ConnectionUtils.NO_NETWORK) {
                    Toast.makeText(getActivity(), "没有网络连接，请检查你的设置",
                            Toast.LENGTH_SHORT).show();
                    xListView.stopLoadMore();
                    return;
                }
                bikesLab.queryData();
            }
        });
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BikeDetailActivity.class);
                intent.putExtra("bike", bikesLab.getItem(position - 1));
                startActivityForResult(intent, RentBike.BIKEDETAILACTIVITY);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RentBike.networkState == ConnectionUtils.NO_NETWORK)
            Toast.makeText(getActivity(), "没有网络连接，请检查你的设置",
                    Toast.LENGTH_SHORT).show();
        else
            bikesLab.refresh(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
