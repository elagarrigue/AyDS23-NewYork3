package ayds.newyork.songinfo.moredetails.presentation.presenter

import android.util.Log
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
                OtherInfoUiEvent.OpenInfoUrl -> prueba()
            }
        }

    private fun prueba(){
        Log.e("TAG","PROBANDOOooooooooooooooooooooooooooooooOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
    }
}