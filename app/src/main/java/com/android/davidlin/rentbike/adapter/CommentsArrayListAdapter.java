package com.android.davidlin.rentbike.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.model.Comment;
import com.android.davidlin.rentbike.usercontrol.CircleImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;

import java.util.List;

/**
 * Adapter of Comments
 * Created by David Lin on 2015/11/3.
 */
public class CommentsArrayListAdapter extends BaseAdapter {

    private static final String TAG = "CommentArrayListAdapter";

    private List<Comment> comments;
    private LayoutInflater inflater;

    public CommentsArrayListAdapter(Context context, List<Comment> comments) {
        this.comments = comments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_comment, null);
        }
        Comment comment = comments.get(position);
        TextView customerName = (TextView) convertView.findViewById(R.id.comment_list_item_customerName);
        customerName.setText(comment.getCustomerId());
        TextView rank = (TextView) convertView.findViewById(R.id.comment_list_item_rank);
        rank.setText("评分：" + comment.getRank());
        TextView date = (TextView) convertView.findViewById(R.id.comment_list_item_date);
        date.setText(comment.getCreatedAt());
        TextView commentWord = (TextView) convertView.findViewById(R.id.comment_list_item_comment);
        commentWord.setText(comment.getComment());
        final CircleImageView portrait = (CircleImageView) convertView.findViewById(R.id.comment_list_item_portrait);
        portrait.setImageResource(R.drawable.list_item_thumbnail);
        AVQuery<AVUser> query = AVUser.getQuery();
        query.getInBackground(comment.getCustomerId(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    AVFile picture = avUser.getAVFile("portrait");
                    if (picture != null) {
                        picture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                if (e == null) {
                                    portrait.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    Log.d(TAG, "set portrait success");
                                } else {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        return convertView;
    }
}
