package ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source

interface CursorToCardMapper {
    fun map(cursor: Cursor): Card?
}

internal class CursorToCardMapperImpl: CursorToCardMapper {
    override fun map(cursor: Cursor): Card? =
        try {
            with(cursor) {
                    Card(
                        artistName= cursor.getString(getColumnIndexOrThrow(COLUMN_NAME)),
                        description = cursor.getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        infoUrl = cursor.getString(getColumnIndexOrThrow(COLUMN_INFO_URL)),
                        source = Source.values()[cursor.getString(getColumnIndexOrThrow(COLUMN_SOURCE)).toInt()],
                        sourceLogoUrl = cursor.getString(getColumnIndexOrThrow(COLUMN_SOURCE_LOGO)),
                    )
            }
        } catch (err: IllegalArgumentException) {
            err.printStackTrace()
            null
        }
}