package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {


    companion object {

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String, info: String) {
            val db = dbHelper.writableDatabase
            val values = buildValues(artist, info)
            db.insert("artists", null, values)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = dbHelper.readableDatabase
            val projection = arrayOf(
                "id", "artist", "info"
            )
            val selection = "artist = ?"
            val selectionArgs = arrayOf(artist)
            val sortOrder = "artist DESC"
            val cursor = db.query(
                "artists", projection, selection, selectionArgs, null, null, sortOrder
            )

            val items: MutableList<String> = mutableListOf()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow("info")
                )
                items.add(info)
            }
            cursor.close()

            return when {
                items.isEmpty() -> null
                else -> items[0]
            }
        }

        private fun buildValues(artist: String, info: String): ContentValues {
            return ContentValues().apply {
                put("artist", artist)
                put("info", info)
                put("source", 1)
            }
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
