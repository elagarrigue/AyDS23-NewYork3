package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface Proxy {
    fun getArtistInfo(artist: String): Card?
}