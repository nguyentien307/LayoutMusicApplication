package com.example.tiennguyen.layoutmusicapplication.homefrg;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tiennguyen.layoutmusicapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 5/8/2017.
 */

public class CustomSwipeAdapter extends PagerAdapter{
    private ArrayList<String> image_resources;
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomSwipeAdapter(Context context, ArrayList<String> image_resources){
        this.context = context;
        this.image_resources = image_resources;
    }

    @Override
    public int getCount() {
        return image_resources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = LayoutInflater.from(context).inflate(R.layout.swipe_layout, container, false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.id_home_fragment_slider_image_view);
        Picasso.with(context).load(image_resources.get(position)).into(imageView);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
