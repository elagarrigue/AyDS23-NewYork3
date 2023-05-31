package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.newyork.songinfo.moredetails.domain.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsPresenter {

    val uiStateObservable: Observable<MoreDetailsUIState>
    val uiState: MoreDetailsUIState
    fun getArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    var cardRepository: CardRepository,
    var artistAbstractHelper: ArtistAbstractHelper,
    override var uiState: MoreDetailsUIState
): MoreDetailsPresenter {

    private val onActionSubject = Subject<MoreDetailsUIState>()
    override var uiStateObservable = onActionSubject

    override fun getArtistInfo(artistName: String) {
        Thread {
            val cards = cardRepository.searchArtistInfo(artistName)
            updateUiState()
            notifyUpdate(uiState)
        }.start()
    }

    private fun notifyUpdate(uiState: MoreDetailsUIState) {
        uiStateObservable.notify(uiState)
    }

    private fun updateUiState() {
        val cards = ArrayList<Card>()
        cards.add(
            Card("textPrueba1", "artista","https://www.google.com.ar/",
                Source.NYTimes, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU", true)
        )
        cards.add(
            Card("textPrueba2", "artista","https://www.facebook.com/",
                Source.Wikipedia, "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png", true)
        )
        cards.add(
            Card("textPrueba3", "artista","https://www.twitter.com/",
                Source.LastFM, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png", true)
        )
        getUiState(cards)
        notifyUpdate(uiState)
    }

    private fun getUiState(cards: List<Card>) {
        cards.forEach { card ->
            card.description = artistAbstractHelper.getInfo(card)
        }
        uiState.cardList = cards
    }
}