package com.android.davidlin.rentbike.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPassEt, newPassEt, newAgainPassEt;
    private Button confirmBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        oldPassEt = (EditText) findViewById(R.id.change_password_old);
        newPassEt = (EditText) findViewById(R.id.change_password_new);
        newAgainPassEt = (EditText) findViewById(R.id.change_password_new_again);
        confirmBt = (Button) findViewById(R.id.change_password_confirm);

        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldpassword = oldPassEt.getText().toString();
                final String newpassword;
                if ("".equals(oldpassword)) {
                    Toast.makeText(ChangePasswordActivity.this,
                            "旧密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(newPassEt.getText().toString())
                        || "".equals(newAgainPassEt.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this,
                            "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassEt.getText().toString().equals(newAgainPassEt.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this,
                            "新密码输入有误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                newpassword = newPassEt.getText().toString();
                AVUser.logInInBackground(RentBike.currentUser.getUsername(),
                        oldpassword, new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if (e == null) {
                                    RentBike.currentUser.updatePasswordInBackground(oldpassword,
                                            newpassword, new UpdatePasswordCallback() {
                                                @Override
                                                public void done(AVException e) {
                                                    if (e == null) {
                                                        Toast.makeText(
                                                                ChangePasswordActivity.this,
                                                                "修改成功",
                                                                Toast.LENGTH_SHORT).show();
                                                        AVUser.logOut();
                                                        RentBike.currentUser = AVUser.getCurrentUser();
                                                        Intent intent = new Intent(
                                                                ChangePasswordActivity.this,
                                                                MainActivity.class
                                                        );
                                                        intent.setFlags(Intent
                                                                .FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("pageId", 2);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(
                                                                ChangePasswordActivity.this,
                                                                e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(),
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
}
