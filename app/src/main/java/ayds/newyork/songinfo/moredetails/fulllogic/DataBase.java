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

    ContentValues artistData = new ContentValues();
    artistData.put("artist", artist);
    artistData.put("info", info);
    artistData.put("source", 1);

    long newRowId = db.insert("artists", null, artistData);
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

    String item = null;
    if (cursor.moveToFirst()) {
      item = cursor.getString(cursor.getColumnIndexOrThrow("info"));
    } else {
      item = null;
    }
    cursor.close();

    return item;
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

  public static void testDB()
  {

    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.

      //statement.executeUpdate("drop table if exists person");
      //statement.executeUpdate("create table person (id integer, name string)");
      //statement.executeUpdate("insert into person values(1, 'leo')");
      //statement.executeUpdate("insert into person values(2, 'yui')");
      ResultSet rs = statement.executeQuery("select * from artists");
      while(rs.next())
      {
        // read the result set
        System.out.println("id = " + rs.getInt("id"));
        System.out.println("artist = " + rs.getString("artist"));
        System.out.println("info = " + rs.getString("info"));
        System.out.println("source = " + rs.getString("source"));

      }
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        System.err.println(e);
      }
    }
  }
}
