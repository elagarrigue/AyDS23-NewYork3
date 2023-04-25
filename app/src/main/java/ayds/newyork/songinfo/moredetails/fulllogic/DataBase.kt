package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val NYTIMES_SOURCE = 1

class DataBase(context: Context) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    companion object {

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String, info: String) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("artist", artist)
                put("info", info)
                put("source", NYTIMES_SOURCE)
            }

            db.insert("artists", null, values)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = dbHelper.readableDatabase
            val items = searchArtistInfo(db, artist)

            return if (items.isEmpty()) {
                null
            } else {
                items.first()
            }
        }

        private fun searchArtistInfo(db: SQLiteDatabase, artist: String): MutableList<String> {
            val projection = arrayOf(
                "id", "artist", "info"
            )
            val selection = "artist = ?"
            val selectionArgs = arrayOf(artist)
            val sortOrder = "artist DESC"
            val items: MutableList<String> = mutableListOf()
            val cursor = db.query(
                "artists", projection, selection, selectionArgs, null, null, sortOrder
            )

            try {
                while (cursor.moveToNext()) {
                    val info = cursor.getString(
                        cursor.getColumnIndexOrThrow("info")
                    )
                    items.add(info)
                }
                cursor.close()
            } catch (err: IllegalArgumentException) {
                items.clear()
            }

            return items
        }
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