package com.example.tiennguyen.layoutmusicapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.tiennguyen.layoutmusicapplication.homefrg.HomeFragment;
import com.example.tiennguyen.layoutmusicapplication.homefrg.SearchFragment;
import com.example.tiennguyen.layoutmusicapplication.settingfrg.SettingFragment;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    LinearLayout linearLayoutAlbumActivity;
    LinearLayout linearLayoutHotSongListActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //-----------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //add icon drawer menu (3 line)
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, this.drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // add fragment drawer menu
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_content, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home Fragment");


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.id_home:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, new HomeFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home");
                        item.setCheckable(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.id_settings:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, new SettingFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Setting");
                        item.setCheckable(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.id_album:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, new AlbumFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Album");
                        item.setCheckable(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.id_hot:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_content, new HotSongListFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Bài hát Hot");
                        item.setCheckable(true);
                        drawerLayout.closeDrawers();
                        break;
                }

                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activit_main_action,menu);
        MenuItem search = menu.findItem(R.id.id_search);
        SearchView edSearch = (SearchView) search.getActionView();
        edSearch.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        StringUtils utils = new StringUtils();
        String searchName = utils.unAccent(query);
        Bundle bundle = new Bundle();
        bundle.putString("searchName", searchName);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, searchFragment).commit();
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        return true;
    }

    public static class StringUtils{
        public static String unAccent(String s) {
            String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d").replaceAll(" ", "+");
        }
    }

    public  void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}
