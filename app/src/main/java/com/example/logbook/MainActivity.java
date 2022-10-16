package com.example.logbook;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String MY_URL_STRING = "https://a1.espncdn.com/combiner/i?img=%2Fi%2Fleaguelogos%2Fsoccer%2F500%2F23.png";

        imgView = findViewById(R.id.img_View);


        new LoadImageTask(imgView)
                .execute(MY_URL_STRING);



    }


    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;


        public LoadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... url) {
            String imageUrl = url[0];
            Bitmap imageBitmap = null;
            try {
                InputStream in = new java.net.URL(imageUrl).openStream();
                imageBitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return imageBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }




}

