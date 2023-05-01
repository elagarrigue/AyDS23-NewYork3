package ayds.newyork.songinfo.moredetails.fulllogic

sealed class ArtistInfo {

    data class NYTArtistInfo(
        val artist: String,
        val abstract: String,
        val url: String,
        var isLocallyStored: Boolean = false
    ): ArtistInfo()

    object EmptyArtistInfo : ArtistInfo()
}
