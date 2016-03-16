package com.android.davidlin.rentbike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.davidlin.rentbike.R;
import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.model.Bike;
import com.avos.avoscloud.AVFile;

import java.util.List;

/**
 * Adapter of Bikes
 * Created by David Lin on 2015/11/3.
 */
public class BikesArrayListAdapter extends BaseAdapter {

    private static final String TAG = "BikesArrayListAdapter";

    private List<Bike> bikes;
    private LayoutInflater inflater;

    public BikesArrayListAdapter(Context context, List<Bike> bikes) {
        this.bikes = bikes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bikes.size();
    }

    @Override
    public Object getItem(int position) {
        return bikes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_bike, null);
        }
        Bike bike = bikes.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.bike_list_item_title);
        title.setText(bike.getBrand() + " " + bike.getType());

        TextView price = (TextView) convertView.findViewById(R.id.bike_list_item_price);
        price.setText(bike.getPrice() + " 元/天");

        TextView attrs = (TextView) convertView.findViewById(R.id.bike_list_item_attrs);
        attrs.setText(bike.getAttrs());

        TextView age = (TextView) convertView.findViewById(R.id.bike_list_item_age);
        age.setText(bike.getAge() > 5 ? "5年以上" : String.valueOf(bike.getAge()) + "年");

        TextView data = (TextView) convertView.findViewById(R.id.bike_list_item_data);
        data.setText("出租" + bike.getRentTime() + "次|评价" + bike.getComment() + "次");

        final ImageView thumbnail = (ImageView) convertView.findViewById(R.id.bike_list_item_imageview);
        thumbnail.setImageResource(R.drawable.list_item_thumbnail);

        if (bike.getPic1() != null && RentBike.isLoadImage) {
            AVFile pic1 = bike.getPic1();
            String url = pic1.getThumbnailUrl(false, 200, 200);
            RentBike.mImageLoader.displayImage(url, thumbnail);
//            AsyncTask<String, Integer, Bitmap> task = new AsyncTask<String, Integer, Bitmap>() {
//                @Override
//                protected Bitmap doInBackground(String... params) {
//                    return HttpUtils.loadThumbnail(params[0]);
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap bitmap) {
//                    super.onPostExecute(bitmap);
//                    thumbnail.setImageBitmap(bitmap);
//                }
//            };
//            task.execute(url);
        }
        return convertView;
    }
}
