package ayds.newyork.songinfo.moredetails.model.data.local.nytimes.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.domain.entities.ArtistInfo

class NYTimesArtistInfoLocalStorageImpl(
    context: Context,
    private val cursorToArtistInfoMapper: CursorToArtistInfoMapper,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    private val projection = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_INFO,
        COLUMN_URL
    )

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
        val artistInfo = cursorToArtistInfoMapper.map(cursor)
        cursor.close()
        return artistInfo
    }

    private fun getCursor(artist: String): Cursor {
        return this.readableDatabase.query(
            TABLE_NAME,
            projection,
            SELECTION_FILTER,
            arrayOf(artist),
            null,
            null,
            SELECTION_ORDER
        )
    }

    @Override
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_CREATION_QUERY)
    }

    @Override
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}