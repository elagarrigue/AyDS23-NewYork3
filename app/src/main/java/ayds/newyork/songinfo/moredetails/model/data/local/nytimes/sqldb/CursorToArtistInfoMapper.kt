package ayds.newyork.songinfo.moredetails.model.data.local.nytimes.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.model.domain.entities.ArtistInfo

interface CursorToArtistInfoMapper {
    fun map(cursor: Cursor): ArtistInfo?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {
    override fun map(cursor: Cursor): ArtistInfo? =
        try {
            with(cursor) {
                if (moveToFirst()) {
                    ArtistInfo(
                        artist = cursor.getString(getColumnIndexOrThrow(COLUMN_ARTIST)),
                        abstract = cursor.getString(getColumnIndexOrThrow(COLUMN_INFO)),
                        url = cursor.getString(getColumnIndexOrThrow(COLUMN_URL)),
                    )
                } else null
            }
        } catch (err: IllegalArgumentException) {
            err.printStackTrace()
            null
        }
}