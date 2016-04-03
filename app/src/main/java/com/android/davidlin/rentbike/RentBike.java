package com.android.davidlin.rentbike;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.davidlin.rentbike.utils.ConnectionUtils;
import com.android.davidlin.rentbike.utils.image.ImageLoader;
import com.android.davidlin.rentbike.utils.image.MemoryCache;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;


/**
 * Application initialization
 * Created by David Lin on 2015/11/5.
 */
public class RentBike extends Application {

    /**
     * requestCode for BikeDetailActivity
     */
    public static final int BIKEDETAILACTIVITY = 0;

    /**
     * requestCode for LoginActivity
     */
    public static final int LOGINACTIVITY = 1;

    /**
     * requestCode for MyAccountActivity
     */
    public static final int MYACCOUNTACTIVITY = 2;
    /**
     * ImageLoader{@link} for all Application
     */
    public static final ImageLoader mImageLoader = new ImageLoader();
    /**
     * Cache for logged user
     */
    public static AVUser currentUser;
    /**
     * Record current network state
     */
    public static int networkState;
    /**
     * true, load image
     * false, do not load image
     */
    public static boolean isLoadImage;
    public static String cacheDir;

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "wfps0o81dmhbd8qukbgirh25ymfvcougc8z8rkk30xcka3ly",
                "p11t2656kdba1b7lynrfm743gv18lnv3rkhrco2i8x66nc2f");
        currentUser = AVUser.getCurrentUser();
        networkState = ConnectionUtils.getNetworkState(this);
        SharedPreferences prefs = getSharedPreferences("rentbike_prefs", MODE_PRIVATE);
        isLoadImage = prefs.getBoolean("isLoadImage", true);

        cacheDir = getCacheDir().getAbsolutePath();

        mImageLoader.setImageCache(new MemoryCache());
    }
}
