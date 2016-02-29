package com.android.davidlin.rentbike.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;

/**
 * Choose the orders' type
 * Created by David Lin on 2015/11/18.
 */
public class OrderTypeChooseFragment extends DialogFragment {

    public static final String EXTRA_ORDER_TYPE_ACTION
            = "com.android.davidlin.rentbike.view.OrderTypeChooseFragment.action";
    public static final int CODE_BORROW = 0;
    public static final int CODE_LEND = 1;

    private TextView borrowTv;
    private TextView lendTv;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.action_choose, null);

        borrowTv = (TextView) view.findViewById(R.id.action_choose_1);
        lendTv = (TextView) view.findViewById(R.id.action_choose_2);

        borrowTv.setText("我是租客");
        lendTv.setText("我是车主");

        borrowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK, CODE_BORROW);
            }
        });
        lendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK, CODE_LEND);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void sendResult(int resultCode, int actionCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ORDER_TYPE_ACTION, actionCode);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        dismiss();
    }
}
