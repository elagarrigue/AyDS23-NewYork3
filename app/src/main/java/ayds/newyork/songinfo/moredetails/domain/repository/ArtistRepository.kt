package ayds.newyork.songinfo.moredetails.domain.repository

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface ArtistRepository {
    fun searchArtistInfo(artist: String): List<Card?>?
}