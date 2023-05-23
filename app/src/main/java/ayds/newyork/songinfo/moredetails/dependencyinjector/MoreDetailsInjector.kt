package ayds.newyork.songinfo.moredetails.dependencyinjector

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.NYTimesArtistInfoLocalStorageImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.CursorToArtistInfoMapperImpl
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.Presenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelperImpl
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoServiceInjector

object MoreDetailsInjector {

    private lateinit var moreDetailsModel: ArtistRepository
    private lateinit var moreDetailsPresenter: Presenter

    private val newYorkTimesArtistInfoService: NYTimesArtistInfoService = NYTimesArtistInfoServiceInjector.newYorkTimesArtistInfoServiceImpl

    fun init(moreDetailsView: MoreDetailsView) {
        initMoreDetailsModel(moreDetailsView)
        moreDetailsPresenter = MoreDetailsPresenterImpl(moreDetailsModel, ArtistAbstractHelperImpl())
    }

    private fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val newYorkTimesArtistInfoLocalStorage: NYTimesArtistInfoLocalStorage = NYTimesArtistInfoLocalStorageImpl(
            moreDetailsView as Context, CursorToArtistInfoMapperImpl()
        )
        moreDetailsModel = ArtistRepositoryImpl(newYorkTimesArtistInfoLocalStorage, newYorkTimesArtistInfoService)
    }

    fun getPresenter(): Presenter = moreDetailsPresenter
}