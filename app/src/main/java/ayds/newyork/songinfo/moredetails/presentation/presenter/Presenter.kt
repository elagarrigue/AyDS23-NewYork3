package ayds.newyork.songinfo.moredetails.presentation.presenter

import android.util.Log
import ayds.newyork.songinfo.moredetails.presentation.view.OtherInfoWindow
import ayds.observer.Observer

interface Presenter {
    fun setOtherInfoWindow(otherInfoWindow: OtherInfoWindow)
}

internal class PresenterImpl: Presenter{

    private lateinit var otherInfoWindow: OtherInfoWindow

    override fun setOtherInfoWindow(otherInfoWindow: OtherInfoWindow) {
        this.otherInfoWindow = otherInfoWindow
        otherInfoWindow.uiEventObservable.subscribe(observer)
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