package ayds.newyork.songinfo.moredetails.model.data.repository.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.domain.entities.ArtistInfo

private const val TABLE_NAME = "artists"
private const val COLUMN_ID = "id"
private const val COLUMN_ARTIST = "artist"
private const val COLUMN_INFO = "info"
private const val COLUMN_SOURCE = "source"
private const val COLUMN_URL = "url"
private const val NYTIMES_SOURCE = 1
private const val SELECTION_FILTER = "$COLUMN_ARTIST = ?"
private const val SELECTION_ORDER = "$COLUMN_ARTIST desc"
private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_CREATION_QUERY = "create table $TABLE_NAME ($COLUMN_ID integer PRIMARY KEY AUTOINCREMENT, $COLUMN_ARTIST string, $COLUMN_INFO string, $COLUMN_SOURCE integer, $COLUMN_URL string)"

class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    fun saveArtistInfo(artistInfo: ArtistInfo) {
        val values = ContentValues().apply {
            put(COLUMN_ARTIST, artistInfo.artist)
            put(COLUMN_INFO, artistInfo.abstract)
            put(COLUMN_SOURCE, NYTIMES_SOURCE)
            put(COLUMN_URL, artistInfo.url)
        }
        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun getInfo(artist: String): ArtistInfo? {
        val cursor = getCursor(artist)
        val artistInfo = getDataFromCursor(cursor)
        cursor.close()
        return artistInfo
    }

    private fun getDataFromCursor(cursor: Cursor): ArtistInfo? {
        return try {
            if (cursor.moveToFirst()) {
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST))
                val info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL))
                ArtistInfo(artist, info, url)
            } else null
        } catch (err: IllegalArgumentException) {
            err.printStackTrace()
            null
        }
    }

    private fun getCursor(artist: String): Cursor {
        val projection = arrayOf(
            COLUMN_ID, COLUMN_ARTIST, COLUMN_INFO, COLUMN_URL
        )
        return this.readableDatabase.query(
            TABLE_NAME, projection, SELECTION_FILTER, arrayOf(artist), null, null, SELECTION_ORDER
        )
    }

    @Override
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_CREATION_QUERY)
    }

    @Override
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { }
}