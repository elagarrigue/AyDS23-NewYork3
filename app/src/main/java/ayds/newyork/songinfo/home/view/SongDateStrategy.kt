package ayds.newyork.songinfo.home.view

interface SongDateStrategy {

    fun getSongDate(releaseDate: String): String

}

internal class DaySongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        TODO("Not yet implemented")
    }

}

internal class MonthSongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        TODO("Not yet implemented")
    }

}

internal class YearSongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        TODO("Not yet implemented")
    }

    private fun esBisiesto(anio: Int): Boolean {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0)
    }

}