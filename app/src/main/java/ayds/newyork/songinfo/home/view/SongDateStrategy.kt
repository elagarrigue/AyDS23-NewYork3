package ayds.newyork.songinfo.home.view

interface SongDateStrategy {

    fun getSongDate(releaseDate: String): String

}

internal class DaySongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        return ""
    }

}

internal class MonthSongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        return ""
    }

}

internal class YearSongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        val anio = releaseDate.split("-")[0].toInt()
        val bisiesto = esBisiesto(anio) // Check if the year is a leap year using the esBisiesto function

        // Construct the result string with the year and leap year status
        val result = if (bisiesto) {
            "$anio (leap year)"
        } else {
            "$anio (not a leap year)"
        }

        return result
    }

    private fun esBisiesto(anio: Int): Boolean {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0)
    }

}