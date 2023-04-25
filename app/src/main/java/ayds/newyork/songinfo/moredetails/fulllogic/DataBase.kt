package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val NYTIMES_SOURCE = 1

class DataBase(context: Context) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

        fun saveArtist(artist: String, info: String) {
            val db = this.writableDatabase

            val values = ContentValues().apply {
                put("artist", artist)
                put("info", info)
                put("source", NYTIMES_SOURCE)
            }

            db.insert("artists", null, values)
        }

        fun getInfo(artist: String): String? {
            val db = this.readableDatabase
            val item = searchArtistInfo(db, artist)

            return if (item == "") {
                null
            } else {
                item
            }
        }

        private fun searchArtistInfo(db: SQLiteDatabase, artist: String): String {
            val projection = arrayOf(
                "id", "artist", "info"
            )
            val selection = "artist = ?"
            val selectionArgs = arrayOf(artist)
            val sortOrder = "artist DESC"
            val cursor = db.query(
                "artists", projection, selection, selectionArgs, null, null, sortOrder
            )
            var item: String
            try {
                item = if (cursor.moveToFirst()) {
                    cursor.getString(cursor.getColumnIndexOrThrow("info"))
                } else ""
                cursor.close()
            } catch (err: IllegalArgumentException) {
                item = ""
            }

            return item
        }

    @Override
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
    }

    @Override
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}