package com.example.tiennguyen.layoutmusicapplication;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

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
public class AlbumFragment extends Fragment {

    FragmentTransaction fragmentTransaction;

    private String urlAlbum = "http://mp3.zing.vn/the-loai-album.html";

    private RecyclerView recyclerViewViet, recyclerViewUs, recyclerViewKorean, recyclerViewHoaTau;
    private AlbumsAdapter adapter;
    private ArrayList<Album> albumList;

    LinearLayout vietAlbums, usAlbums, koreanAlbums, hoatauAlbums;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).setActionBarTitle("Album");

        View v = inflater.inflate(R.layout.fragment_album, container, false);
//        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//
//        initCollapsingToolbar();

        recyclerViewViet = (RecyclerView) v.findViewById(R.id.recycler_view_viet);
        recyclerViewUs = (RecyclerView) v.findViewById(R.id.recycler_view_us);
        recyclerViewKorean = (RecyclerView) v.findViewById(R.id.recycler_view_korean);
        recyclerViewHoaTau = (RecyclerView) v.findViewById(R.id.recycler_view_hoatau);

        vietAlbums = (LinearLayout) v.findViewById(R.id.id_albumfrg_viet);
        usAlbums = (LinearLayout) v.findViewById(R.id.id_albumfrg_us);
        koreanAlbums = (LinearLayout) v.findViewById(R.id.id_albumfrg_korean);
        hoatauAlbums = (LinearLayout) v.findViewById(R.id.id_albumfrg_hoatau);

        vietAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, new VietAlbumFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        usAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, new UsAlbumFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        koreanAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, new KoreanAlbumFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        hoatauAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, new HoaTauAlbumFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) v.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return v;

    }

//    private void initCollapsingToolbar() {
//        final CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) getView().findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(" ");
//        AppBarLayout appBarLayout = (AppBarLayout) getView().findViewById(R.id.appbar);
//        appBarLayout.setExpanded(true);
//
//        // hiding & showing the title when toolbar expanded & collapsed
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
//                    isShow = true;
//                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
//                    isShow = false;
//                }
//            }
//        });
//    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {

        new ReadXML().execute(urlAlbum);

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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

            Elements totalAlbum = doc.select("div.tab-pane");
            for(int a = 0; a < totalAlbum.size(); a++) {

                Element alb = totalAlbum.get(a);
                albumList = new ArrayList<Album>();

                Elements itemList = alb.select(".item");

                for (int i = 0; i < itemList.size(); i++) {
                    Element albItem = itemList.get(i);
                    String albImgUrl = albItem.select("img").attr("src");
                    String title = albItem.select(".title-item a").text();
                    String artist = albItem.select(".txt-info").text();
                    albumList.add(new Album(albImgUrl, title, artist));
                }
                switch (a) {
                    case 0: {
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerViewViet.setLayoutManager(mLayoutManager);
                        //recyclerViewViet.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                        recyclerViewViet.setItemAnimator(new DefaultItemAnimator());
                        adapter = new AlbumsAdapter(getContext(), albumList);
                        adapter.notifyDataSetChanged();
                        recyclerViewViet.setAdapter(adapter);
                    }

                    case 1:{
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerViewUs.setLayoutManager(mLayoutManager);
                        //recyclerViewUs.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                        recyclerViewUs.setItemAnimator(new DefaultItemAnimator());
                        adapter = new AlbumsAdapter(getContext(), albumList);
                        adapter.notifyDataSetChanged();
                        recyclerViewUs.setAdapter(adapter);
                    }
                    case 2:{
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerViewKorean.setLayoutManager(mLayoutManager);
                        //recyclerViewKorean.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                        recyclerViewKorean.setItemAnimator(new DefaultItemAnimator());
                        adapter = new AlbumsAdapter(getContext(), albumList);
                        adapter.notifyDataSetChanged();
                        recyclerViewKorean.setAdapter(adapter);
                    }

                    case 3:{
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerViewHoaTau.setLayoutManager(mLayoutManager);
                        //recyclerViewHoaTau.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                        recyclerViewHoaTau.setItemAnimator(new DefaultItemAnimator());
                        adapter = new AlbumsAdapter(getContext(), albumList);
                        adapter.notifyDataSetChanged();
                        recyclerViewHoaTau.setAdapter(adapter);
                    }
                }
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
