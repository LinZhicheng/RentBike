package com.android.davidlin.rentbike.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.utils.StringUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;

public class FindPasswordActivity extends AppCompatActivity {

    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = (EditText) findViewById(R.id.find_password_email);
        Button confirm = (Button) findViewById(R.id.find_password_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmail(email.getText().toString())) {
                    AVUser.requestPasswordResetInBackground(email.getText().toString(),
                            new RequestPasswordResetCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        Toast.makeText(FindPasswordActivity.this,
                                                "邮件已发送，请尽快查看并更改密码！",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(FindPasswordActivity.this,
                                                e.getMessage(), Toast.LENGTH_SHORT).show();
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
