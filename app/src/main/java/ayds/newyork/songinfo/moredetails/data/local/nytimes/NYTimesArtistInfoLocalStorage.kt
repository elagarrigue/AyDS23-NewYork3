package ayds.newyork.songinfo.moredetails.data.local.nytimes

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo

interface NYTimesArtistInfoLocalStorage {

    fun insertArtistInfo(artistInfo: NYTArtistInfo)

    fun getArtistInfo(artist: String): NYTArtistInfo?
}