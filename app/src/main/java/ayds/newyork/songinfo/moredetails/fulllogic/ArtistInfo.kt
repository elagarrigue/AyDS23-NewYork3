package ayds.newyork.songinfo.moredetails.fulllogic

data class ArtistInfo(
    val artist: String,
    var abstract: String?,
    val url: String?,
    var isLocallyStored: Boolean = false
)