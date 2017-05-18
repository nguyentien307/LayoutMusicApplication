package com.example.tiennguyen.layoutmusicapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TIENNGUYEN on 5/11/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<Album> album;
    Context context;
    LayoutInflater layoutInflater;

    public AlbumAdapter(Context context,List<Album> album) {
        this.album = album;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }



    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle,mArtist;
        public ImageView avatar;
        public AlbumViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.id_homefrg_album_name);
            mArtist= (TextView) v.findViewById(R.id.id_homefrg_album_artist);

            avatar= (ImageView) v.findViewById(R.id.id_homefrg_album_image);
        }
    }
    @Override
    public AlbumAdapter.AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_layout, parent, false);
        AlbumViewHolder vh=new AlbumViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.AlbumViewHolder holder, int position) {
        holder.mTitle.setText(album.get(position).getTitle());
        Picasso.with(context).load(album.get(position).getImg()).into(holder.avatar);
        holder.mArtist.setText(album.get(position).getArtist());
    }


    @Override
    public int getItemCount() {
        return album.size();
    }

}
