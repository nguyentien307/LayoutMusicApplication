package com.example.tiennguyen.layoutmusicapplication.homefrg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiennguyen.layoutmusicapplication.R;
import com.example.tiennguyen.layoutmusicapplication.Song;
import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.String.valueOf;

/**
 * Created by TIENNGUYEN on 5/10/2017.
 */

public class TabLayoutChartAdapter extends RecyclerView.Adapter<TabLayoutChartAdapter.TablayoutChartViewHolder> {
    private List<Song> song;
    Context context;
    LayoutInflater layoutInflater;

    public TabLayoutChartAdapter(Context context,List<Song> song) {
        this.song = song;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }



    public static class TablayoutChartViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle,mArtist,mNum;
        public ImageView avatar;
        public TablayoutChartViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.id_homefrg_chart_songname);
            mArtist= (TextView) v.findViewById(R.id.id_homefrg_chart_artist);
            mNum= (TextView) v.findViewById(R.id.id_homefrg_chart_index);
            avatar= (ImageView) v.findViewById(R.id.id_homefrg_chart_image);
        }
    }
    @Override
    public TabLayoutChartAdapter.TablayoutChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chart_song_item, parent, false);
        TablayoutChartViewHolder vh=new TablayoutChartViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TabLayoutChartAdapter.TablayoutChartViewHolder holder, int position) {
            holder.mTitle.setText(song.get(position+1).getTitle());
            Picasso.with(context).load(song.get(position+1).getBgcover()).into(holder.avatar);
            holder.mArtist.setText(song.get(position+1).getArtist());
            holder.mNum.setText(valueOf(position + 2));
    }


    @Override
    public int getItemCount() {
        return (song.size()-1);
    }

}
