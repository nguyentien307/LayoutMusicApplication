package com.example.tiennguyen.layoutmusicapplication.homefrg;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.tiennguyen.layoutmusicapplication.Album;
import com.example.tiennguyen.layoutmusicapplication.AlbumAdapter;
import com.example.tiennguyen.layoutmusicapplication.AlbumFragment;
import com.example.tiennguyen.layoutmusicapplication.Helper;
import com.example.tiennguyen.layoutmusicapplication.HotSongListFragment;
import com.example.tiennguyen.layoutmusicapplication.MainActivity;
import com.example.tiennguyen.layoutmusicapplication.R;
import com.example.tiennguyen.layoutmusicapplication.Song;
import com.example.tiennguyen.layoutmusicapplication.SongAdapter;
import com.example.tiennguyen.layoutmusicapplication.XMLDOMParser;

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
public class HomeFragment extends Fragment {

    FragmentTransaction fragmentTransaction;

    String url = "http://mp3.zing.vn/";

    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    TabLayout tabLayout;
    ViewPager tabLayoutViewPager;
    TabLayoutAdapter tabLayoutAdapter;
    View view;

    ArrayList<Album> album;
    AlbumAdapter albumAdapter;
    RecyclerView recyclerViewAlbum;
    LinearLayoutManager layoutManager;

    ArrayList<String> sliderImgArr;

    ListView listViewHotSong;
    private ArrayList<Song> arrayListHotSong;
    ArrayList<String> arrayListHotSongCode = new ArrayList<>();
    public final String URL_SONG = "http://mp3.zing.vn/html5/song/";
    SongAdapter songAdapter;
    public final String URL_SOURCE = "http://mp3.zing.vn/xml/song-xml/";
    private XMLDOMParser parser = new XMLDOMParser();

    LinearLayout linearLayoutAlbumActivity;
    LinearLayout linearLayoutHotSongListActivity;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).setActionBarTitle("Home");

        //View pager home fragment slider
        viewPager = (ViewPager) view.findViewById(R.id.id_home_fragment_slider);
        tabLayout = (TabLayout)view.findViewById(R.id.id_homefrg_tab_layout);

        listViewHotSong = (ListView) view.findViewById(R.id.id_homefrg_hotsonglist);
        arrayListHotSong = new ArrayList<>();


        tabLayoutViewPager = (ViewPager) view.findViewById(R.id.id_homefrg_tablayout_viewpager);
        tabLayoutAdapter = new TabLayoutAdapter(getChildFragmentManager());





        tabLayoutAdapter.addFragments(new VietChartFragment(), "Việt Nam");
        tabLayoutAdapter.addFragments(new UsChartFragment(), "Âu Mỹ");
        tabLayoutAdapter.addFragments(new KoreanChartFragment(), "Hàn Quốc");
        tabLayoutViewPager.setAdapter(tabLayoutAdapter);
        tabLayout.setupWithViewPager(tabLayoutViewPager);


        recyclerViewAlbum = (RecyclerView) view.findViewById(R.id.id_homefrg_album_scrollview);

        //------------slider


        linearLayoutAlbumActivity = (LinearLayout) view.findViewById(R.id.id_homefrg_album_activity);
        linearLayoutHotSongListActivity = (LinearLayout) view.findViewById(R.id.id_homefrg_hotsonglist_activity);


        linearLayoutAlbumActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, new AlbumFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        linearLayoutHotSongListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, new HotSongListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadXML().execute(url);
            }
        });


        return view;
    }



    class ReadXML extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            org.jsoup.nodes.Document doc = Jsoup.parse(s);

            Elements alb = doc.select("div#albumHot .album-item");
            album=new ArrayList<Album>();

            for(int i = 0; i < 4; i++) {
                Element albItem = alb.get(i);
                String albImgUrl = albItem.select("img").attr("src");
                String title = albItem.select(".title-item a").text();
                String artist = albItem.select(".fn-artist a").text();
                album.add(new Album(albImgUrl,title,artist));

            }




            layoutManager=new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewAlbum.setLayoutManager(layoutManager);
            recyclerViewAlbum.setNestedScrollingEnabled(false);

            albumAdapter=new AlbumAdapter(getActivity(),album);
            albumAdapter.notifyDataSetChanged();
            recyclerViewAlbum.setAdapter(albumAdapter);

            //-----------Slider

            Elements e = doc.select("div.slide-scroll img");

            sliderImgArr = new ArrayList<String>();
            sliderImgArr.add(e.get(0).attr("src"));
            for(int i = 1; i < e.size(); i++ ){
                sliderImgArr.add(e.get(i).attr("data-lazy"));
            }

            adapter = new CustomSwipeAdapter(getActivity(), sliderImgArr);
            viewPager.setAdapter(adapter);

            Elements hotSong = doc.select(".fn-song");



            for (int i = 0; i < 10; i++){
                Element elementSong = hotSong.get(i);
                String data_code = elementSong.attr("data-code");
                arrayListHotSongCode.add(i,data_code);
                new LoadDataCode().execute(URL_SOURCE+data_code);
            }


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
                songAdapter = new SongAdapter(getContext(), R.layout.item_list_song, arrayListHotSong);
                songAdapter.notifyDataSetChanged();
                listViewHotSong.setAdapter(songAdapter);
                Helper.getListViewSize(listViewHotSong);


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
