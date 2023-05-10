package ayds.newyork.songinfo.moredetails.dependencyinjector

import android.content.Context
import ayds.newyork.songinfo.home.model.HomeModelImpl
import ayds.newyork.songinfo.home.model.HomeModelInjector
import ayds.newyork.songinfo.home.model.repository.SongRepository
import ayds.newyork.songinfo.home.model.repository.SongRepositoryImpl
import ayds.newyork.songinfo.home.model.repository.external.spotify.SpotifyInjector
import ayds.newyork.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.newyork.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.newyork.songinfo.home.model.repository.local.spotify.sqldb.CursorToSpotifySongMapperImpl
import ayds.newyork.songinfo.home.model.repository.local.spotify.sqldb.SpotifyLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.external.nytimes.NYTimesArtistInfoService
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.JsonToArtistInfoResolver
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesAPI
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesArtistInfoServiceImpl
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesToArtistInfoResolver
import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.NYTimesArtistInfoLocalStorageImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.*
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.Presenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.PresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.ArtistAbstractHelper
import ayds.newyork.songinfo.moredetails.presentation.view.ArtistAbstractHelperImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MoreDetailsInjector {

    private const val NEW_YORK_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
    private val newYorkTimesRetrofit = Retrofit.Builder()
        .baseUrl(NEW_YORK_TIMES_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val newYorkTimesAPI: NYTimesAPI = newYorkTimesRetrofit.create(NYTimesAPI::class.java)
    private val newYorkTimesToArtistInfoResolver: NYTimesToArtistInfoResolver = JsonToArtistInfoResolver()

    private lateinit var moreDetailsModel: ArtistRepository
    private lateinit var moreDetailsPresenter: Presenter

    val artistAbstractHelper: ArtistAbstractHelper = ArtistAbstractHelperImpl()
    private val newYorkTimesArtistInfoService: NYTimesArtistInfoService = NYTimesArtistInfoServiceImpl(
        newYorkTimesAPI,
        newYorkTimesToArtistInfoResolver
    )

    fun initMoreDetailsView(moreDetailsView: MoreDetailsView) {
        initMoreDetailsModel(moreDetailsView)
        initMoreDetailsPresenter(moreDetailsView)
    }

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val newYorkTimesArtistInfoLocalStorage: NYTimesArtistInfoLocalStorage = NYTimesArtistInfoLocalStorageImpl(
            moreDetailsView as Context, CursorToArtistInfoMapperImpl()
        )
        moreDetailsModel = ArtistRepositoryImpl(newYorkTimesArtistInfoLocalStorage, newYorkTimesArtistInfoService)
    }

    fun initMoreDetailsPresenter(moreDetailsView: MoreDetailsView) {
        // TODO observers
        moreDetailsPresenter = PresenterImpl()
    }

    fun getPresenter(): Presenter = moreDetailsPresenter
}