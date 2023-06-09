package ayds.newyork.songinfo.moredetails.data.external.proxies

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface Proxy {
    fun getArtistInfo(artist: String): Card?
}