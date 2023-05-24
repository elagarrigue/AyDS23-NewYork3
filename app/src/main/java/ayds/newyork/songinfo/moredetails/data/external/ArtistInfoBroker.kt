package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface ArtistInfoBroker {
    fun getArtistInfo(artist:String) :List<Card?>
}

class ArtistInfoBrokerImpl(
    private val proxyLisboa: LisboaProxy,
    private val proxyNewYorkTimes: NewYorkTimesProxy,
    private val proxyWinchester:WinchesterProxy
): ArtistInfoBroker {

    override fun getArtistInfo(artist: String): List<Card?> {
        val cardList: MutableList<Card?> = mutableListOf()
        cardList.add(proxyLisboa.getArtistInfo(artist))
        cardList.add(proxyNewYorkTimes.getArtistInfo(artist))
        cardList.add(proxyWinchester.getArtistInfo(artist))

        return cardList
    }

}