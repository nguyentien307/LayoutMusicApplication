package com.example.tiennguyen.layoutmusicapplication.framentplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiennguyen.layoutmusicapplication.R;

public class FragmentSongList extends Fragment {

    private TextView tvSonglist;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        initializeView(view);

//        String arrNew = getArguments().getString("arrNew");
//        tvSonglist.setText(arrNew);
        return view;
    }

    private void initializeView(View view){
        tvSonglist = (TextView) view.findViewById(R.id.tvSongList);
    }
}