package com.android.davidlin.rentbike.model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;

/**
 * Object Bike
 * Created by David Lin on 2015/11/3.
 */
public class Bike {

    private String objectId;
    private String ownerId;
    private String brand;
    private String type;
    private String attrs;
    private String address;
    private String require;
    private int price;
    private int comment = 0;
    private int rentTime = 0;
    private int age;
    private boolean isRenting = false;
    private AVFile pic1;
    private AVFile pic2;
    private AVFile pic3;
    private AVGeoPoint position;

    public static Bike from(AVObject avObject) {
        Bike bike = new Bike();
        bike.setObjectId(avObject.getObjectId());
        bike.setBrand(avObject.getString("brand"));
        bike.setType(avObject.getString("bikeType"));
        bike.setAttrs(avObject.getString("bikeAttrs"));
        bike.setAddress(avObject.getString("bikeAddress"));
        bike.setRequire(avObject.getString("require"));
        bike.setOwnerId(avObject.getString("ownerId"));
        bike.setAge(avObject.getInt("age"));
        bike.setComment(avObject.getInt("comment"));
        bike.setRentTime(avObject.getInt("rentTime"));
        bike.setPrice(Integer.valueOf(avObject.getString("price")));
        bike.setIsRenting(avObject.getBoolean("isRenting"));
        bike.setPic1(avObject.getAVFile("bikePic1"));
        bike.setPic2(avObject.getAVFile("bikePic2"));
        bike.setPic3(avObject.getAVFile("bikePic3"));
        bike.setPosition(avObject.getAVGeoPoint("bikePosition"));
        return bike;
    }

    public static AVObject to(Bike bike) {
        AVObject avObject = new AVObject("BikeRecord");
        if (bike.getObjectId() != null && bike.getObjectId() != "") {
            avObject.setObjectId(bike.getObjectId());
        }
        avObject.put("brand", bike.getBrand());
        avObject.put("bikeType", bike.getType());
        avObject.put("bikeAttrs", bike.getAttrs());
        avObject.put("bikeAddress", bike.getAddress());
        avObject.put("require", bike.getRequire());
        avObject.put("ownerId", bike.getOwnerId());
        avObject.put("age", bike.getAge());
        avObject.put("comment", bike.getComment());
        avObject.put("rentTime", bike.getRentTime());
        avObject.put("price", String.valueOf(bike.getPrice()));
        avObject.put("isRenting", bike.isRenting());
        avObject.put("bikePic1", bike.getPic1());
        avObject.put("bikePic2", bike.getPic2());
        avObject.put("bikePic3", bike.getPic3());
        avObject.put("bikePosition", bike.getPosition());
        return avObject;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isRenting() {
        return isRenting;
    }

    public void setIsRenting(boolean isRenting) {
        this.isRenting = isRenting;
    }

    public AVFile getPic1() {
        return pic1;
    }

    public void setPic1(AVFile pic1) {
        this.pic1 = pic1;
    }

    public AVFile getPic2() {
        return pic2;
    }

    public void setPic2(AVFile pic2) {
        this.pic2 = pic2;
    }

    public AVFile getPic3() {
        return pic3;
    }

    public void setPic3(AVFile pic3) {
        this.pic3 = pic3;
    }

    public AVGeoPoint getPosition() {
        return position;
    }

    public void setPosition(AVGeoPoint position) {
        this.position = position;
    }
}
