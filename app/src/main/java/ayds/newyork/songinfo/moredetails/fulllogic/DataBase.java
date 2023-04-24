package ayds.newyork.songinfo.moredetails.fulllogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    public static void saveArtist(DataBase dbHelper, String artist, String info)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("artist", artist);
        values.put("info", info);
        values.put("source", 1);

        long newRowId = db.insert("artists", null, values);
    }

    public static String getInfo(DataBase dbHelper, String artist)
    {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                "id",
                "artist",
                "info"
        };

        String selection = "artist  = ?";
        String[] selectionArgs = { artist };

        String sortOrder = "artist DESC";

        Cursor cursor = db.query(
                "artists",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<String> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            String info = cursor.getString(
                    cursor.getColumnIndexOrThrow("info"));
            items.add(info);
        }
        cursor.close();

        if(items.isEmpty()) return null;
        else return items.get(0);
    }

    public DataBase(Context context) {

        super(context, "dictionary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)");

        Log.i("DB", "DB created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
