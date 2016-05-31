package com.android.davidlin.rentbike.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.android.davidlin.rentbike.utils.PermissionsUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

/**
 * A {@link android.support.v4.app.Fragment} for user profile
 * Created by David Lin on 2015/11/4.
 */
public class MainMyProfileFragment extends Fragment {

    public static final int REQUEST_CHOOSE_ORDER_TYPE = 0;
    public static final int REQUEST_CHOOSE_PICTURE_ACTION = 1;
    public static final int REQUEST_CAMERA = 2;
    public static final int REQUEST_ALBUM = 3;
    public static final int REQUEST_CROP = 4;

    private static final String TAG = "MyProfileFragment";
    private View view;
    private CircleImageView portraitIv;
    private TextView usernameTv;
    private GroupTextView accountTv, orderTv, bikesTv, msgTv;
    private AVUser currentUser;
    private View.OnClickListener startLogin;

    // 拍照时的存储路径
    private String name = "", route = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_my_profile, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
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
                startChoosePicture();
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
                    startChooseRole();
                }
            });
            bikesTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MyBikesActivity.class);
                    startActivity(intent);
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
        } else if (requestCode == REQUEST_CHOOSE_PICTURE_ACTION) {
            int action_code = data.getIntExtra(PictureActionChooseFragment.EXTRA_PICTURE_ACTION, -1);
            switch (action_code) {
                case PictureActionChooseFragment.CODE_CAMERA:
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                            || !Environment.isExternalStorageRemovable()) {
                        route = getActivity().getExternalFilesDir(null).getPath();
                    } else {
                        route = getActivity().getFilesDir().getPath();
                    }
                    name = UUID.randomUUID().toString();
                    File dir = new File(route);
                    if (!dir.exists()) dir.mkdirs();
                    Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent_camera.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(route + "/" + name)));
                    intent_camera.putExtra("outputFormat", "JPEG");
                    try {
                        startActivityForResult(intent_camera, REQUEST_CAMERA);
                    } catch (SecurityException se) {
                        PermissionsUtils.verifyCameraPermission(getActivity());
                        Toast.makeText(getActivity(), "调用相机失败，请确认权限后重试", Toast.LENGTH_LONG).show();
                        Log.e(TAG, se.getMessage());
                    }
                    break;
                case PictureActionChooseFragment.CODE_ALBUM:
                    Intent intent_album = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent_album, REQUEST_ALBUM);
                    break;
                default:
                    Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (!route.equals("") && !name.equals("")) {
                Uri uri = Uri.fromFile(new File(route + "/" + name));
                Intent intent = startCutPicture(uri);
                startActivityForResult(intent, REQUEST_CROP);
            }
        } else if (requestCode == REQUEST_ALBUM) {
            Uri uri = data.getData();
            Intent intent = startCutPicture(uri);
            startActivityForResult(intent, REQUEST_CROP);
        } else if (requestCode == REQUEST_CROP) {
            final Bitmap bmp = data.getParcelableExtra("data");
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("请稍等...");
            dialog.setMessage("正在上传头像...");
            dialog.show();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            final AVFile portrait = new AVFile("portrait", bytes);
            portrait.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        final AVFile oldPortrait = currentUser.getAVFile("portrait");
                        currentUser.put("portrait", portrait);
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    portraitIv.setImageBitmap(bmp);
                                    Toast.makeText(getActivity(), "头像设置成功", Toast.LENGTH_SHORT).show();
                                    if (oldPortrait != null) {
                                        oldPortrait.deleteInBackground();
                                    }
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "头像设置失败", Toast.LENGTH_SHORT).show();
                                    portrait.deleteInBackground();
                                }
                            }
                        });
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void startChooseRole() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        OrderTypeChooseFragment fragment = new OrderTypeChooseFragment();
        fragment.setTargetFragment(MainMyProfileFragment.this, REQUEST_CHOOSE_ORDER_TYPE);
        fragment.show(fm, "请选择您的身份：");
    }

    private void startChoosePicture() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        PictureActionChooseFragment fragment = new PictureActionChooseFragment();
        fragment.setTargetFragment(MainMyProfileFragment.this, REQUEST_CHOOSE_PICTURE_ACTION);
        fragment.show(fm, "从哪里获取图片:");
    }

    private Intent startCutPicture(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        return intent;
    }
}
