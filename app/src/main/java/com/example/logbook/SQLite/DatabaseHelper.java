package com.example.logbook.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    private static final String DATABASE_NAME = "test";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_IMAGES = "images";
    private static final String IMAGE_ID = "image_id";
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
                    "%s TEXT)",
            TABLE_IMAGES, IMAGE_ID, IMAGE_BITMAP, IMAGE_PATH, IMAGE_LOCATION
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
}
