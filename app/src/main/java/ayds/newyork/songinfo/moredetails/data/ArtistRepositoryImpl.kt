package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoBroker
import ayds.newyork.songinfo.moredetails.data.local.nytimes.CardLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

class ArtistRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val artistInfoBroker: ArtistInfoBroker
) : ArtistRepository {

    override fun searchArtistInfo(artist: String): List<Card?>? {
        var cards: List<Card?>?
        val artistInfo = cardLocalStorage.getArtistInfo(artist)
        when {
            artistInfo != null -> {
                markCardAsLocal(artistInfo)
                cards = artistInfo
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
        return cards
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