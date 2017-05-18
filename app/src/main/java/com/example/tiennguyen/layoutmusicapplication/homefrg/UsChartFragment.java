package com.example.tiennguyen.layoutmusicapplication.homefrg;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tiennguyen.layoutmusicapplication.R;
import com.example.tiennguyen.layoutmusicapplication.Song;
import com.example.tiennguyen.layoutmusicapplication.XMLDOMParser;
import com.squareup.picasso.Picasso;

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
public class UsChartFragment extends Fragment {


    String url = "http://mp3.zing.vn/bang-xep-hang/index.html";
    public final String URL_SONG = "http://mp3.zing.vn/html5/song/";
    public final String URL_HOME = "http://mp3.zing.vn";
    public final String URL_SOURCE = "http://mp3.zing.vn/xml/song-xml/";

    public String imgUrl1 = "";

    public ArrayList<String> listUsChartCode = new ArrayList<>();
    public ArrayList<Song> listUsChart = new ArrayList<Song>();
    public ArrayList<String> listChartTitle = new ArrayList<String>();
    public ArrayList<String> listChartArtist = new ArrayList<String>();
    public ArrayList<String> listLinkSong = new ArrayList<String>();

    private XMLDOMParser parser = new XMLDOMParser();

    RecyclerView recyclerView;
    ImageView imgViewNum1;
    TextView txtvSongNameNum1, txtvArtistNum1;
    LinearLayoutManager layoutManager;
    TabLayoutChartAdapter tabLayoutChartAdapter;
    public UsChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_us_chart, container, false);
        imgViewNum1 = (ImageView) v.findViewById(R.id.id_homefrg_tablayout_num1_imageus);
        txtvSongNameNum1 = (TextView) v.findViewById(R.id.id_homefrg_tablayout_num1_songnameus);
        txtvArtistNum1 = (TextView) v.findViewById(R.id.id_homefrg_tablayout_num1_artistus);
        recyclerView = (RecyclerView) v.findViewById(R.id.id_homefrg_tablayout_chartlistus);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadXML().execute(url);
            }
        });


        return v;
    }


    class ReadXMLSong extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //XMLDOMParser1 parser = new XMLDOMParser1();
            org.jsoup.nodes.Document doc = Jsoup.parse(s);
            String data_code = doc.select("#tabService").attr("data-code");
            String url1 = URL_SOURCE + data_code;
            listUsChartCode.add(data_code);
            //Toast.makeText(getActivity(),url1, Toast.LENGTH_SHORT).show();

            new LoadDataCode().execute(url1);
        }
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


                recyclerView.setHasFixedSize(true);
                layoutManager=new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(true);
                listUsChart.add(new Song(title, artist, url, lyric, backimage, mv, link));


                tabLayoutChartAdapter = new TabLayoutChartAdapter(getActivity(),listUsChart);
                tabLayoutChartAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(tabLayoutChartAdapter);


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
            Element e = doc.select("div.wrap-fullwidth-content .row").get(1);
            Element col = e.select(".col-4").get(1);
            Elements listItemSong = col.select("ul li");

            Element firstSong = listItemSong.get(0);

            String linkSong1 = firstSong.select(".song-name a").attr("href");
            listLinkSong.add(0, linkSong1);
            String title1 = firstSong.select(".song-name a").text();
            listChartTitle.add(0, title1);
            String artist1 = firstSong.select("div.inblock .title-sd-item a").text();
            listChartArtist.add(0, artist1);
            imgUrl1 = firstSong.select("img").attr("src");


            txtvSongNameNum1.setText(title1);
            txtvArtistNum1.setText(artist1);



            Picasso.with(getActivity()).load(imgUrl1).resize(400, 250).into(imgViewNum1);



            for (int i = 1; i < listItemSong.size(); i++){
                Element elementSong = listItemSong.get(i);


                String linkSong = elementSong.select(".song-name a").attr("href");
                listLinkSong.add(i, linkSong);
                String title = elementSong.select(".song-name a").text();
                listChartTitle.add(i, title);
                String artist = elementSong.select("div.inblock .title-sd-item a").text();
                listChartArtist.add(i, artist);
                // String imgUrl = elementSong.select(".thumb img").attr("src");

            }

            for(int i = 0; i < 5; i++){
                new ReadXMLSong().execute(URL_HOME + listLinkSong.get(i));
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


}
