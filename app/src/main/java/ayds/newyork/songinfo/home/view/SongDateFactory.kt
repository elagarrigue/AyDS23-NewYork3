package ayds.newyork.songinfo.home.view

interface SongDateFactory {
    fun get(releaseDatePrecision: String): SongDateStrategy
}

object SongDateFactoryImpl: SongDateFactory {

    override fun get(releaseDatePrecision: String): SongDateStrategy {
        return when (releaseDatePrecision) {
            "day" -> DaySongDateStrategy()
            "month" -> MonthSongDateStrategy()
            "year" -> YearSongDateStrategy()
            else -> EmptySongDateStrategy()
        }
    }

}