package ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo

interface CursorToArtistInfoMapper {
    fun map(cursor: Cursor): ArtistInfo.NYTArtistInfo?
}

internal class CursorToArtistInfoMapperImpl : CursorToArtistInfoMapper {
    override fun map(cursor: Cursor): ArtistInfo.NYTArtistInfo? =
        try {
            with(cursor) {
                if (moveToFirst()) {
                    ArtistInfo.NYTArtistInfo(
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