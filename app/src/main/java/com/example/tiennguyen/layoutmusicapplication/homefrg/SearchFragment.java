package com.example.tiennguyen.layoutmusicapplication.homefrg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.tiennguyen.layoutmusicapplication.ActivityPlayer;
import com.example.tiennguyen.layoutmusicapplication.R;
import com.example.tiennguyen.layoutmusicapplication.Song;
import com.example.tiennguyen.layoutmusicapplication.SongAdapter;
import com.example.tiennguyen.layoutmusicapplication.XMLDOMParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ListView lvDanhsach;
    private ProgressBar pbListSong;
    private SongAdapter adapter;

    private ArrayList<String> arrayList;
    private ArrayList<Song> arraySong;
    private ArrayList<String> arrayBaiHat;

    private XMLDOMParser parser = new XMLDOMParser();

    public final String URL_SEARCH = "http://mp3.zing.vn/tim-kiem/bai-hat.html?q=";
    public final String URL_SOURCE = "http://mp3.zing.vn/xml/song-xml/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initialView(view);
        
        Bundle bundle = getArguments();
        if(bundle != null){
            final String searchName = bundle.getString("searchName");
            pbListSong.setVisibility(View.VISIBLE);
            arrayList = new ArrayList<>();
            arraySong = new ArrayList<>();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new LoadData().execute(URL_SEARCH + searchName);
                }
            });
        }
        return view;
    }

    private void initialView(View view) {
        lvDanhsach = (ListView) view.findViewById(R.id.lvDanhsachbaihat);
        pbListSong = (ProgressBar) view.findViewById(R.id.pbLoadingListSong);
    }

    class LoadData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = parser.LoadDataFromURL(strings[0]);
            return url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

            Document doc = Jsoup.parse(s);
            Elements div = doc.select("div.item-song");

            for(int i = 0; i < div.size(); i++) {
                String data_code = div.get(i).attr("data-code");
                //Toast.makeText(MainActivity.this, data_code, Toast.LENGTH_SHORT).show();
                //arrayList.add(i, data_code);
                arrayList.add(i, data_code);
                new LoadXML().execute(URL_SOURCE + data_code);
            }
            pbListSong.setVisibility(View.INVISIBLE);
        }
    }

    class LoadXML extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = parser.LoadDataFromURL(strings[0]);
            return url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            org.w3c.dom.Document doc = parser.getDocument(s);
            if(doc != null) {
                String title = parser.getValue(doc, "title");
                String artist = parser.getValue(doc, "performer");
                String url = parser.getValue(doc, "source");
                String lyric = parser.getValue(doc, "lyric");
                String backimage = parser.getValue(doc, "backimage");
                String mv = parser.getValue(doc, "mv");
                String link = parser.getValue(doc, "link");

                if (artist == "") {
                    artist = "Đang cập nhật";
                }
                if (backimage == "") {
                    backimage = "http://1.bp.blogspot.com/-3oZ7k46VeG4/Vk0d1nmNODI/AAAAAAAAC20/3B1E5A8BDC4/s1600/hinh-anh-dep-ve-dong-vat..jpg";
                }

                arraySong.add(new Song(title, artist, url, lyric, backimage, mv, link));
                //Toast.makeText(MainActivity.this, "" + arraySong.size(), Toast.LENGTH_SHORT).show();
                adapter = new SongAdapter(getActivity(), R.layout.item_list_song, arraySong);
                adapter.notifyDataSetChanged();
                lvDanhsach.setAdapter(adapter);

                lvDanhsach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent player = new Intent(getActivity(), ActivityPlayer.class);
                        arrayBaiHat = new ArrayList<>();
                        arrayBaiHat.add(arrayList.get(i));
                        arrayBaiHat.add(arraySong.get(i).getTitle());
                        arrayBaiHat.add(arraySong.get(i).getArtist());
                        arrayBaiHat.add(arraySong.get(i).getUrl());
                        arrayBaiHat.add(arraySong.get(i).getLiric());
                        arrayBaiHat.add(arraySong.get(i).getBgcover());
                        arrayBaiHat.add(arraySong.get(i).getMv());
                        arrayBaiHat.add(arraySong.get(i).getArtisturl());
                        player.putStringArrayListExtra("arrSong", arrayBaiHat);
                        startActivity(player);
                        //Intent player = new Intent(MainActivity.this, Player.class);
                        //startActivity(player);
                    }
                });
            }
        }
    }

}
