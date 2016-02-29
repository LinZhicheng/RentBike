package com.android.davidlin.rentbike.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.usercontrol.CircleImageView;
import com.android.davidlin.rentbike.usercontrol.GroupTextView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;

/**
 * A {@link android.support.v4.app.Fragment} for user profile
 * Created by David Lin on 2015/11/4.
 */
public class MainMyProfileFragment extends Fragment {

    public static final int REQUEST_CHOOSE_ORDER_TYPE = 0;

    private static final String TAG = "MyProfileFragment";

    private CircleImageView portraitIv;
    private TextView usernameTv;
    private GroupTextView accountTv, orderTv, bikesTv, msgTv;
    private AVUser currentUser;
    private View.OnClickListener startLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        super.onCreateView(inflater, group, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_my_profile, null);

        portraitIv = (CircleImageView) view.findViewById(R.id.my_profile_portrait);
        usernameTv = (TextView) view.findViewById(R.id.my_profile_username);
        accountTv = (GroupTextView) view.findViewById(R.id.my_profile_myaccount);
        orderTv = (GroupTextView) view.findViewById(R.id.my_profile_myorder);
        bikesTv = (GroupTextView) view.findViewById(R.id.my_profile_mybike);
        msgTv = (GroupTextView) view.findViewById(R.id.my_profile_msg);

        startLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, RentBike.LOGINACTIVITY);
            }
        };

        portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 选择照片
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = RentBike.currentUser;

        if (currentUser == null) {
            portraitIv.setImageResource(R.drawable.list_item_thumbnail);
            portraitIv.setEnabled(false);
            usernameTv.setText("登录/注册");
            usernameTv.setOnClickListener(startLogin);
            accountTv.setOnClickListener(startLogin);
            orderTv.setOnClickListener(startLogin);
            bikesTv.setOnClickListener(startLogin);
            msgTv.setOnClickListener(startLogin);
        } else {
            AVFile portrait = currentUser.getAVFile("portrait");
            if (portrait == null) {
                portraitIv.setImageResource(R.drawable.list_item_thumbnail);
            } else {
                String url = portrait.getUrl();
                RentBike.mImageLoader.displayImage(url, portraitIv);
//                portrait.getDataInBackground(new GetDataCallback() {
//                    @Override
//                    public void done(byte[] bytes, AVException e) {
//                        if (e == null) {
//                            portraitIv.setImageBitmap(BitmapFactory
//                                    .decodeByteArray(bytes, 0, bytes.length));
//                            Log.d(TAG, "get portrait success");
//                        } else {
//                            Log.e(TAG, e.getMessage());
//                        }
//                    }
//                });
            }
            usernameTv.setText(currentUser.getUsername());
            usernameTv.setOnClickListener(null);
            accountTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                    startActivityForResult(intent, RentBike.MYACCOUNTACTIVITY);
                }
            });
            orderTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActionChoose();
                }
            });
            bikesTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // MyBikesActivity
                }
            });
            msgTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // MyMsgActivity
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CHOOSE_ORDER_TYPE) {
            int code = data.getIntExtra(OrderTypeChooseFragment.EXTRA_ORDER_TYPE_ACTION, -1);
            Intent intent = new Intent(getActivity(), MyOrdersActivity.class);
            switch (code) {
                case OrderTypeChooseFragment.CODE_BORROW:
                    intent.putExtra("type", "user");
                    break;
                case OrderTypeChooseFragment.CODE_LEND:
                    intent.putExtra("type", "owner");
                    break;
                default:
                    Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                    return;
            }
            startActivity(intent);
        }
    }

    private void startActionChoose() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        OrderTypeChooseFragment fragment = new OrderTypeChooseFragment();
        fragment.setTargetFragment(MainMyProfileFragment.this, REQUEST_CHOOSE_ORDER_TYPE);
        fragment.show(fm, "请选择您的身份：");
    }

}