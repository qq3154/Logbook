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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvCurrent;
    private ImageView imgView;
    private Button btnPrevious;
    private Button btnForward;
    private Button btnAdd;
    private EditText edtUrl;

    List<String> imageUrls;
    int currentImage;
    int maxImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        insertImageOnStart();

        loadData();
    }

    private void insertImageOnStart(){
        imageUrls = new ArrayList<>();
        currentImage = 0;
        maxImage = 3;
        imageUrls.add("https://a1.espncdn.com/combiner/i?img=%2Fi%2Fleaguelogos%2Fsoccer%2F500%2F23.png");
        imageUrls.add("https://media.istockphoto.com/photos/wild-grass-in-the-mountains-at-sunset-picture-id1322277517?k=20&m=1322277517&s=612x612&w=0&h=ZdxT3aGDGLsOAn3mILBS6FD7ARonKRHe_EKKa-V-Hws=");
        imageUrls.add("https://www.w3schools.com/w3css/img_lights.jpg");
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
        imageUrls.add(url);

        Toast.makeText(this, "Add image successfully!", Toast.LENGTH_SHORT).show();

        currentImage = maxImage;

        loadData();

        edtUrl.setText("");

    }


    private void loadData(){
        maxImage = imageUrls.size();

        if(currentImage < maxImage){
            loadImage(imageUrls.get(currentImage));
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
        this.currentImage = currentImage;
        loadData();
    }
}

