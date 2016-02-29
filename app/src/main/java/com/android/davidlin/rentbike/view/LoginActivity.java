package com.android.davidlin.rentbike.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.utils.StringUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText userPortal;
    private EditText password;
    private Button loginBt;
    private TextView forgetPassword;
    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userPortal = (EditText) findViewById(R.id.login_userportal);
        password = (EditText) findViewById(R.id.login_password);
        loginBt = (Button) findViewById(R.id.login_confirm);
        forgetPassword = (TextView) findViewById(R.id.login_forget_password);
        submit = (TextView) findViewById(R.id.login_submit);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String portal = userPortal.getText().toString();
                if (StringUtils.isMobileNO(portal)) {
                    AVUser.loginByMobilePhoneNumberInBackground(
                            portal, password.getText().toString(),
                            new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    loginCallback(avUser, e);
                                }
                            }
                    );
                } else {
                    AVUser.logInInBackground(portal, password.getText().toString(),
                            new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    loginCallback(avUser, e);
                                }
                            });
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SubmitActivity.class);
                startActivity(intent);
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

    private void loginCallback(AVUser avUser, AVException e) {
        if (e == null) {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            RentBike.currentUser = avUser;
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
