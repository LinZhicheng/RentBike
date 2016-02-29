package com.android.davidlin.rentbike.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.model.Bike;
import com.android.davidlin.rentbike.utils.image.ImageUtils;
import com.android.davidlin.rentbike.utils.StringUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

/**
 * A {@link android.support.v4.app.Fragment} for bike owner to lend bikes
 * Created by David Lin on 2015/11/4.
 */
public class MainForRentFragment extends Fragment {

    public static final int REQUEST_CHOOSE_PICTURE_ACTION = 0;
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_ALBUM = 2;

    private static final String TAG = "ForRentFragment";

    private EditText brandEt, typeEt, addressEt, requireEt, attrEt;
    private Spinner ageSpinner;
    private ImageView[] pics = new ImageView[3];
    private AVFile[] files = new AVFile[3];
    private Button confirmBt, clearBt;
    private TextView priceTv;
    private ImageButton addPrice, subPrice;

    private TextView pleaseLogin;
    private ScrollView scrollView;

    private String[] ages = {"", "1年", "2年", "3年", "4年", "5年", "5年以上"};

    // 拍照时的存储路径
    private String name = "", route = "";
    // 选择的ImageView编号
    private int picNo = 0;
    private int age;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        super.onCreateView(inflater, group, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_for_rent, null);

        brandEt = (EditText) view.findViewById(R.id.for_rent_brand);
        typeEt = (EditText) view.findViewById(R.id.for_rent_type);
        addressEt = (EditText) view.findViewById(R.id.for_rent_address);
        requireEt = (EditText) view.findViewById(R.id.for_rent_require);
        ageSpinner = (Spinner) view.findViewById(R.id.for_rent_age);
        attrEt = (EditText) view.findViewById(R.id.for_rent_attrs);
        pics[0] = (ImageView) view.findViewById(R.id.for_rent_pic1);
        pics[1] = (ImageView) view.findViewById(R.id.for_rent_pic2);
        pics[2] = (ImageView) view.findViewById(R.id.for_rent_pic3);
        confirmBt = (Button) view.findViewById(R.id.for_rent_confirm);
        clearBt = (Button) view.findViewById(R.id.for_rent_clear);
        priceTv = (TextView) view.findViewById(R.id.for_rent_price);
        addPrice = (ImageButton) view.findViewById(R.id.for_rent_add_price);
        subPrice = (ImageButton) view.findViewById(R.id.for_rent_sub_price);

        ageSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ages));

        addPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (price < 200) {
                    price += 1;
                    priceTv.setText(String.valueOf(price));
                }
            }
        });

        subPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(priceTv.getText().toString());
                if (price > 1) {
                    price -= 1;
                    priceTv.setText(String.valueOf(price));
                }
            }
        });

        pics[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picNo = 0;
                startActionChoose();
            }
        });
        pics[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picNo = 1;
                startActionChoose();
            }
        });
        pics[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picNo = 2;
                startActionChoose();
            }
        });

        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress
                        = ProgressDialog.show(getActivity(), "请稍等...", "正在上传数据...");
                Bike bike = new Bike();
                bike.setOwnerId(RentBike.currentUser.getObjectId());
                bike.setPosition(new AVGeoPoint(0, 0));
                bike.setAddress(addressEt.getText().toString());
                bike.setAge(ageSpinner.getSelectedItemPosition());
                bike.setAttrs(attrEt.getText().toString());
                bike.setBrand(brandEt.getText().toString());
                bike.setPrice(Integer.parseInt(priceTv.getText().toString()));
                bike.setRequire(requireEt.getText().toString());
                bike.setType(typeEt.getText().toString());
                if (files[0] != null) {
                    bike.setPic1(files[0]);
                    files[0] = null;
                    bike.getPic1().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.d(TAG, "file upload success");
                            } else {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
                if (files[1] != null) {
                    bike.setPic2(files[1]);
                    files[1] = null;
                    bike.getPic2().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.d(TAG, "file upload success");
                            } else {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
                if (files[2] != null) {
                    bike.setPic3(files[2]);
                    files[2] = null;
                    bike.getPic3().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.d(TAG, "file upload success");
                            } else {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
                AVObject avObject = Bike.to(bike);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                            resetView();
                        } else {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
            }
        });

        clearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetView();
                for (int i = 0; i < files.length; i++) {
                    files[i] = null;
                }
            }
        });

        pleaseLogin = (TextView) view.findViewById(R.id.for_rent_please_login);
        pleaseLogin.setVisibility(View.INVISIBLE);
        scrollView = (ScrollView) view.findViewById(R.id.for_rent_scrollview);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RentBike.currentUser = AVUser.getCurrentUser();
        if (RentBike.currentUser == null) {
            pleaseLogin.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        } else {
            pleaseLogin.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CHOOSE_PICTURE_ACTION) {
            int action_code = data.getIntExtra(PictureActionChooseFragment.EXTRA_PICTURE_ACTION, -1);
            switch (action_code) {
                case PictureActionChooseFragment.CODE_CAMERA:
                    Calendar c = Calendar.getInstance();
                    name = "IMG_" + c.get(Calendar.YEAR) + (c.get(Calendar.MONTH) + 1)
                            + c.get(Calendar.DAY_OF_MONTH) + "_" + c.get(Calendar.HOUR_OF_DAY)
                            + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".jpg";
                    if (!Objects.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
                        route = Environment.getDataDirectory().getAbsolutePath() + "/image";
                    } else {
                        route = Environment.getExternalStoragePublicDirectory(Environment
                                .DIRECTORY_DCIM).getAbsolutePath() + "/Camera";
                    }
                    File dir = new File(route);
                    if (!dir.exists()) dir.mkdirs();
                    Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent_camera.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(route + "/" + name)));
                    intent_camera.putExtra("outputFormat", "JPEG");
                    try {
                        startActivityForResult(intent_camera, REQUEST_CAMERA);
                    } catch (SecurityException se) {
                        Toast.makeText(getActivity(), "调用相机失败", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, se.getMessage());
                        return;
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
                files[picNo] = ImageUtils.BitmapToAVFile(route + "/" + name, pics[picNo]);
                if (files[picNo] == null) {
                    Toast.makeText(getActivity(), "读取图片失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                pics[(picNo + 1) % 3].setEnabled(true);
                pics[(picNo + 1) % 3].setVisibility(View.VISIBLE);
            }
        } else if (requestCode == REQUEST_ALBUM) {
            Uri uri = data.getData();
            if (uri != null) {
                String path = StringUtils.getRealFilePath(getActivity(), uri);
                files[picNo] = ImageUtils.BitmapToAVFile(path, pics[picNo]);
                if (files[picNo] == null) {
                    Toast.makeText(getActivity(), "读取图片失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                pics[(picNo + 1) % 3].setEnabled(true);
                pics[(picNo + 1) % 3].setVisibility(View.VISIBLE);
            }
        }
    }

    private void startActionChoose() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        PictureActionChooseFragment fragment = new PictureActionChooseFragment();
        fragment.setTargetFragment(MainForRentFragment.this, REQUEST_CHOOSE_PICTURE_ACTION);
        fragment.show(fm, "从哪里获取图片:");
    }

    private void resetView() {
        brandEt.setText("");
        typeEt.setText("");
        attrEt.setText("");
        ageSpinner.setSelection(0);
        priceTv.setText("1");
        addressEt.setText("");
        requireEt.setText("");
        for (int i = 1; i < 3; i++) {
            pics[i].setEnabled(false);
            pics[i].setImageResource(R.drawable.list_item_thumbnail);
            pics[i].setVisibility(View.INVISIBLE);
        }
        pics[0].setImageResource(R.drawable.list_item_thumbnail);
        name = "";
        route = "";
        picNo = 0;
    }
}


