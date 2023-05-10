package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.view.ArtistAbstractHelper
import ayds.observer.Observable
import ayds.observer.Subject

interface Presenter {

    val uiEventObservable: Observable<MoreDetailsUIState>
    fun getArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private var artistInfoRepository: ArtistRepository,
    private val artistAbstractHelper: ArtistAbstractHelper
): Presenter {

    private val onActionSubject = Subject<MoreDetailsUIState>()
    override val uiEventObservable = onActionSubject

    override fun getArtistInfo(artistName: String) {
        Thread {
            val artistInfo = artistInfoRepository.searchArtistInfo(artistName)
            val uiState = createUiState(artistInfo)
            notifyUpdate(uiState)
        }.start()
    }

    private fun notifyUpdate(uiState: MoreDetailsUIState){
        uiEventObservable.notify(uiState)
    }

    private fun createUiState(artistInfo: ArtistInfo): MoreDetailsUIState {
        return when (artistInfo) {
            is NYTArtistInfo -> createArtistInfoUiState(artistInfo)
            EmptyArtistInfo -> createEmptyUiState()
        }
    }

    private fun createArtistInfoUiState(artistInfo: NYTArtistInfo): MoreDetailsUIState {
        return MoreDetailsUIState(
                artistAbstractHelper.getInfo(artistInfo),
                artistInfo.url,
                true
            )
    }

    private fun createEmptyUiState(): MoreDetailsUIState {
        return MoreDetailsUIState(
                artistAbstractHelper.getInfo(EmptyArtistInfo),
                "",
                false
            )
    }
}