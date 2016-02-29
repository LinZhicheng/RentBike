package com.android.davidlin.rentbike.view;

import android.os.Bundle;
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
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.SaveCallback;

public class BindingEmailActivity extends AppCompatActivity {

    private EditText emailAddressEt;
    private Button confirmBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emailAddressEt = (EditText) findViewById(R.id.binding_email_address);
        confirmBt = (Button) findViewById(R.id.binding_email_confirm);

        emailAddressEt.setText(RentBike.currentUser.getEmail());
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailAddressEt.getText().toString();
                if (!StringUtils.isEmail(email)) return;
                if (RentBike.currentUser.getEmail() == null) {
                    RentBike.currentUser.setEmail(email);
                    RentBike.currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                sendEmail(email);
                            } else {
                                Toast.makeText(BindingEmailActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    sendEmail(email);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        RentBike.currentUser.refreshInBackground(new RefreshCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    RentBike.currentUser = (AVUser) avObject;
                    finish();
                } else {
                    Toast.makeText(BindingEmailActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RentBike.currentUser.refreshInBackground(new RefreshCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null) {
                            RentBike.currentUser = (AVUser) avObject;
                            finish();
                        } else {
                            Toast.makeText(BindingEmailActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendEmail(String email) {
        AVUser.requestEmailVerfiyInBackground(email, new RequestEmailVerifyCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(BindingEmailActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BindingEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
