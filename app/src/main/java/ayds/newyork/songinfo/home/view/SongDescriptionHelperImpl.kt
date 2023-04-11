package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${datePrecisionYear(song)}"
            else -> "Song not found"
        }
    }
    private fun datePrecisionYear(song: SpotifySong): String {
        var release_date = song.releaseDate
        var release_date_precision = song.releaseDatePrecision

        return when {
            release_date_precision == "day" -> "99/99/9999"
            release_date_precision == "month" -> "MMMM, YYYY"
            release_date_precision == "year" -> "YYYY (not a leap year)"
            else -> ""
        }

    }
}