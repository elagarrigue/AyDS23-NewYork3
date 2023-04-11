package ayds.newyork.songinfo.home.view

object SongDateStrategyFactory {

    fun get(releaseDatePrecision: String) {
        when (releaseDatePrecision) {
            "day" -> DaySongDateStrategy()
            "month" -> MonthSongDateStrategy()
            "year" -> YearSongDateStrategy()
        }
    }

}