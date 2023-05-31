package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface ArtistInfoBroker {
    fun getArtistInfo(artist:String) :List<Card>
}

class ArtistInfoBrokerImpl(
    private val proxyLisboa: LastFMProxy,
    private val proxyNewYorkTimes: NewYorkTimesProxy,
    private val proxyWinchester:WikipediaProxy
): ArtistInfoBroker {

    override fun getArtistInfo(artist: String): List<Card> {
        val cardList: MutableList<Card> = mutableListOf()

        proxyLisboa.getArtistInfo(artist)?.let { cardList.add(it) }
        proxyNewYorkTimes.getArtistInfo(artist)?.let { cardList.add(it) }
        proxyWinchester.getArtistInfo(artist)?.let { cardList.add(it) }

        return cardList
    }

}