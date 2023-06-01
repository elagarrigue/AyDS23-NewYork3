package ayds.newyork.songinfo.moredetails.data.local.nytimes

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface CardLocalStorage {

    fun insertArtistInfo(artistInfo: Card)

    fun getArtistInfo(artist: String): List<Card>?
}