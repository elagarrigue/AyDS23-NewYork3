package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoBroker
import ayds.newyork.songinfo.moredetails.data.local.nytimes.CardLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.repository.CardRepository

class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val artistInfoBroker: ArtistInfoBroker
): CardRepository {

    override fun searchArtistInfo(artist: String): List<Card> {
        var cards = cardLocalStorage.getArtistInfo(artist)
        when {
            cards.isNotEmpty() -> {
                markCardsAsLocals(cards)
            }
            else -> {
                try {
                    cards = artistInfoBroker.getArtistInfo(artist)
                    cards.forEach { card ->
                        cardLocalStorage.insertArtistInfo(card)
                    }
                } catch (e: Exception) {
                    cards = emptyList()
                }
            }
        }
        return cards
    }

    private fun markCardsAsLocals(cards: List<Card>) {
        cards.forEach { it.isLocallyStored = true}
    }
}