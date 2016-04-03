package com.android.davidlin.rentbike.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;

/**
 * A {@link android.support.v4.app.Fragment} for application settings
 * Created by David Lin on 2015/11/4.
 */
public class MainSettingsFragment extends Fragment {

    private View view;

    private Switch loadImageSwitch;

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

        return view;
    }
}
