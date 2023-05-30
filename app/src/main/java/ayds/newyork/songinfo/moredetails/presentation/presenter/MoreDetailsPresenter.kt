package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
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
            //val cards = cardRepository.searchArtistInfo(artistName)
            updateUiState(artistName)
            notifyUpdate(uiState)
        }.start()
    }

    private fun notifyUpdate(uiState: MoreDetailsUIState) {
        uiStateObservable.notify(uiState)
    }

    private fun updateUiState(artistName: String) {
        val cards = ArrayList<Card>()
        cards.add(
            Card("textPrueba1", "https://www.google.com.ar/",
                Source.NYTimes, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU", true)
        )
        cards.add(
            Card("textPrueba2", "https://www.facebook.com/",
                Source.Wikipedia, "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png", true)
        )
        cards.add(
            Card("textPrueba3", "https://www.twitter.com/",
                Source.LastFM, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png", true)
        )
        uiState = getUiState(cards, artistName)
        notifyUpdate(uiState)
    }

    private fun getNYTimesCard(cards: List<Card>): Card {
        val newYorkTimesCard = cards.find { it.source == Source.NYTimes }
        return newYorkTimesCard ?: Card(source = Source.NYTimes)
    }

    private fun getWikipediaCard(cards: List<Card>): Card {
        val wikipediaCard = cards.find { it.source == Source.Wikipedia }
        return wikipediaCard ?: Card(source = Source.Wikipedia)
    }

    private fun getLastFMCard(cards: List<Card>): Card {
        val lastFMCard = cards.find { it.source == Source.LastFM }
        return lastFMCard ?: Card(source = Source.LastFM)
    }

    private fun getUiState(cards: List<Card>, artistName: String): MoreDetailsUIState{
        val newYorkTimesCard = getNYTimesCard(cards)
        artistAbstractHelper.getInfo(newYorkTimesCard)
        val wikipediaCard = getWikipediaCard(cards)
        artistAbstractHelper.getInfo(wikipediaCard)
        val lastFMCard = getLastFMCard(cards)
        artistAbstractHelper.getInfo(lastFMCard)
        return MoreDetailsUIState(newYorkTimesCard, wikipediaCard, lastFMCard)
    }
/*
    private fun updateArtistInfoUiState(artistInfo: NYTArtistInfo) {
        uiState = uiState.copy(
            abstract = artistAbstractHelper.getInfo(artistInfo),
            url = artistInfo.url,
            actionsEnabled = true
        )
    }

    private fun updateEmptyUiState() {
        uiState = uiState.copy(
            abstract = artistAbstractHelper.getInfo(EmptyArtistInfo),
            url = "",
            actionsEnabled = false
        )
    }
    */

}