package com.example.tiennguyen.layoutmusicapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 5/17/2017.
 */

public class RecyclerSongListAdapter extends RecyclerView.Adapter<SongViewHolder> {

    ArrayList<Song> list = new ArrayList<>();
    Context context;

    public RecyclerSongListAdapter(ArrayList<Song> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row_layout, parent, false);
        SongViewHolder holder = new SongViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getArtist());
        Picasso.with(context).load(list.get(position).getBgcover()).into(holder.imageView);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Song song) {
        list.add(position, song);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Song song) {
        int position = list.indexOf(song);
        list.remove(position);
        notifyItemRemoved(position);
    }

}