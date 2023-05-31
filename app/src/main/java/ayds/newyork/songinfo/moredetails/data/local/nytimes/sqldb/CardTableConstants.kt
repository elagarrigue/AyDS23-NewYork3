package ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "artistInfo.db"
const val TABLE_NAME = "artists"
const val COLUMN_ID = "id"
const val COLUMN_NAME = "artistName"
const val COLUMN_DESCRIPTION = "description"
const val COLUMN_INFO_URL = "infoUrl"
const val COLUMN_SOURCE = "source"
const val COLUMN_SOURCE_LOGO = "sourceLogo"
const val NYTIMES_SOURCE = 1
const val SELECTION_FILTER = "$COLUMN_NAME = ?"
const val SELECTION_ORDER = "$COLUMN_NAME desc"
const val DATABASE_CREATION_QUERY: String =
    "create table $TABLE_NAME (" +
            "$COLUMN_ID integer PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_NAME string, " +
            "$COLUMN_DESCRIPTION string, " +
            "$COLUMN_INFO_URL string, " +
            "$COLUMN_SOURCE integer, " +
            "$COLUMN_SOURCE_LOGO string)"