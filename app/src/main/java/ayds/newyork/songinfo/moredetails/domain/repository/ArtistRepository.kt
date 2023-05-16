package ayds.newyork.songinfo.moredetails.domain.repository

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo

interface ArtistRepository {
    fun searchArtistInfo(artist: String): ArtistInfo
}