package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoBroker
import ayds.newyork.songinfo.moredetails.data.local.nytimes.CardLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.repository.CardRepository

class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val artistInfoBroker: ArtistInfoBroker
) : CardRepository {

    override fun searchArtistInfo(artist: String): List<Card?> {
        var cards: List<Card?>?
        val cardInfo = cardLocalStorage.getArtistInfo(artist)
        when {
            cardInfo != null -> {
                markCardAsLocal(cardInfo)
                cards = cardInfo
            }
            else -> {
                try {
                    cards = artistInfoBroker.getArtistInfo(artist)
                    cards.forEach { card ->
                        card?.let {
                            cardLocalStorage.insertArtistInfo(it)
                        }
                    }
                } catch (e: Exception) {
                    cards = null
                }
            }
        }
        return emptyList()
    }

    private fun markCardAsLocal(card: List<Card>) {
        card.forEach { it.isLocallyStored = true}
    }

    private fun ayds.ny3.newyorktimes.external.NYTArtistInfo.convert(): NYTArtistInfo {
        return NYTArtistInfo(
            artist = this.artist,
            abstract = this.abstract,
            url = this.url
        )
    }
}