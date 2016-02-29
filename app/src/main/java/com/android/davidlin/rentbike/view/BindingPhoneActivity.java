package com.android.davidlin.rentbike.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.utils.StringUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;

public class BindingPhoneActivity extends AppCompatActivity {

    private EditText phoneNumberEt, codeEt;
    private Button getCodeBt, confirmBt;
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_phone);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNumberEt = (EditText) findViewById(R.id.binding_phone_number);
        codeEt = (EditText) findViewById(R.id.binding_phone_code);
        getCodeBt = (Button) findViewById(R.id.binding_phone_getcode);
        confirmBt = (Button) findViewById(R.id.binding_code_confirm);
        timeCount = new TimeCount(60000, 1000, getCodeBt);

        phoneNumberEt.setText(RentBike.currentUser.getMobilePhoneNumber());
        getCodeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = phoneNumberEt.getText().toString();
                if (!StringUtils.isMobileNO(number)) return;
                if (RentBike.currentUser.getMobilePhoneNumber() == null) {
                    RentBike.currentUser.setMobilePhoneNumber(number);
                    RentBike.currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                requestSMSCode(number);
                            } else {
                                Toast.makeText(BindingPhoneActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    requestSMSCode(number);
                }
            }
        });

        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEt.getText().toString();
                if (code.equals("")) return;
                AVUser.verifyMobilePhoneInBackground(code, new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(BindingPhoneActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                            RentBike.currentUser.refreshInBackground(new RefreshCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    if (e == null) {
                                        RentBike.currentUser = (AVUser) avObject;
                                        finish();
                                    } else {
                                        Toast.makeText(BindingPhoneActivity.this, e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(BindingPhoneActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestSMSCode(String phoneNumber) {
        AVUser.requestMobilePhoneVerifyInBackground(phoneNumber, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(BindingPhoneActivity.this, "发送成功",
                            Toast.LENGTH_SHORT).show();
                    timeCount.start();
                } else {
                    Toast.makeText(BindingPhoneActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class TimeCount extends CountDownTimer {

        private Button button;

        public TimeCount(long millisInFuture, long countDownInterval, Button button) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
            this.button = button;
        }

        @Override
        public void onFinish() {//计时完毕时触发
            button.setText("重新获取");
            button.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            button.setClickable(false);
            button.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
