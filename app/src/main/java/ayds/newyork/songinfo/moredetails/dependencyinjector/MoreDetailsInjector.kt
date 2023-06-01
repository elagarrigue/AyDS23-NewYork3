package ayds.newyork.songinfo.moredetails.dependencyinjector

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.CardRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.local.nytimes.CardLocalStorage
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.CardLocalStorageImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.CursorToCardMapperImpl
import ayds.newyork.songinfo.moredetails.domain.repository.CardRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelperImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoServiceInjector
import ayds.lastfmservice.LastFMInjector
import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoBrokerImpl
import ayds.newyork.songinfo.moredetails.data.external.LastFMProxyImpl
import ayds.newyork.songinfo.moredetails.data.external.NewYorkTimesProxyImpl
import ayds.newyork.songinfo.moredetails.data.external.WikipediaProxyImpl
import ayds.winchester2.wikipediaexternal.injector.WikipediaInjector

object MoreDetailsInjector {

    private lateinit var moreDetailsModel: CardRepository
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    private val lastFMProxy = LastFMProxyImpl(LastFMInjector.getService())
    private val wikipediaProxy = WikipediaProxyImpl(WikipediaInjector.wikipediaTrackService)
    private val newYorkTimesProxy = NewYorkTimesProxyImpl(NYTimesArtistInfoServiceInjector.newYorkTimesArtistInfoServiceImpl)

    private val artistInfoBroker = ArtistInfoBrokerImpl(lastFMProxy,newYorkTimesProxy,wikipediaProxy)

    fun init(moreDetailsView: MoreDetailsView) {
        initMoreDetailsModel(moreDetailsView)
        moreDetailsPresenter = MoreDetailsPresenterImpl(moreDetailsModel, ArtistAbstractHelperImpl(), MoreDetailsUIState())
    }

    private fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val newYorkTimesArtistInfoLocalStorage: CardLocalStorage = CardLocalStorageImpl(
            moreDetailsView as Context, CursorToCardMapperImpl()
        )
        moreDetailsModel = CardRepositoryImpl(newYorkTimesArtistInfoLocalStorage, artistInfoBroker)
    }

    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter
}