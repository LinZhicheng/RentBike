package com.android.davidlin.rentbike.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.model.Bike;
import com.android.davidlin.rentbike.model.Comment;
import com.android.davidlin.rentbike.usercontrol.CircleImageView;
import com.android.davidlin.rentbike.usercontrol.KeyValueTextView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BikeDetailActivity extends AppCompatActivity {

    private static final String TAG = "BikeDetailActivity";

    private ViewPager viewPager;
    private TextView priceTv;
    private KeyValueTextView brandKYTv, typeKYTv, ageKYTv, attrsKYTv, requireKYTv, addressKYTv;
    private CircleImageView ownerPortraitIv, customerPortraitIv;
    private TextView ownerNameTv, ownerSubmitTv, customerNameTv, customerRankTv,
            customerCommentTv, commentMoreTv;
    private ImageView favoriteIv;
    private TextView contactOwnerTv, rentTv;
    private String ownerPhone;
    private List<ImageView> imageViews = new ArrayList<>();
    private ImageView[] indicator_imgs;//存放引到图片数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.bike_detail_viewpager);
        priceTv = (TextView) findViewById(R.id.bike_detail_price);
        brandKYTv = (KeyValueTextView) findViewById(R.id.bike_detail_detail_brand);
        typeKYTv = (KeyValueTextView) findViewById(R.id.bike_detail_detail_type);
        ageKYTv = (KeyValueTextView) findViewById(R.id.bike_detail_detail_age);
        attrsKYTv = (KeyValueTextView) findViewById(R.id.bike_detail_detail_attrs);
        requireKYTv = (KeyValueTextView) findViewById(R.id.bike_detail_detail_require);
        addressKYTv = (KeyValueTextView) findViewById(R.id.bike_detail_detail_address);
        ownerPortraitIv = (CircleImageView) findViewById(R.id.bike_detail_owner_portrait);
        customerPortraitIv = (CircleImageView) findViewById(R.id.bike_detail_customer_portrait);
        ownerNameTv = (TextView) findViewById(R.id.bike_detail_owner_name);
        ownerSubmitTv = (TextView) findViewById(R.id.bike_detail_owner_phone);
        customerNameTv = (TextView) findViewById(R.id.bike_detail_customer_name);
        customerRankTv = (TextView) findViewById(R.id.bike_detail_customer_rank);
        customerCommentTv = (TextView) findViewById(R.id.bike_detail_customer_comment);
        commentMoreTv = (TextView) findViewById(R.id.bike_detail_comment_more);
        favoriteIv = (ImageView) findViewById(R.id.bike_detail_favorite);
        contactOwnerTv = (TextView) findViewById(R.id.bike_detail_contact_owner);
        rentTv = (TextView) findViewById(R.id.bike_detail_rent);

        final AVObject object = getIntent().getParcelableExtra("bike");
        Bike bike = Bike.from(object);
        if (bike.getPic1() != null) {
            final ImageView pic1 = new ImageView(this);
            imageViews.add(pic1);
            pic1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BikeDetailActivity.this,
                            LargeImageActivity.class);
                    startActivity(intent);
                }
            });
            final String url = bike.getPic1().getUrl();
            RentBike.mImageLoader.displayImage(url, pic1);
            pic1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BikeDetailActivity.this,
                            LargeImageActivity.class);
                    intent.putExtra("imageUrl", url);
                    startActivity(intent);
                }
            });
        }
        if (bike.getPic2() != null) {
            final ImageView pic2 = new ImageView(this);
            imageViews.add(pic2);
            final String url = bike.getPic2().getUrl();
            RentBike.mImageLoader.displayImage(url, pic2);
            pic2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BikeDetailActivity.this,
                            LargeImageActivity.class);
                    intent.putExtra("imageUrl", url);
                    startActivity(intent);
                }
            });
        }
        if (bike.getPic3() != null) {
            final ImageView pic3 = new ImageView(this);
            imageViews.add(pic3);
            final String url = bike.getPic3().getUrl();
            RentBike.mImageLoader.displayImage(url, pic3);
            pic3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BikeDetailActivity.this,
                            LargeImageActivity.class);
                    intent.putExtra("imageUrl", url);
                    startActivity(intent);
                }
            });
        }
        Log.d(TAG, "imageViews.size() = " + imageViews.size());
        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(imageViews);
        viewPager.setAdapter(adapter);
        indicator_imgs = new ImageView[imageViews.size()];

        initIndicator(imageViews.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageViews.size(); i++) {
                    if (i == position) {
                        indicator_imgs[i].setBackgroundResource(R.drawable.indicator_focused);
                    } else {
                        indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        priceTv.setText(bike.getPrice() + " 元/天");
        brandKYTv.setValue(bike.getBrand());
        typeKYTv.setValue(bike.getType());
        ageKYTv.setValue(bike.getAge() > 5 ? "5年以上" : String.valueOf(bike.getAge()) + "年");
        attrsKYTv.setValue(bike.getAttrs());
        requireKYTv.setValue(bike.getRequire());
        addressKYTv.setValue(bike.getAddress());

        ownerPortraitIv.setImageResource(R.drawable.list_item_thumbnail);
        String ownerId = bike.getOwnerId();
        AVQuery<AVUser> queryOwner = new AVQuery<>("_User");
        queryOwner.getInBackground(ownerId, new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    AVFile portrait = avUser.getAVFile("portrait");
                    if (null != portrait) {
                        portrait.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                if (e == null) {
                                    ownerPortraitIv.setImageBitmap(
                                            BitmapFactory.decodeByteArray(bytes, 0, bytes.length)
                                    );
                                }
                            }
                        });
                    }
                    ownerNameTv.setText(avUser.getUsername());
                    Calendar c = Calendar.getInstance();
                    c.setTime(avUser.getCreatedAt());
                    String time = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-"
                            + c.get(Calendar.DAY_OF_MONTH);
                    ownerSubmitTv.setText("注册时间: " + time);
                    ownerPhone = avUser.getMobilePhoneNumber();
                } else {
                    ownerNameTv.setText("数据下载错误");
                    ownerSubmitTv.setText("数据下载错误");
                }
            }
        });


        AVQuery<AVObject> queryComment = new AVQuery<>("CommentRecord");
        queryComment.whereEqualTo("bikeId", bike.getObjectId());
        queryComment.setLimit(1);
        queryComment.orderByDescending("rank");
        queryComment.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() < 1) {
                        Log.d(TAG, "list.size() = 0");
                        return;
                    }
                    Comment comment = Comment.from(list.get(0));
                    customerRankTv.setText("评分：" + comment.getRank());
                    customerCommentTv.setText(comment.getComment());
                    String customerId = comment.getCustomerId();
                    AVQuery<AVUser> queryCustomer = new AVQuery<>("_User");
                    queryCustomer.getInBackground(customerId, new GetCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {
                                avUser.getAVFile("portrait").getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, AVException e) {
                                        if (e == null) {
                                            customerPortraitIv.setImageBitmap(
                                                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length)
                                            );
                                        } else {
                                            Log.e(TAG, e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                    customerPortraitIv.setVisibility(View.VISIBLE);
                    customerNameTv.setVisibility(View.VISIBLE);
                    customerRankTv.setVisibility(View.VISIBLE);
                    customerCommentTv.setVisibility(View.VISIBLE);
                    commentMoreTv.setText("查看更多");
                    commentMoreTv.setEnabled(true);
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        commentMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CommentsActivity
            }
        });

        favoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RentBike.currentUser == null) {
                    startLoginActivity();
                } else {
                    // 切换图片，新建收藏记录
                }
            }
        });

        contactOwnerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RentBike.currentUser == null) {
                    startLoginActivity();
                } else {
                    if (null != ownerPhone) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ownerPhone));
                            startActivity(intent);
                        } catch (SecurityException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            }
        });

        rentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RentBike.currentUser == null) {
                    startLoginActivity();
                } else {
                    Intent intent = new Intent(BikeDetailActivity.this, NewOrderActivity.class);
                    intent.putExtra("bike", object);
                    startActivity(intent);
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

    /**
     * 初始化引导图标
     * 动态创建多个小圆点，然后组装到线性布局里
     *
     * @param length 根据图片数量确定小圆点的个数
     */

    private void initIndicator(int length) {

        ImageView imgView;
        View v = findViewById(R.id.bike_detail_indicator);// 线性水平布局，负责动态调整导航图标

        for (int i = 0; i < length; i++) {
            imgView = new ImageView(this);
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(10, 10);
            params_linear.setMargins(7, 10, 7, 10);
            imgView.setLayoutParams(params_linear);
            indicator_imgs[i] = imgView;

            if (i == 0) { // 初始化第一个为选中状态
                indicator_imgs[i].setBackgroundResource(R.drawable.indicator_focused);
            } else {
                indicator_imgs[i].setBackgroundResource(R.drawable.indicator);
            }
            ((ViewGroup) v).addView(indicator_imgs[i]);
        }

    }

    private void startLoginActivity() {
        // Start LoginActivity
    }

    private class ImageViewPagerAdapter extends PagerAdapter {

        List<ImageView> imageViewList;

        public ImageViewPagerAdapter(List<ImageView> imageViews) {
            this.imageViewList = imageViews;
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }
    }

}
