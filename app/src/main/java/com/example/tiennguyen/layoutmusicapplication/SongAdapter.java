package com.example.tiennguyen.layoutmusicapplication;

/**
 * Created by TIENNGUYEN on 5/12/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Quyen Hua on 3/30/2017.
 */

public class SongAdapter extends ArrayAdapter<Song>{

    Context context;
    ArrayList<Song> listSong = new ArrayList<>();

    public SongAdapter(Context context, int resource, ArrayList<Song> listSong) {
        super(context, resource, listSong);
        this.context = context;
        this.listSong = listSong;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        if(rowView == null){
            //LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //rowView = inflater.inflate(R.layout.item_list_song, null);
            rowView = LayoutInflater.from(context).inflate(R.layout.item_list_song, null);
            viewHolder = new ViewHolder();
            viewHolder.imageSong = (ImageView)rowView.findViewById(R.id.imageSong);
            viewHolder.title = (TextView)rowView.findViewById(R.id.tvTitle);
            viewHolder.artist = (TextView)rowView.findViewById(R.id.tvArtist);
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Picasso.with(context)
                .load(listSong.get(position).getBgcover())
                .placeholder(R.drawable.demo)
                .error(R.drawable.demo)
                .into(viewHolder.imageSong, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
        //viewHolder.imageSong.setImageBitmap(LoadImage(listSong.get(position).getBgcover()));
        viewHolder.title.setText(newText(listSong.get(position).getTitle()));
        viewHolder.artist.setText(newText(listSong.get(position).getArtist()));
        return rowView;
    }

    static class ViewHolder{
        ImageView imageSong;
        TextView title, artist;
    }

    private String newText(String text){
        String newText = "";
        if(text.length() > 18){
            for(int i = 0; i < 18;i++){
                newText = newText + text.charAt(i);
            }
            newText = newText + " ...";
        }
        else{
            newText = text;
        }
        return newText;
    }
}
