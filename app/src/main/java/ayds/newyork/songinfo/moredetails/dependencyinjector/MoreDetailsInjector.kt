package ayds.newyork.songinfo.moredetails.dependencyinjector

import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.NYTimesArtistInfoLocalStorageImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.*

object MoreDetailsInjector {

    private lateinit var otherInfoWindow: MoreDetailsView
    private lateinit var NYTimesArtistInfoLocalStorageImpl: NYTimesArtistInfoLocalStorage
    private var cursorToArtistMapper: CursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()

    fun init(otherInfoWindow: MoreDetailsView) {
        MoreDetailsInjector.otherInfoWindow = otherInfoWindow
        initializeNYTimesLocalStorage()
    }

    private fun initializeNYTimesLocalStorage() {
        NYTimesArtistInfoLocalStorageImpl = NYTimesArtistInfoLocalStorageImpl(otherInfoWindow, cursorToArtistMapper)
    }

}