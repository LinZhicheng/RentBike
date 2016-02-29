package com.android.davidlin.rentbike.usercontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;

/**
 * Custom User Control {@link KeyValueTextView}
 * Created by David Lin on 2015/11/5.
 */
public class KeyValueTextView extends LinearLayout {

    private TextView keyTextView, valueTextView;

    public KeyValueTextView(Context context) {
        this(context, null);
    }

    public KeyValueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.keyvaluetextview, this, true);
        keyTextView = (TextView) findViewById(R.id.keyvaluetextview_key);
        valueTextView = (TextView) findViewById(R.id.keyvaluetextview_value);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KeyValueTextView);
        CharSequence key = array.getText(R.styleable.KeyValueTextView_key);
        if (null != key) keyTextView.setText(key);
        CharSequence value = array.getText(R.styleable.KeyValueTextView_value);
        if (null != value) valueTextView.setText(value);
        array.recycle();
    }

    public String getKey() {
        return keyTextView.getText().toString();
    }

    public void setKey(String key) {
        keyTextView.setText(key);
    }

    public String getValue() {
        return valueTextView.getText().toString();
    }

    public void setValue(String value) {
        valueTextView.setText(value);
    }
}
