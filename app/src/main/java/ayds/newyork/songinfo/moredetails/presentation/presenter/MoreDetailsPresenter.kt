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
    private var cardRepository: CardRepository,
    private var artistAbstractHelper: ArtistAbstractHelper,
    override var uiState: MoreDetailsUIState

): MoreDetailsPresenter {

    private val onActionSubject = Subject<MoreDetailsUIState>()
    override var uiStateObservable = onActionSubject

    override fun getArtistInfo(artistName: String) {
        Thread {
            val cards = cardRepository.searchArtistInfo(artistName)
            updateUiState(cards)
            notifyUpdate(uiState)
        }.start()
    }

    private fun notifyUpdate(uiState: MoreDetailsUIState) {
        uiStateObservable.notify(uiState)
    }

    private fun updateUiState(cards: List<Card>) {
        cards.forEach { card ->
            card.description = artistAbstractHelper.getInfo(card)
        }
        uiState.cardList = cards
    }
}