package com.example.logbook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logbook.Model.MyImage;
import com.example.logbook.SQLite.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    private TextView tvCurrent;
    private ImageView imgView;
    private Button btnPrevious;
    private Button btnForward;
    private Button btnAdd;
    private EditText edtUrl;
    private Button btnLaunchCamera;

    List<MyImage> images;
    int currentImage;
    int maxImage;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

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
        btnLaunchCamera = findViewById(R.id.btn_launch_camera);

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

        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        activityResultLauncher.launch(intent);
                    }
                }
            }
        });

        loadData();
    }



    private void onTakePicture(Bitmap photo){
        //convert photo bitmap to bytes array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG,100, stream);
        byte [] b = stream.toByteArray();

        //convert bytes array to string
        String stringConverted = Base64.encodeToString(b, Base64.DEFAULT);

        //add to database
        databaseHelper.insertImage("", stringConverted, "", "");
        int imageCount = databaseHelper.getImages().size();
        databaseHelper.updateCurrentImage(imageCount-1);

        Toast.makeText( this, "Add photo successful!", Toast.LENGTH_SHORT).show();
    }


    private void loadData(){
        images = databaseHelper.getImages();
        maxImage = images.size();
        currentImage = databaseHelper.getCurrentImage();

        if(currentImage < maxImage){
            if(images.get(currentImage).getUrl().equals("")){
                String stringConverted = images.get(currentImage).getBitmap();
                String uri = convertToUri(this, stringConverted);
                loadImage(uri);
            }
            else{
                loadImage(images.get(currentImage).getUrl());
            }
        }
    }

    public String convertToUri(Context inContext, String stringInDataBase) {

        byte [] encodeByte = Base64.decode(stringInDataBase,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String uri = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmap, "", null);
        return uri;
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

    ActivityResultLauncher<Intent> activityResultLauncher = (ActivityResultLauncher<Intent>) registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        onTakePicture(photo);
                        loadData();
                    }

                }
            }
    );

}

