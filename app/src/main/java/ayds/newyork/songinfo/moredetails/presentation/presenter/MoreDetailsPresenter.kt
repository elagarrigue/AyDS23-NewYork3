package ayds.newyork.songinfo.moredetails.presentation.presenter

import android.util.Log
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.observer.Observer

interface Presenter {
    fun setOtherInfoWindow(moreDetailsView: MoreDetailsView)
}

internal class PresenterImpl: Presenter{

    private lateinit var moreDetailsView: MoreDetailsView

    override fun setOtherInfoWindow(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        moreDetailsView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiEvent> =
        Observer { value ->
            when (value) {
                OtherInfoUiEvent.OpenInfoUrl -> openUrl()
                OtherInfoUiEvent.GetInfo -> getInfo()
            }
        }

    private fun openUrl(){
        moreDetailsView.openURL(moreDetailsView.uiState.url)
    }

    private fun getInfo(){
        // searchArtistInfo() estaba en la vista
        val artistInfo = ArtistInfo("artista","informacion","https://www.google.com.ar",true)
        moreDetailsView.updateMoreDetailsText(artistInfo)
    }
}