package com.example.tiennguyen.layoutmusicapplication.framentplayer;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiennguyen.layoutmusicapplication.R;
import com.example.tiennguyen.layoutmusicapplication.convert.ImageConverter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Quyen Hua on 5/19/2017.
 */

public class FragmentPlayer extends Fragment {


    private CircleImageView imgDia;
    private ArrayList<String> arrItemSong;
    private ImageButton imgInfo;
    private ImageView imgBackground;
    private TextView tvLyrics;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        inititalize(view);

        Bundle bundle = getArguments();
        arrItemSong = new ArrayList<>();
        if(bundle != null) {
            arrItemSong = bundle.getStringArrayList("itemSong");
            loadBitmap(arrItemSong.get(5));
        }
        setEvent();
        return view;
    }

    private void setEvent() {
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setTitle("Infomation");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_infomation);
                imgBackground = (ImageView) dialog.findViewById(R.id.imgBackground);
                TextView textName = (TextView) dialog.findViewById(R.id.tvDialogName);
                TextView tvArtist = (TextView) dialog.findViewById(R.id.tvLinkArtist);
                TextView tvMv = (TextView) dialog.findViewById(R.id.tvLinkMv);
                new InfoImage().execute(arrItemSong.get(5));
                textName.setText(arrItemSong.get(1));
                tvArtist.setText(arrItemSong.get(2));

                tvArtist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "link ca sĩ", Toast.LENGTH_SHORT).show();
                    }
                });

                tvMv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "link MV", Toast.LENGTH_SHORT).show();
                    }
                });
                //tvMv.setText(arraySong.get(6));
                dialog.show();
            }
        });
    }

    private void loadBitmap(String linkImg){
        new LoadImage().execute(linkImg);
    }

    class LoadImage extends AsyncTask<String, Void, Bitmap>{
        Bitmap bitmap;
        InputStream in;
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                in = new URL(params[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
            imgDia.setImageBitmap(circularBitmap);
            if(arrItemSong.get(8).equals("true")) {
                Animation rotate_dish = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate_dish);
                imgDia.startAnimation(rotate_dish);
            }
        }
    };

    class InfoImage extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;
        InputStream in;
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                in = new URL(params[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
            imgBackground.setImageBitmap(circularBitmap);
        }
    };

    private void inititalize(View view) {
        imgDia = (CircleImageView) view.findViewById(R.id.imgDish);
        imgInfo = (ImageButton) view.findViewById(R.id.imgInfo);
    }
}

