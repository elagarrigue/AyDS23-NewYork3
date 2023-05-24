package ayds.newyork.songinfo.moredetails.domain.entities

sealed class Card(
    private val description: String,
    private val infoUrl: String,
    private val source: String,
    private val sourceLogoUrl: String
)