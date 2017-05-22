package com.example.tiennguyen.layoutmusicapplication.framentplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiennguyen.layoutmusicapplication.lyrics.DefaultLrcBuilder;
import com.example.tiennguyen.layoutmusicapplication.lyrics.ILrcBuilder;
import com.example.tiennguyen.layoutmusicapplication.lyrics.ILrcView;
import com.example.tiennguyen.layoutmusicapplication.lyrics.LrcRow;
import com.example.tiennguyen.layoutmusicapplication.lyrics.LrcView;

import java.util.List;

/**
 * Created by Quyen Hua on 5/19/2017.
 */

public class FragmentLyric extends Fragment {

    private TextView tvLyric;
    private ILrcView mLrcView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_lyric, container, false);
        mLrcView = new LrcView(getContext(), null);

//        initialView(view);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String lyric = bundle.getString("lyric");
//            tvLyric.setText(lyric);
            readLyric(lyric);
        }

        return (View) mLrcView;
//        return view;
    }

    private void readLyric(String lyric) {
        ILrcBuilder builder = new DefaultLrcBuilder();
        List<LrcRow> rows = builder.getLrcRows(lyric);
        mLrcView.setLrc(rows);
    }

//    private void initialView(View view) {
//        tvLyric = (TextView) view.findViewById(R.id.tvLyric);
//    }


}
