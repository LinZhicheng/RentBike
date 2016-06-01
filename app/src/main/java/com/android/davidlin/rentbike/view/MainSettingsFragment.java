package com.android.davidlin.rentbike.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;

import java.io.File;

/**
 * A {@link android.support.v4.app.Fragment} for application settings
 * Created by David Lin on 2015/11/4.
 */
public class MainSettingsFragment extends Fragment {

    private View view;

    private Switch loadImageSwitch;
    private RelativeLayout clearCacheLayout;
    private TextView clearCacheTv;
    private File cacheFolder;
    private File extCacheFolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_settings, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        loadImageSwitch = (Switch) view.findViewById(R.id.settings_load_image);
        loadImageSwitch.setChecked(RentBike.isLoadImage);
        loadImageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RentBike.isLoadImage = isChecked;
                SharedPreferences prefs = getActivity()
                        .getSharedPreferences("rentbike_prefs", Context.MODE_PRIVATE);
                prefs.edit().putBoolean("isLoadImage", RentBike.isLoadImage).apply();
            }
        });

        clearCacheLayout = (RelativeLayout) view.findViewById(R.id.settings_clear_cache_layout);
        clearCacheTv = (TextView) view.findViewById(R.id.settings_clear_cache_tv);
        cacheFolder = getActivity().getCacheDir();
        extCacheFolder = getActivity().getExternalCacheDir();

        clearCacheTv.setText(getCacheSize());

        clearCacheLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("确定清除缓存？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (File file : cacheFolder.listFiles()) {
                                    file.delete();
                                }
                                for (File file : extCacheFolder.listFiles()) {
                                    file.delete();
                                }
                                dialog.dismiss();
                                clearCacheTv.setText(getCacheSize());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        return view;
    }

    private String getCacheSize() {
        long usageBytes = 0L;
        for (File file : cacheFolder.listFiles()) {
            usageBytes += file.length();
        }
        for (File file : extCacheFolder.listFiles()) {
            usageBytes += file.length();
        }
        double usageKBytes = usageBytes / 1024;
        double usageMBytes = usageBytes / 1024 / 1024;
        if (usageMBytes > 1) {
            return String.valueOf(usageMBytes) + "MB";
        } else {
            return String.valueOf(usageKBytes) + "KB";
        }
    }
}
