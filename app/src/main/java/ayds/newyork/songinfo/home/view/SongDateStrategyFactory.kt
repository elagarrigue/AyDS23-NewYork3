package ayds.newyork.songinfo.home.view

interface SongDateStrategyFactory {
    fun get(releaseDatePrecision: String): SongDateStrategy
}

object SongDateStrategyFactoryImpl {

    fun get(releaseDatePrecision: String): SongDateStrategy {
        return when (releaseDatePrecision) {
            "day" -> DaySongDateStrategy()
            "month" -> MonthSongDateStrategy()
            "year" -> YearSongDateStrategy()
            else -> EmptySongDateStrategy()
        }
    }

}