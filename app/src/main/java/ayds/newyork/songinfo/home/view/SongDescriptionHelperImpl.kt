package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl(): SongDescriptionHelper {

    private val songDateFactory: SongDateFactory = HomeViewInjector.songDateFactory

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
                "Release date: ${getSongDate(song.releaseDatePrecision, song.releaseDate)}"
    }

    private fun getSongDate(releaseDatePrecision: String, releaseDate: String): String {
        return songDateFactory.get(releaseDatePrecision).getSongDate(releaseDate)
    }

}