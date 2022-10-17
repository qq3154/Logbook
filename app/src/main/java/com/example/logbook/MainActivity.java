package com.example.logbook;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logbook.Model.MyImage;
import com.example.logbook.SQLite.DatabaseHelper;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    private TextView tvCurrent;
    private ImageView imgView;
    private Button btnPrevious;
    private Button btnForward;
    private Button btnAdd;
    private  Button btnDelete;
    private EditText edtUrl;

    List<MyImage> images;
    int currentImage;
    int maxImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        tvCurrent = findViewById(R.id.tv_current);
        imgView = findViewById(R.id.img_View);
        btnPrevious = findViewById(R.id.btn_previous);
        btnForward = findViewById(R.id.btn_forward);
        btnAdd = findViewById(R.id.btn_add);
        edtUrl = findViewById(R.id.edt_url);
        btnDelete = findViewById(R.id.btn_delete);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImage((currentImage + maxImage -1) % maxImage);
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextImage((currentImage + maxImage + 1) % maxImage);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage();
            }
        });

        //firstOpen();

        loadData();
    }


    private void firstOpen(){

        databaseHelper.deleteAllImages();
        databaseHelper.deleteCurrent();

        Long image1 = databaseHelper.insertImage( "https://a1.espncdn.com/combiner/i?img=%2Fi%2Fleaguelogos%2Fsoccer%2F500%2F23.png",
                "",
                "",
                "");

        Long image2 = databaseHelper.insertImage( "https://media.istockphoto.com/photos/wild-grass-in-the-mountains-at-sunset-picture-id1322277517?k=20&m=1322277517&s=612x612&w=0&h=ZdxT3aGDGLsOAn3mILBS6FD7ARonKRHe_EKKa-V-Hws=",
                "",
                "",
                "");


        Long image3 = databaseHelper.insertImage( "https://www.w3schools.com/w3css/img_lights.jpg",
                "",
                "",
                "");

        databaseHelper.insertCurrentImage(0);
    }

    private void loadData(){
        images = databaseHelper.getImages();
        maxImage = images.size();
        currentImage = databaseHelper.getCurrentImage();

        if(currentImage < maxImage){
            loadImage(images.get(currentImage).getUrl());
        }
    }

    private void loadImage(String url){
        new LoadImageTask(imgView)
                .execute(url);

        int currentPos = currentImage + 1;
        String currentImageStr = String.format("%s" +"/"+ "%s", currentPos, maxImage);
        tvCurrent.setText(currentImageStr);
    }

    private void nextImage(int currentImage) {
        databaseHelper.updateCurrentImage(currentImage);
        loadData();
    }

    private void addImage() {
        String url = edtUrl.getText().toString().trim();

        if(url == null || url.length() == 0){
            Toast.makeText(this, "Please enter URL!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!URLUtil.isValidUrl(url)){
            Toast.makeText(this, "Invalid URL!", Toast.LENGTH_SHORT).show();
            return;
        }

        //we can add image here
        Long image = databaseHelper.insertImage( url,
                "",
                "",
                "");

        databaseHelper.updateCurrentImage(maxImage);
        loadData();

        edtUrl.setText("");
    }

    private void deleteImage(){
        //databaseHelper.deleteImage(images.get(currentImage));

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

