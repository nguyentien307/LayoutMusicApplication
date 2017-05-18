package com.example.tiennguyen.layoutmusicapplication;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotSongListFragment extends Fragment {

    private String url = "http://mp3.zing.vn/top100/Nhac-Tre/IWZ9Z088.html";
    public final String URL_SOURCE = "http://mp3.zing.vn/xml/song-xml/";

    ArrayList<Song> arrayListHotSong = new ArrayList<>();
    RecyclerView recyclerView;

    public HotSongListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).setActionBarTitle("Bài Hát Hot");
        View v = inflater.inflate(R.layout.fragment_hot_song_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);


        new ReadXML().execute(url);


        return v;
    }

    class ReadXML extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //XMLDOMParser1 parser = new XMLDOMParser1();
            org.jsoup.nodes.Document doc = Jsoup.parse(s);
            Element divTopSong = doc.select("div#topsong").get(0);

            Elements listItemSong = divTopSong.select("ul li");


            for (int i = 1; i < 20; i++){
                Element elementSong = listItemSong.get(i);
                String data_code = elementSong.attr("data-code");
                new LoadDataCode().execute(URL_SOURCE + data_code);
            }

        }
    }


    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }


    class LoadDataCode extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = docNoiDung_Tu_URL(strings[0]);
            return url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
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


                arrayListHotSong.add(new Song(title, artist, url, lyric, backimage, mv, link));


                RecyclerSongListAdapter adapter = new RecyclerSongListAdapter(arrayListHotSong, getActivity());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Intent player = new Intent(getActivity(), Player.class);
//                        arrayBaiHat = new ArrayList<>();
//                        arrayBaiHat.add(listBXHVietCode.get(i));
//                        arrayBaiHat.add(listBXHViet.get(i).getTitle());
//                        arrayBaiHat.add(listBXHViet.get(i).getArtist());
//                        arrayBaiHat.add(listBXHViet.get(i).getUrl());
//                        arrayBaiHat.add(listBXHViet.get(i).getLiric());
//                        arrayBaiHat.add(listBXHViet.get(i).getBgcover());
//                        arrayBaiHat.add(listBXHViet.get(i).getMv());
//                        arrayBaiHat.add(listBXHViet.get(i).getArtisturl());
//                        player.putStringArrayListExtra("arrSong", arrayBaiHat);
//                        startActivity(player);
//                        //Intent player = new Intent(MainActivity.this, Player.class);
//                        //startActivity(player);
//                    }
//                });
            }
        }
    }

}
