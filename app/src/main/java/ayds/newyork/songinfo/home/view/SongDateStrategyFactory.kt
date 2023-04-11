package ayds.newyork.songinfo.home.view

object SongDateStrategyFactory {

    fun get(releaseDatePrecision: String): SongDateStrategy {
        return when (releaseDatePrecision) {
            "day" -> DaySongDateStrategy()
            "month" -> MonthSongDateStrategy()
            "year" -> YearSongDateStrategy()
            else -> EmptySongDateStrategy()
        }
    }

}