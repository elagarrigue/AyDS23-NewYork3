package ayds.newyork.songinfo.moredetails.model.data.local.nytimes

import ayds.newyork.songinfo.moredetails.model.domain.entities.ArtistInfo

interface NYTimesArtistInfoLocalStorage {

    fun insertArtistInfo(artistName: String, artistInfo: ArtistInfo)

    fun getArtistInfo(artist: String): ArtistInfo?
}