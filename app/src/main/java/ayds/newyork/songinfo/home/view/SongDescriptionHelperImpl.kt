package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl(private val songDateFactory: SongDateFactory): SongDescriptionHelper {

    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong -> buildSongDescriptionText(song)
            else -> "Song not found"
        }
    }

    private fun buildSongDescriptionText(song: SpotifySong): String {
        return "${
            "Song: ${song.songName} " +
                    if (song.isLocallyStored) "[*]" else ""
        }\n" +
                "Artist: ${song.artistName}\n" +
                "Album: ${song.albumName}\n" +
                "Release date: ${songDateFactory.get(song.releaseDatePrecision).getSongDate(song.releaseDate)}"
    }
}