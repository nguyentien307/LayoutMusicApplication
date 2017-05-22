package com.example.tiennguyen.layoutmusicapplication;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiennguyen.layoutmusicapplication.adapter.ViewPagerAdapter;
import com.example.tiennguyen.layoutmusicapplication.framentplayer.FragmentLyric;
import com.example.tiennguyen.layoutmusicapplication.framentplayer.FragmentPlayer;
import com.example.tiennguyen.layoutmusicapplication.framentplayer.FragmentSongList;
import com.example.tiennguyen.layoutmusicapplication.utility.Utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by Quyen Hua on 5/19/2017.
 */

public class ActivityPlayer extends FragmentActivity{
    private ViewPager vpPlayer;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    private ImageButton imgPlay, imgPause, imgNext, imgPre, imgShuffle, imgRepeat, imgBack;
    private TextView tvName, tvTimeStart, tvTimeEnd, tvSinger;
    private SeekBar sbTime;

    private ArrayList<String> arraySong;
    private ArrayList<Song> arrItemSong;
    private List<Fragment> fragmentList = new Vector<>();

    private int loadSeekBar = 0;
    private double timeStart = 0;
    private double timeEnd = 0;

    public final String URL_SONG = "http://mp3.zing.vn/html5/song/";
    public final String URL_SOURCE = "http://mp3.zing.vn/xml/song-xml/";
    public final String TITLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler myHandler = new Handler();
    private Utility utility = new Utility();

    private XMLDOMParser parser = new XMLDOMParser();

    private boolean firstLauch = true;

    private String fileName = "note.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initializeView();

        arrItemSong = new ArrayList<>();
        fragmentList.add(Fragment.instantiate(this, FragmentPlayer.class.getName()));
        fragmentList.add(Fragment.instantiate(this, FragmentSongList.class.getName()));
        fragmentList.add(Fragment.instantiate(this, FragmentLyric.class.getName()));

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vpPlayer.setAdapter(viewPagerAdapter);

        //tabLayout.setupWithViewPager(vpPlayer);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            arraySong = bundle.getStringArrayList("arrSong");
            arraySong.add("true");
        }

        setBundle();

        tvName.setText(arraySong.get(1));
        tvSinger.setText(arraySong.get(2));

        playMedia();
        saveDataSong(arraySong.get(0));
        loadLyrics(arraySong.get(4));
        setEvent();
    }

    private void loadLyrics(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadLyric().execute(s);
            }
        });
    }

    private void setBundle() {
        Bundle bundle1 = new Bundle();
//        Bundle lyricBundle = new Bundle();
        bundle1.putStringArrayList("itemSong", arraySong);
        fragmentList.get(0).setArguments(bundle1);
//        lyricBundle.putString("lyric", arraySong.get(4));
//        fragmentList.get(2).setArguments(lyricBundle);
    }

    private void setEvent() {
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
                playing();
            }
        });

        imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                pausing();
            }
        });

        imgNext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                double timeDB = timeStart + 5000;
                if(timeDB <= timeEnd){
                    mediaPlayer.seekTo((int) timeDB);
                }
                else{
                    mediaPlayer.seekTo((int) timeEnd);
                }
                return false;
            }
        });

        imgPre.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                double timeDB = timeStart - 5000;
//                imgPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
                if(timeDB >= 0){
                    mediaPlayer.seekTo((int) timeDB);
                }
                else{
                    mediaPlayer.seekTo(0);
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(loadSeekBar);
            }
        });

//        imgInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog dialog = new Dialog(Player.this);
//                dialog.setTitle("Infomation");
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.dialog_infomation);
//                ImageView imgBackground = (ImageView) dialog.findViewById(R.id.imgBackground);
//                TextView textName = (TextView) dialog.findViewById(R.id.tvName);
//                TextView tvArtist = (TextView) dialog.findViewById(R.id.tvLinkArtist);
//                TextView tvMv = (TextView) dialog.findViewById(R.id.tvLinkMv);
//                Picasso.with(Player.this).load(arraySong.get(5)).placeholder(R.drawable.demo)
//                        .error(R.drawable.demo).into(imgBackground, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });
//                textName.setText(arraySong.get(1));
//                tvArtist.setText(arraySong.get(2));
//
//                tvArtist.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(Player.this, "link ca sĩ", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                tvMv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(Player.this, "link MV", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                //tvMv.setText(arraySong.get(6));
//                dialog.show();
//            }
//        });
    }

    private void saveDataSong(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadDataSong().execute(URL_SOURCE + arraySong.get(0));
            }
        });
    }

