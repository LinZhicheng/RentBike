package com.android.davidlin.rentbike.usercontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;

/**
 * GroupTextView
 * Created by David Lin on 2015/11/7.
 */
public class GroupTextView extends RelativeLayout {

    private RedTipTextView titleTv;
    private TextView tipsTv;
    private CircleTextView numberTv;

    public GroupTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.grouptextview, this, true);
        titleTv = (RedTipTextView) findViewById(R.id.grouptextview_title);
        tipsTv = (TextView) findViewById(R.id.grouptextview_tips);
        numberTv = (CircleTextView) findViewById(R.id.grouptextview_number);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GroupTextView);
        CharSequence title = array.getText(R.styleable.GroupTextView_group_title);
        if (null != title) {
            titleTv.setText(title);
        }
        array.recycle();
    }

    public String getTitle() {
        return titleTv.getText().toString();
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public String getTips() {
        return tipsTv.getText().toString();
    }

    public void setTips(String tips) {
        tipsTv.setText(tips);
    }

    public void setNumber(int number) {
        numberTv.setNotifiText(number);
    }

}
