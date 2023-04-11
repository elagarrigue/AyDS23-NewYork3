package ayds.newyork.songinfo.home.view

interface SongDateStrategy {

    fun getSongDate(releaseDate: String): String

}

internal class DaySongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {

    }

}

internal class MonthSongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        val monthAndYear = releaseDate.split("-")
        val monthName = when (monthAndYear[1].toInt()) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> ""
        }
        return "$monthName, ${monthAndYear[0]}"
    }

}

internal class YearSongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        TODO("Not yet implemented")
    }

}