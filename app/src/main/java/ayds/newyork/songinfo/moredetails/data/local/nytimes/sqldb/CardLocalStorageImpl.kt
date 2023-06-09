package ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.data.local.nytimes.CardLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.Card

internal class CardLocalStorageImpl(
    context: Context,
    private val cursorToCardMapper: CursorToCardMapper,
): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    CardLocalStorage {

    private val projection = arrayOf(
        COLUMN_NAME,
        COLUMN_DESCRIPTION,
        COLUMN_INFO_URL,
        COLUMN_SOURCE,
        COLUMN_SOURCE_LOGO
    )

    override fun insertArtistInfo(artistInfo: Card) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, artistInfo.artistName)
            put(COLUMN_DESCRIPTION, artistInfo.description)
            put(COLUMN_INFO_URL, artistInfo.infoUrl)
            put(COLUMN_SOURCE, artistInfo.source.ordinal)
            put(COLUMN_SOURCE_LOGO, artistInfo.sourceLogoUrl)
        }
        this.writableDatabase.insert(TABLE_NAME, null, values)
    }

    override fun getArtistInfo(artist: String): List<Card> {
        val cursor = getCursor(artist)
        val cards = mutableListOf<Card>()
        while (cursor.moveToNext()) {
            val artistInfo = cursorToCardMapper.map(cursor)
            artistInfo?.let { cards.add(artistInfo) }
        }
        cursor.close()
        return cards
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
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}