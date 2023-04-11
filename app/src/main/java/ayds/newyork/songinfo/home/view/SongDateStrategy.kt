package ayds.newyork.songinfo.home.view

interface SongDateStrategy {

    fun getSongDate(releaseDate: String): String

}

internal class DaySongDateStrategy: SongDateStrategy {

    override fun getSongDate(releaseDate: String): String {
        val delim = "-"
        val arr = releaseDate.split(delim).toTypedArray()
        val day = arr.get(2)
        val month = arr.get(1)
        val year = arr.get(0)
        return "$day/$month/$year"
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
        val year = releaseDate.split("-")[0].toInt()
        val leap = isLeap(year)

        val result = if (leap) {
            "$year (leap year)"
        } else {
            "$year (not a leap year)"
        }

        return result
    }

    private fun isLeap(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

}

internal class EmptySongDateStrategy: SongDateStrategy {
    override fun getSongDate(releaseDate: String): String {
        return ""
    }
}