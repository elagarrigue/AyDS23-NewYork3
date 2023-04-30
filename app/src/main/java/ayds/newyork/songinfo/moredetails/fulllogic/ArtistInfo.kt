package ayds.newyork.songinfo.moredetails.fulllogic

data class ArtistInfo(
    val abstract: String,
    val url: String,
    var isLocallyStored: Boolean = false
)
