package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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

    fun saveArtistInfo(artistInfo: ArtistInfo) { // TODO: modificar para que tambien se guarde la url
        val values = ContentValues().apply {
            put(COLUMN_ARTIST, artistInfo.artist)
            put(COLUMN_INFO, artistInfo.abstract)
            put(COLUMN_SOURCE, NYTIMES_SOURCE)
            put(COLUMN_URL, artistInfo.url)
        }
        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun getInfo(artist: String): ArtistInfo? {  // TODO: modificar para que retorne un objeto de tipo ArtistInfo
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
        db.execSQL(DATABASE_CREATION_QUERY)
    }

    @Override
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { }
}