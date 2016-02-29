package com.android.davidlin.rentbike.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.utils.PermissionsUtils;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost fragmentTabHost;
    private String texts[] = {"租车", "出租", "我的", "设置"};
    private int imageButtons[] = {R.drawable.to_rent, R.drawable.for_rent,
            R.drawable.my_profile, R.drawable.settings};
    private int imageButtonsSelected[] = {R.drawable.to_rent_select, R.drawable.for_rent_select,
            R.drawable.my_profile_select, R.drawable.settings_select};
    private Class fragmentArrays[] = {MainToRentFragment.class, MainForRentFragment.class,
            MainMyProfileFragment.class, MainSettingsFragment.class};
    private TextView tabTextViews[] = new TextView[4];
    private ImageView tabImageViews[] = new ImageView[4];
    private Menu mainToRentMenu;

    private int pageId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 申请磁盘写入权限；6.0+必需，5.x及以下非必需
        PermissionsUtils.verifyStoragePermissions(this);

        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.main_content);
        pageId = getIntent().getIntExtra("pageId", 0);

        for (int i = 0; i < texts.length; i++) {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));

            fragmentTabHost.addTab(spec, fragmentArrays[i], null);
        }

        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabTextViews != null) {
                    for (int i = 0; i < texts.length; i++) {
                        tabTextViews[i].setTextColor(getResources().getColor(R.color.darker_gray));
                    }
                }
                if (tabImageViews != null) {
                    tabImageViews[0].setImageResource(R.drawable.to_rent);
                    tabImageViews[1].setImageResource(R.drawable.for_rent);
                    tabImageViews[2].setImageResource(R.drawable.my_profile);
                    tabImageViews[3].setImageResource(R.drawable.settings);
                }
                pageId = -1;
                if (tabId.equals(texts[0])) {
                    pageId = 0;
                    tabImageViews[0].setImageResource(R.drawable.to_rent_select);
                } else if (tabId.equals(texts[1])) {
                    pageId = 1;
                    tabImageViews[1].setImageResource(R.drawable.for_rent_select);
                } else if (tabId.equals(texts[2])) {
                    pageId = 2;
                    tabImageViews[2].setImageResource(R.drawable.my_profile_select);
                } else if (tabId.equals(texts[3])) {
                    pageId = 3;
                    tabImageViews[3].setImageResource(R.drawable.settings_select);
                }
                if (pageId != -1) {
                    View tabView = fragmentTabHost.getTabWidget().getChildTabViewAt(pageId);
                    TextView textView = (TextView) tabView.findViewById(R.id.tabcontent_text);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    getSupportActionBar().setTitle(texts[pageId]);
                }
                if (mainToRentMenu != null) {
                    if (pageId == 0) {
                        mainToRentMenu.setGroupVisible(R.id.menu_main_to_rent, true);
                    } else {
                        mainToRentMenu.setGroupVisible(R.id.menu_main_to_rent, false);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) return;
        switch (requestCode) {
            case RentBike.BIKEDETAILACTIVITY:
                fragmentTabHost.setCurrentTab(0);
                break;
            case RentBike.LOGINACTIVITY:
            case RentBike.MYACCOUNTACTIVITY:
                fragmentTabHost.setCurrentTab(2);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_to_rent, menu);
        mainToRentMenu = menu;
        fragmentTabHost.setCurrentTab(pageId);
        getSupportActionBar().setTitle(texts[pageId]);
        return true;
    }

    private View getView(int i) {
        View view = View.inflate(MainActivity.this, R.layout.tabcontent, null);
        tabImageViews[i] = (ImageView) view.findViewById(R.id.tabcontent_image);
        tabTextViews[i] = (TextView) view.findViewById(R.id.tabcontent_text);
        if (i == pageId) {
            tabImageViews[i].setImageResource(imageButtonsSelected[i]);
        } else {
            tabImageViews[i].setImageResource(imageButtons[i]);
        }
        tabTextViews[i].setText(texts[i]);
        if (i == 0) {
            tabTextViews[i].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        return view;
    }
}
