package ayds.newyork.songinfo.moredetails.data.external.nytimes

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo

interface NYTimesArtistInfoService {

    fun getArtistInfo(artist: String): NYTArtistInfo?
}