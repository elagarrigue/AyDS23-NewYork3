package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.view.ArtistAbstractHelper
import ayds.observer.Observable
import ayds.observer.Subject

interface Presenter {

    val uiStateObservable: Observable<MoreDetailsUIState>
    fun getArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private var artistInfoRepository: ArtistRepository,
    private val artistAbstractHelper: ArtistAbstractHelper
): Presenter {

    private val onActionSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onActionSubject
    private var uiState = MoreDetailsUIState()

    override fun getArtistInfo(artistName: String) {
        Thread {
            val artistInfo = artistInfoRepository.searchArtistInfo(artistName)
            updateUiState(artistInfo)
            notifyUpdate(uiState)
        }.start()
    }

    private fun notifyUpdate(uiState: MoreDetailsUIState) {
        uiStateObservable.notify(uiState)
    }

    private fun updateUiState(artistInfo: ArtistInfo) {
        when (artistInfo) {
            is NYTArtistInfo -> updateArtistInfoUiState(artistInfo)
            EmptyArtistInfo -> updateEmptyUiState()
        }
    }

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
}