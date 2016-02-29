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
import com.avos.avoscloud.SignUpCallback;

import java.util.Objects;

public class SubmitActivity extends AppCompatActivity {

    private EditText username, password, passwordAgain;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = (EditText) findViewById(R.id.submit_username);
        password = (EditText) findViewById(R.id.submit_password);
        passwordAgain = (EditText) findViewById(R.id.submit_password_again);
        confirm = (Button) findViewById(R.id.submit_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString(),
                        pwd = password.getText().toString(),
                        pwdAgain = passwordAgain.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(SubmitActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                } else if (pwd.equals("")) {
                    Toast.makeText(SubmitActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (!Objects.equals(pwd, pwdAgain)) {
                    Toast.makeText(SubmitActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                } else {
                    AVUser newUser = new AVUser();
                    newUser.setUsername(name);
                    newUser.setPassword(pwd);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(SubmitActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                RentBike.currentUser = AVUser.getCurrentUser();
                                Intent intent = new Intent(SubmitActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SubmitActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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
