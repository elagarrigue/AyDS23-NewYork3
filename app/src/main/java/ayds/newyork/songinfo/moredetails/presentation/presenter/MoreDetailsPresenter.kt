package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.view.ArtistAbstractHelper
import ayds.observer.Observable
import ayds.observer.Subject

interface Presenter {
    fun setArtistAbstractHelper(artistAbstractHelper: ArtistAbstractHelper)
    val uiEventObservable: Observable<MoreDetailsUiState>
    fun getInfo(artistName: String?)

}

internal class MoreDetailsPresenterImpl(
    private var artistInfoRepository: ArtistRepository
): Presenter {

    private val onActionSubject = Subject<MoreDetailsUiState>()
    override val uiEventObservable= onActionSubject
    private lateinit var artistAbstractHelper: ArtistAbstractHelper

    override fun getInfo(artistName: String?) {
        // var artistInfo = artistName?.let { artistInfoRepository.searchArtistInfo(it) }
        var artistInfo = ArtistInfo.NYTArtistInfo("artista", "informacion", "http://www.facebook.com", true)
        val uiState = artistInfo?.let { createUiState(artistName, it) }
        uiState?.let { notifyUpdate(it) }
    }

    private fun notifyUpdate(uiState: MoreDetailsUiState){
        uiEventObservable.notify(uiState)
    }

    private fun createUiState(artistName: String?, artistInfo: ArtistInfo): MoreDetailsUiState? =
        artistName?.let { MoreDetailsUiState(artistAbstractHelper.getInfo(artistInfo), artistAbstractHelper.getUrl(artistInfo), it) }

    override fun setArtistAbstractHelper(artistAbstractHelper: ArtistAbstractHelper){
        this.artistAbstractHelper =  artistAbstractHelper
    }
}