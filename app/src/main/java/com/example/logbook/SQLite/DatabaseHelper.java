package com.example.logbook.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.logbook.Model.MyImage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    private static final String DATABASE_NAME = "test";
    private static final int DATABASE_VERSION = 8;

    private static final String TABLE_IMAGES = "images";
    private static final String IMAGE_ID = "image_id";
    private static final String IMAGE_URL = "image_url";
    private static final String IMAGE_BITMAP = "image_bitmap";
    private static final String IMAGE_PATH = "image_path";
    private static final String IMAGE_LOCATION = "image_location";

    private static final String TABLE_CURRENT = "current";
    private static final String CURRENT_ID = "image_id";

    private static final String CREATE_TABLE_IMAGES = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE_IMAGES, IMAGE_ID, IMAGE_URL, IMAGE_BITMAP, IMAGE_PATH, IMAGE_LOCATION
    );

    private static final String CREATE_TABLE_CURRENT = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER)",
            TABLE_CURRENT, CURRENT_ID
    );

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGES);
        sqLiteDatabase.execSQL(CREATE_TABLE_CURRENT);

        insertImageOnCreate(sqLiteDatabase, "https://a1.espncdn.com/combiner/i?img=%2Fi%2Fleaguelogos%2Fsoccer%2F500%2F23.png", "", "", "");
        insertImageOnCreate(sqLiteDatabase, "https://media.istockphoto.com/photos/wild-grass-in-the-mountains-at-sunset-picture-id1322277517?k=20&m=1322277517&s=612x612&w=0&h=ZdxT3aGDGLsOAn3mILBS6FD7ARonKRHe_EKKa-V-Hws=", "", "", "");
        insertImageOnCreate(sqLiteDatabase, "https://www.w3schools.com/w3css/img_lights.jpg", "", "", "");

        insertCurrentImageOnCreate(sqLiteDatabase);

    }

    public long insertImageOnCreate(SQLiteDatabase sqLiteDatabase, String url, String bitmap, String path,  String location){
        ContentValues rowValues = new ContentValues();

        rowValues.put(IMAGE_URL, url);
        rowValues.put(IMAGE_PATH, bitmap);
        rowValues.put(IMAGE_BITMAP, path);
        rowValues.put(IMAGE_LOCATION, location);

        return sqLiteDatabase.insertOrThrow(TABLE_IMAGES, null, rowValues);
    }

    public long insertCurrentImageOnCreate(SQLiteDatabase sqLiteDatabase){
        ContentValues rowValues = new ContentValues();

        rowValues.put(CURRENT_ID, 0);


        return sqLiteDatabase.insertOrThrow(TABLE_CURRENT, null, rowValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

        Log.v(this.getClass().getName(), TABLE_IMAGES +
                "database upgrade to version" + newVersion + " - old data lost"
        );

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT);

        Log.v(this.getClass().getName(), TABLE_CURRENT +
                "database upgrade to version" + newVersion + " - old data lost"
        );

        onCreate(db);
    }

    public List<MyImage> getImages(){
        List<MyImage> myImages = new ArrayList<>();

        Cursor results = database.query(TABLE_IMAGES,
                new String[]{IMAGE_ID, IMAGE_URL, IMAGE_BITMAP, IMAGE_PATH, IMAGE_LOCATION},
                null, null, null, null, IMAGE_ID
        );

        results.moveToFirst();
        while(!results.isAfterLast()){
            int id = results.getInt(0);
            String url = results.getString(1);
            String bitmap = results.getString(2);
            String path = results.getString(3);
            String location = results.getString(4);

            MyImage myImage = new MyImage(id, url, bitmap, path, location);
            myImages.add(myImage);

            results.moveToNext();
        }
        return myImages;
    }

    public long insertImage(String url, String bitmap, String path,  String location){
        ContentValues rowValues = new ContentValues();

        rowValues.put(IMAGE_URL, url);
        rowValues.put(IMAGE_PATH, bitmap);
        rowValues.put(IMAGE_BITMAP, path);
        rowValues.put(IMAGE_LOCATION, location);

        return database.insertOrThrow(TABLE_IMAGES, null, rowValues);
    }

    public void deleteImage(MyImage image){
        String DATABASE_DELETE = String.format("DELETE FROM %s  WHERE %s = %s;",
                TABLE_IMAGES, IMAGE_ID, image.getId());
        database.execSQL(DATABASE_DELETE);
    }

    public void deleteAllImages(){
        String DATABASE_DELETE_ALL = String.format("DELETE FROM %s;", TABLE_IMAGES);
        database.execSQL(DATABASE_DELETE_ALL);
    }


    public int getCurrentImage(){
        int currentImage = 0;

        Cursor results = database.query(TABLE_CURRENT,
                new String[]{CURRENT_ID},
                null, null, null, null, IMAGE_ID
        );

        results.moveToFirst();
        while(!results.isAfterLast()){
            currentImage = results.getInt(0);


            results.moveToNext();
        }
        return currentImage;
    }

    public long insertCurrentImage(int pos){
        ContentValues rowValues = new ContentValues();

        rowValues.put(CURRENT_ID, pos);


        return database.insertOrThrow(TABLE_CURRENT, null, rowValues);
    }

    public void updateCurrentImage(int pos){

        String DATABASE_UPDATE = String.format("UPDATE %s SET %s = '%s'",
                TABLE_CURRENT,
                CURRENT_ID, pos);

        database.execSQL(DATABASE_UPDATE);
    }

    public void deleteCurrent(){
        String DATABASE_DELETE_ALL = String.format("DELETE FROM %s;", TABLE_CURRENT);
        database.execSQL(DATABASE_DELETE_ALL);
    }

}
