package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val TABLE_NAME = "artists"
private const val COLUMN_ID = "id"
private const val COLUMN_ARTIST = "artist"
private const val COLUMN_SOURCE = "source"
private const val COLUMN_INFO = "info"
private const val NYTIMES_SOURCE = 1
private const val SELECTION_FILTER = "$COLUMN_ARTIST = ?"
private const val SELECTION_ORDER = "$COLUMN_ARTIST desc"

class DataBase(context: Context) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    fun saveArtist(artist: String, info: String) {
        val values = ContentValues().apply {
            put(COLUMN_ARTIST, artist)
            put(COLUMN_INFO, info)
            put(COLUMN_SOURCE, NYTIMES_SOURCE)
        }

        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun getInfo(artist: String): String? {
        val artistInfo = searchArtistInfo(artist)
        return artistInfo.takeIf { it.isNotBlank() }
    }

    private fun searchArtistInfo(artist: String): String {
        val cursor = getCursor(artist)
        val artistInfo = getDataFromCursor(cursor)
        cursor.close()
        return artistInfo
    }

    private fun getDataFromCursor(cursor: Cursor): String {
        return try {
            if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO))
            } else ""
        } catch (err: IllegalArgumentException) {
            err.printStackTrace()
            ""
        }
    }

    private fun getCursor(artist: String): Cursor {
        val projection = arrayOf(
            COLUMN_ID, COLUMN_ARTIST, COLUMN_INFO
        )
        val selection = SELECTION_FILTER
        val sortOrder = SELECTION_ORDER
        val selectionArgs = arrayOf(artist)
        return this.readableDatabase.query(
            TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder
        )
    }

    @Override
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists ($COLUMN_ID integer PRIMARY KEY AUTOINCREMENT, $COLUMN_ARTIST string, $COLUMN_INFO string, $COLUMN_SOURCE integer)"
        )
    }

    @Override
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}