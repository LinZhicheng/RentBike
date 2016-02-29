package com.android.davidlin.rentbike.controller;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.davidlin.rentbike.adapter.CommentsArrayListAdapter;
import com.android.davidlin.rentbike.model.Comment;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller of Comment
 * Created by David Lin on 2015/11/3.
 */
public class CommentsLab {

    private static final String TAG = "controller.CommentsLab";

    private List<Comment> comments = new ArrayList<>();
    private CommentsArrayListAdapter adapter;
    private ListView listView;
    private int hasQueried = 0;
    private AVQuery<AVObject> query = new AVQuery<>("CommentRecord");

    public CommentsLab(Context context, ListView listView) {
        adapter = new CommentsArrayListAdapter(context, comments);
        this.listView = listView;
    }

    public void queryData() {
        query.setLimit(20);
        query.setSkip(hasQueried);
        query.orderByDescending("updatedAt");
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(600000);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject avObject : list) {
                        comments.add(Comment.from(avObject));
                    }
                    if (listView.getAdapter() == null)
                        listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    setHasQueried(getHasQueried() + list.size());
                    Log.d(TAG, "list binding success");
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public int getHasQueried() {
        return hasQueried;
    }

    public void setHasQueried(int hasQueried) {
        this.hasQueried = hasQueried;
    }
}
