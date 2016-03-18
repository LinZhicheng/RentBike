package com.android.davidlin.rentbike.model;

import com.avos.avoscloud.AVObject;

/**
 * Created by linzh on 2016/3/18.
 */
public class Favorite {
    private String userId;
    private String bikeId;

    public static Favorite from(AVObject object) {
        Favorite favorite = new Favorite();
        favorite.setBikeId(object.getString("bikeId"));
        favorite.setUserId(object.getString("userId"));
        return favorite;
    }

    public static AVObject to(Favorite favorite) {
        AVObject object = new AVObject();
        object.add("bikeId", favorite.getBikeId());
        object.add("userId", favorite.getUserId());
        return object;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }
}