//    @Override
//    public void changeText(String text) {
//        Bundle bundle1 = new Bundle();
//        Bundle lyricBundle = new Bundle();
//        bundle1.putStringArrayList("itemSong", arraySong);
//        fragmentList.get(0).setArguments(bundle1);
//        lyricBundle.putString("lyric", arraySong.get(3));
//        fragmentList.get(2).setArguments(lyricBundle);
//    }

    class LoadLyric extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url = LoadDataFromURL(strings[0]);
            return url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Bundle lyricBundle = new Bundle();
            lyricBundle.putString("lyric", s);
            fragmentList.get(2).setArguments(lyricBundle);
        }
    }

    class LoadDataSong extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = LoadDataFromURL(strings[0]);
            return url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            saveData(s);
            String news = readData();

//            Bundle fragBundle = new Bundle();
//            fragBundle.putString("arrNew", news);
//            fragmentList.get(1).setArguments(fragBundle);

//            Document doc = parser.getDocument(news);
//
//            //Toast.makeText(Player.this, doc.getTextContent(), Toast.LENGTH_SHORT).show();
//
//            NodeList item = doc.getElementsByTagName("item");
//            for(int i = 0; i < item.getLength(); i++){
//                Element title = (Element) item.item(i);
//                String tsong = parser.getValueData(title, "title");
//                String asong = parser.getValueData(title, "performer");
//                String url = parser.getValueData(title, "source");
//                String lsong = parser.getValueData(title, "lyric");
//                String bsong = parser.getValueData(title, "backimage");
//                String mvsong = parser.getValueData(title, "mv");
//                String arsong = parser.getValueData(title, "link");
//                arrItemSong.add(i, new Song(tsong, asong, url, lsong, bsong, mvsong, arsong));
//            }
        }
    }

