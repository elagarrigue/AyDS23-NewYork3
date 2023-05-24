package ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface CursorToCardMapper {
    fun map(cursor: Cursor): Card?
}

internal class CursorToCardMapperImpl : CursorToCardMapper {
    override fun map(cursor: Cursor): Card? =
        try {
            with(cursor) {
                if (moveToFirst()) {
                    Card(
                        description = cursor.getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        infoUrl = cursor.getString(getColumnIndexOrThrow(COLUMN_INFO_URL)),
                        source = cursor.getString(getColumnIndexOrThrow(COLUMN_SOURCE)),
                        sourceLogoUrl = cursor.getString(getColumnIndexOrThrow(COLUMN_SOURCE_LOGO)),
                    )
                } else null
            }
        } catch (err: IllegalArgumentException) {
            err.printStackTrace()
            null
        }
}