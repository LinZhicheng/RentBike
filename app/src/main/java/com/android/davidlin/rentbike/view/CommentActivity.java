package com.android.davidlin.rentbike.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.model.Comment;
import com.android.davidlin.rentbike.model.Order;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class CommentActivity extends AppCompatActivity {

    private EditText commentEt;
    private RatingBar ratingBar;
    private Button submitBt;
    private AVObject orderObj;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        commentEt = (EditText) findViewById(R.id.comment_edittext);
        ratingBar = (RatingBar) findViewById(R.id.comment_ratingbar);
        submitBt = (Button) findViewById(R.id.comment_submit_button);

        orderObj = getIntent().getParcelableExtra("order");
        order = Order.from(orderObj);

        final String bikeId = getIntent().getStringExtra("bikeId");
        final String customerId = getIntent().getStringExtra("customerId");

        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = commentEt.getText().toString();
                if (content.length() <= 0) {
                    Toast.makeText(CommentActivity.this, "请填写评论!", Toast.LENGTH_LONG).show();
                    return;
                }
                int rank = Math.round(ratingBar.getRating());
                if (rank <= 0) {
                    Toast.makeText(CommentActivity.this, "请进行评分!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (bikeId == null || customerId == null) {
                    Toast.makeText(CommentActivity.this, "获取id失败，请重启应用", Toast.LENGTH_LONG).show();
                    return;
                }
                Comment comment = new Comment();
                comment.setBikeId(bikeId);
                comment.setCustomerId(customerId);
                comment.setComment(commentEt.getText().toString());
                comment.setRank((int) ratingBar.getRating());
                AVObject avObject = Comment.to(comment);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            order.setState(Order.ORDER_STATE_HAVE_COMMENT);
                            orderObj = Order.to(order);
                            orderObj.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        Toast.makeText(CommentActivity.this, "评论上传成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(CommentActivity.this, "评论上传失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(CommentActivity.this, "评论上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
