package com.example.tiennguyen.layoutmusicapplication;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by TIENNGUYEN on 5/17/2017.
 */

public class SongViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;
    TextView description;
    ImageView imageView;

    SongViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardViewSong);
        title = (TextView) itemView.findViewById(R.id.tvTitle);
        description = (TextView) itemView.findViewById(R.id.tvArtist);
        imageView = (ImageView) itemView.findViewById(R.id.imageSong);
    }
}