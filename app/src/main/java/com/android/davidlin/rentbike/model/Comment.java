package com.android.davidlin.rentbike.model;

import com.avos.avoscloud.AVObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Object Comment
 * Created by David Lin on 2015/11/3.
 */
public class Comment {

    private String objectId;
    private String customerId;
    private String bikeId;
    private String comment;
    private int rank;
    private Date createdAt;

    public static Comment from(AVObject avObject) {
        Comment comment = new Comment();
        comment.setObjectId(avObject.getObjectId());
        comment.setBikeId(avObject.getString("bikeId"));
        comment.setCustomerId(avObject.getString("customerId"));
        comment.setComment(avObject.getString("comment"));
        comment.setRank(avObject.getInt("rank"));
        comment.setCreatedAt(avObject.getCreatedAt());
        return comment;
    }

    public static AVObject to(Comment comment) {
        AVObject avObject = new AVObject("CommentRecord");
        avObject.put("bikeId", comment.getBikeId());
        avObject.put("customerId", comment.getCustomerId());
        avObject.put("comment", comment.getComment());
        avObject.put("rank", comment.getRank());
        return avObject;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCreatedAt() {
        StringBuilder sb = new StringBuilder();
        Calendar c = Calendar.getInstance();
        c.setTime(createdAt);
        sb.append(c.get(Calendar.YEAR));
        sb.append("-");
        sb.append(c.get(Calendar.MONTH));
        sb.append("-");
        sb.append(c.get(Calendar.DAY_OF_MONTH));
        return sb.toString();
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
