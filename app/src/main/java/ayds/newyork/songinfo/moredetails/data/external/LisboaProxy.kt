package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface LisboaProxy {
    fun getArtistInfo(artist: String): Card?
}

class LisboaProxyImpl(
    // TODO(nombre service)
): LisboaProxy {

    override fun getArtistInfo(artist: String): Card? {
        return null
    }
}