//    private void askPermissionAndWriteFile(String data) {
//        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        //
//        if (canWrite) {
//            this.writeFile(data);
//        }
//    }
//
//    private void writeFile(String data) {
//        // Thư mục gốc của SD Card.
//        File extStore = Environment.getExternalStorageDirectory();
//        // ==> /storage/emulated/0/note.txt
//        String path = extStore.getAbsolutePath() + "/" + fileName;
//        Log.i("ExternalStorageDemo", "Save to: " + path);
//
//        try {
//            File myFile = new File(path);
//            myFile.createNewFile();
//            FileOutputStream fOut = new FileOutputStream(myFile);
//            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//            myOutWriter.append(data);
//            myOutWriter.close();
//            fOut.close();
//
//            Toast.makeText(getApplicationContext(), fileName + " saved", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void readDataFromExternalStorage() {
//
//        String data = askPermissionAndReadFile();
//        //Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
//        Document doc = parser.getDocument(data);
//
//    }
//
//    private String askPermissionAndReadFile() {
//        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
//                Manifest.permission.READ_EXTERNAL_STORAGE);
//        String data = "";
//        if (canRead) {
//            data = this.readFile();
//        }
//        return data;
//    }
//
//    private String readFile() {
//        // Thư mục gốc của SD Card.
//        File extStore = Environment.getExternalStorageDirectory();
//        // ==> /storage/emulated/0/note.txt
//        String path = extStore.getAbsolutePath() + "/" + fileName;
//        Log.i("ExternalStorageDemo", "Read file: " + path);
//
//        String s = "";
//        String fileContent = "";
//        try {
//            File myFile = new File(path);
//            FileInputStream fIn = new FileInputStream(myFile);
//            BufferedReader myReader = new BufferedReader(
//                    new InputStreamReader(fIn));
//
//            while ((s = myReader.readLine()) != null) {
//                fileContent += s + "\n";
//            }
//            myReader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileContent;
//    }
//
//    private boolean askPermission(int requestId, String permissionName) {
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//
//            // Kiểm tra quyền
//            int permission = ActivityCompat.checkSelfPermission(this, permissionName);
//
//
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//
//                // Nếu không có quyền, cần nhắc người dùng cho phép.
//                this.requestPermissions(
//                        new String[]{permissionName},
//                        requestId
//                );
//                return false;
//            }
//        }
//        return true;
//    }

    private String readData() {
        StringBuilder builder = new StringBuilder();
        String content = "";
        try {
            FileInputStream input = this.openFileInput(fileName);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            builder.append(TITLE + '\n' + "<data>" + '\n');
            while ((content = buffer.readLine()) != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if(!Objects.equals(content, TITLE) && !Objects.equals(content, "<data>") && !Objects.equals(content, "</data>")) {
                        builder.append(content).append('\n');
                    }
                }
            }
            builder.append("</data>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error1:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error2:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return builder.toString();
    }

    private void saveData(String s) {
//        org.w3c.dom.Document doc = parser.getDocument(s);
//
//        String title = parser.getValue(doc, "title");
//        String artist = parser.getValue(doc, "performer");
//        String url = parser.getValue(doc, "source");
//        String lyric = parser.getValue(doc, "lyric");
//        String backimage = parser.getValue(doc, "backimage");
//        if(backimage == "") {
//            backimage = "http://1.bp.blogspot.com/-3oZ7k46VeG4/Vk0d1nmNODI/AAAAAAAAC20/3B1E5A8BDC4/s1600/hinh-anh-dep-ve-dong-vat..jpg";
//        }
//        String mv = parser.getValue(doc, "mv");
//        String link = parser.getValue(doc, "link");
//
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        Document sdoc;
//        try {
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            sdoc = db.newDocument();
//            Node data = sdoc.createElement("data");
//            sdoc.appendChild(data);
//
//            Node item = sdoc.createAttribute("item");
//            data.appendChild(item);
//
//            Node ntitle = sdoc.createElement("title");
//            ntitle.setTextContent(title);
//            item.appendChild(ntitle);
//
//            Node nperformer = sdoc.createElement("performer");
//            nperformer.setTextContent(artist);
//            item.appendChild(nperformer);
//
//            Node nsource = sdoc.createElement("source");
//            nsource.setTextContent(url);
//            item.appendChild(nsource);
//
//            Node nlyric = sdoc.createElement("lyric");
//            nlyric.setTextContent(lyric);
//            item.appendChild(nlyric);
//
//            Node nbackground = sdoc.createElement("background");
//            nbackground.setTextContent(backimage);
//            item.appendChild(nbackground);
//
//            Node nmv = sdoc.createElement("mv");
//            nmv.setTextContent(mv);
//            item.appendChild(nmv);
//
//            Node nlink = sdoc.createElement("link");
//            nlink.setTextContent(link);
//            item.appendChild(nlink);
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
        try {
            FileOutputStream output = this.openFileOutput(fileName, MODE_APPEND);
            output.write(s.getBytes());
            output.close();
            //Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error1:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error2:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeView(){
        vpPlayer = (ViewPager) findViewById(R.id.vpPlayer);
        imgPause = (ImageButton)findViewById(R.id.imgPause);
        imgPlay = (ImageButton)findViewById(R.id.imgPlay);
        imgNext = (ImageButton)findViewById(R.id.imgNext);
        imgPre = (ImageButton)findViewById(R.id.imgPre);
        imgShuffle = (ImageButton) findViewById(R.id.imgShuffle);
        imgRepeat = (ImageButton) findViewById(R.id.imgRepeat);
        imgBack = (ImageButton) findViewById(R.id.imgBack);
        tvName = (TextView)findViewById(R.id.tvName);
        sbTime = (SeekBar)findViewById(R.id.sbTime);
        tvTimeStart = (TextView)findViewById(R.id.tvTimeCurrent);
        tvTimeEnd = (TextView)findViewById(R.id.tvTimeEnd);
        tvSinger = (TextView)findViewById(R.id.tvSinger);
    }

    private void playMedia(){
        firstLauch = false;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(ActivityPlayer.this, PowerManager.PARTIAL_WAKE_LOCK);
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(arraySong.get(3));
        } catch (IOException e) {
            try {
                mediaPlayer.setDataSource(URL_SONG + arraySong.get(0));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //playCircle();
                //imgPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
                playing();
                mediaPlayer.start();
                timeEnd = mediaPlayer.getDuration();
                timeStart = mediaPlayer.getCurrentPosition();
                if(loadSeekBar == 0){
                    sbTime.setMax((int) timeEnd);
                    loadSeekBar = 1;
                }

                myHandler.postDelayed(UpdateTime, 100);
                tvTimeEnd.setText(utility.convertDuration((long) timeEnd));
            }
        });
    }

    private void pausing() {
//        arraySong.set(8, "false");
//        fragmentList.get(0).setArguments(bundle1);
        imgPause.setEnabled(false);
        imgPause.setVisibility(View.INVISIBLE);
        imgPlay.setEnabled(true);
        imgPlay.setVisibility(View.VISIBLE);
    }

    private void playing() {
//        arraySong.set(8, "true");
//        fragmentList.get(0).setArguments(bundle1);
        imgPlay.setEnabled(false);
        imgPlay.setVisibility(View.INVISIBLE);
        imgPause.setEnabled(true);
        imgPause.setVisibility(View.VISIBLE);
    }

    private Runnable UpdateTime = new Runnable() {
        @Override
        public void run() {
            timeStart = mediaPlayer.getCurrentPosition();
            tvTimeStart.setText(utility.convertDuration((long) timeStart));
            myHandler.postDelayed(this, 100);
            sbTime.setProgress((int) timeStart);
            if(utility.convertDuration((long) timeEnd).equals(utility.convertDuration((long) timeStart))){
                pausing();
            }
        }
    };

//    private void playCircle(){
//        if(mediaPlayer.isPlaying()){
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    playCircle();
//                }
//            };
//            handler.postDelayed(runnable, 100);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //mediaPlayer.start();
        //playCircle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mediaPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public static String LoadDataFromURL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            Log.d("URL", String.valueOf(url));
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
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
