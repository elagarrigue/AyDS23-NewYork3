package ayds.newyork.songinfo.moredetails.domain.entities

data class Card(
    var description: String,
    val infoUrl: String,
    val source: Source,
    val sourceLogoUrl: String = "https://forum.hestiacp.com/uploads/default/optimized/2X/4/4d6f29ac652c55cde2faf6026c9852214fb74196_2_690x471.png",
    var isLocallyStored: Boolean = false
)

enum class Source{
    NYTimes,
    Wikipedia,
    LastFM
}