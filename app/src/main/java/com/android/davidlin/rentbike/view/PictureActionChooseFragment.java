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
 * Choose action to get picture
 */
public class PictureActionChooseFragment extends DialogFragment {

    public static final String EXTRA_PICTURE_ACTION
            = "com.android.davidlin.rentbike.view.pictureactionchoosefragment.action";

    public static final int CODE_CAMERA = 0;
    public static final int CODE_ALBUM = 1;

    private TextView cameraTv;
    private TextView albumTv;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.action_choose, null);

        cameraTv = (TextView) view.findViewById(R.id.action_choose_1);
        albumTv = (TextView) view.findViewById(R.id.action_choose_2);

        cameraTv.setText("拍照");
        albumTv.setText("从图库选择");

        cameraTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK, CODE_CAMERA);
            }
        });
        albumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK, CODE_ALBUM);
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
        intent.putExtra(EXTRA_PICTURE_ACTION, actionCode);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        dismiss();
    }
}
