package com.android.davidlin.rentbike.model;

import com.avos.avoscloud.AVObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Object Order
 * Created by David Lin on 2015/11/3.
 */
public class Order {

    public final static int ORDER_STATE_NON_PAYED = 0;
    public final static int ORDER_STATE_PAYED = 1;
    public final static int ORDER_STATE_GET_BIKE = 2;
    public final static int ORDER_STATE_RETURN_BIKE = 3;
    public final static int ORDER_STATE_WAITING_COMMENT = 4;
    public final static int ORDER_STATE_HAVE_COMMENT = 5;

    private String objectId;
    private String bikeId;
    private String ownerId;
    private String customerId;
    private Date createdAt;
    private Date finishedAt;
    private Date startDate;
    private Date endDate;
    private int state = ORDER_STATE_NON_PAYED;
    private int days;
    private int totalPrice;

    public static Order from(AVObject avObject) {
        Order order = new Order();
        order.setObjectId(avObject.getObjectId());
        order.setBikeId(avObject.getString("bikeId"));
        order.setCustomerId(avObject.getString("customerId"));
        order.setOwnerId(avObject.getString("ownerId"));
        order.setDays(avObject.getInt("days"));
        Date finishedAt = avObject.getDate("finishedAt");
        order.setFinishedAt(finishedAt != null ? finishedAt : null);
        order.setState(avObject.getInt("state"));
        order.setTotalPrice(avObject.getInt("totalPrice"));
        order.setCreatedAt(avObject.getCreatedAt());
        return order;
    }

    public static AVObject to(Order order) {
        AVObject avObject = new AVObject("OrderRecord");
        avObject.put("bikeId", order.getBikeId());
        avObject.put("customerId", order.getCustomerId());
        avObject.put("ownerId", order.getOwnerId());
        avObject.put("days", order.getDays());
        avObject.put("finishedAt", null);
        avObject.put("state", order.getState());
        avObject.put("totalPrice", order.getTotalPrice());
        return avObject;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreatedAt() {
        Calendar c = Calendar.getInstance();
        c.setTime(createdAt);
        return (c.get(Calendar.YEAR) + "-")
                + c.get(Calendar.MONTH) + "-"
                + c.get(Calendar.DAY_OF_MONTH)
                + " " + c.get(Calendar.HOUR_OF_DAY)
                + ":" + c.get(Calendar.MINUTE);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinishedAt() {
        Calendar c = Calendar.getInstance();
        c.setTime(finishedAt);
        return (c.get(Calendar.YEAR) + "-")
                + c.get(Calendar.MONTH) + "-"
                + c.get(Calendar.DAY_OF_MONTH)
                + " " + c.get(Calendar.HOUR_OF_DAY)
                + ":" + c.get(Calendar.MINUTE);
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
