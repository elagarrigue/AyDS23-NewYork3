package ayds.newyork.songinfo.moredetails

import ayds.newyork.songinfo.moredetails.model.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.model.data.local.nytimes.sqldb.NYTimesArtistInfoLocalStorageImpl
import ayds.newyork.songinfo.moredetails.presentation.view.OtherInfoWindow
import ayds.newyork.songinfo.moredetails.model.data.local.nytimes.sqldb.*

object MoreDetailsInjector {

    private lateinit var otherInfoWindow: OtherInfoWindow
    private lateinit var NYTimesArtistInfoLocalStorageImpl: NYTimesArtistInfoLocalStorage
    private var cursorToArtistMapper: CursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()

    fun init(otherInfoWindow: OtherInfoWindow) {
        this.otherInfoWindow = otherInfoWindow
        initializeNYTimesLocalStorage()
    }

    private fun initializeNYTimesLocalStorage() {
        NYTimesArtistInfoLocalStorageImpl = NYTimesArtistInfoLocalStorageImpl(otherInfoWindow, cursorToArtistMapper)
    }

}