package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface WinchesterProxy {
    fun getArtistInfo(artist: String): Card?
}

class WinchesterProxyImpl(
    //TODO: nombre service
): WinchesterProxy {

    override fun getArtistInfo(artist: String): Card? {
        return null
    }
}