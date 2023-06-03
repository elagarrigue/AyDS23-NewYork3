package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.data.external.proxies.Proxy
import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface ArtistInfoBroker {
    fun getArtistInfo(artist: String): List<Card>
}

internal class ArtistInfoBrokerImpl(
    private val proxyList: List<Proxy>
): ArtistInfoBroker {

    override fun getArtistInfo(artist: String): List<Card> {
        val cardList: MutableList<Card> = mutableListOf()

        proxyList.forEach { proxy ->
            proxy.getArtistInfo(artist)?.let { cardList.add(it) }
        }

        return cardList
    }
}