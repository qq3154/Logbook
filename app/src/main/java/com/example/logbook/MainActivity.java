package com.example.logbook;

import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logbook.Model.MyImage;
import com.example.logbook.SQLite.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    private TextView tvCurrent;
    private ImageView imgView;
    private Button btnPrevious;
    private Button btnForward;
    private Button btnAdd;
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

        loadData();
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

        //load with Picasso
        Picasso.with(this).load(url).into(imgView);

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

}

