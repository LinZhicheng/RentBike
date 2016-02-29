package com.android.davidlin.rentbike.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.usercontrol.GroupTextView;
import com.avos.avoscloud.AVUser;

public class MyAccountActivity extends AppCompatActivity {

    private GroupTextView bindingPhone, bindingEmail, changePassword;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindingPhone = (GroupTextView) findViewById(R.id.my_account_bindingphone);
        bindingEmail = (GroupTextView) findViewById(R.id.my_account_bindingemail);
        changePassword = (GroupTextView) findViewById(R.id.my_account_changepassword);
        logout = (Button) findViewById(R.id.my_account_logout);

        bindingPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, BindingPhoneActivity.class);
                startActivity(intent);
            }
        });

        bindingEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, BindingEmailActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();
                RentBike.currentUser = AVUser.getCurrentUser();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (RentBike.currentUser.getMobilePhoneNumber() == null) {
            bindingPhone.setTips("手机未绑定");
        } else if (!RentBike.currentUser.isMobilePhoneVerified()) {
            bindingPhone.setTips("手机未验证");
        } else {
            bindingPhone.setTips("手机已验证");
        }

        if (RentBike.currentUser.getEmail() == null) {
            bindingEmail.setTips("邮箱未绑定");
        } else if (!RentBike.currentUser.getBoolean("emailVerified")) {
            bindingEmail.setTips("邮箱未验证");
        } else {
            bindingEmail.setTips("邮箱已验证");
        }
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
