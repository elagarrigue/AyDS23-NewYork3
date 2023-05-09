package ayds.newyork.songinfo.moredetails.data.local.nytimes

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo

interface NYTimesArtistInfoLocalStorage {

    fun insertArtistInfo(artistInfo: ArtistInfo)

    fun getArtistInfo(artist: String): ArtistInfo?
}