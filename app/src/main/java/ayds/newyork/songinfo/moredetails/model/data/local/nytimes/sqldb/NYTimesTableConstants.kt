package ayds.newyork.songinfo.moredetails.model.data.local.nytimes.sqldb

const val TABLE_NAME = "artists"
const val COLUMN_ID = "id"
const val COLUMN_ARTIST = "artist"
const val COLUMN_INFO = "info"
const val COLUMN_SOURCE = "source"
const val COLUMN_URL = "url"
const val NYTIMES_SOURCE = 1
const val SELECTION_FILTER = "$COLUMN_ARTIST = ?"
const val SELECTION_ORDER = "$COLUMN_ARTIST desc"
const val DATABASE_NAME = "dictionary.db"
const val DATABASE_CREATION_QUERY: String =
    "create table $TABLE_NAME (" +
            "$COLUMN_ID integer PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_ARTIST string, " +
            "$COLUMN_INFO string, " +
            "$COLUMN_SOURCE integer, " +
            "$COLUMN_URL string)"
