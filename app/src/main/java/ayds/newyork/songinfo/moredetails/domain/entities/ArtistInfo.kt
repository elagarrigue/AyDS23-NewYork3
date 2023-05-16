package ayds.newyork.songinfo.moredetails.domain.entities

sealed class ArtistInfo {

    data class NYTArtistInfo(
        val artist: String,
        var abstract: String,
        val url: String,
        var isLocallyStored: Boolean = false
    ) : ArtistInfo()

    object EmptyArtistInfo : ArtistInfo()

}