package ayds.newyork.songinfo.moredetails.dependencyinjector

import ayds.newyork.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.newyork.songinfo.home.model.repository.external.spotify.auth.SpotifyAuthInjector
import ayds.newyork.songinfo.home.model.repository.external.spotify.tracks.JsonToSongResolver
import ayds.newyork.songinfo.home.model.repository.external.spotify.tracks.SpotifyToSongResolver
import ayds.newyork.songinfo.home.model.repository.external.spotify.tracks.SpotifyTrackInjector
import ayds.newyork.songinfo.home.model.repository.external.spotify.tracks.SpotifyTrackServiceImpl
import ayds.newyork.songinfo.moredetails.data.external.nytimes.NYTimesArtistInfoService
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.JsonToArtistInfoResolver
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesAPI
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesArtistInfoServiceImpl
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesToArtistInfoResolver
import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.NYTimesArtistInfoLocalStorageImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object MoreDetailsInjector {

    private lateinit var otherInfoWindow: MoreDetailsView
    private lateinit var NYTimesArtistInfoLocalStorageImpl: NYTimesArtistInfoLocalStorage
    private var cursorToArtistMapper: CursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()
    private const val NEW_YORK_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
    private val newYorkTimesRetrofit = Retrofit.Builder()
        .baseUrl(NEW_YORK_TIMES_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val newYorkTimesAPI: NYTimesAPI = newYorkTimesRetrofit.create(NYTimesAPI::class.java)
    private val newYorkTimesToArtistInfoResolver: NYTimesToArtistInfoResolver = JsonToArtistInfoResolver()

    val newYorkTimesArtistInfoService: NYTimesArtistInfoService = NYTimesArtistInfoServiceImpl(
        newYorkTimesAPI,
        newYorkTimesToArtistInfoResolver
    )

    fun init(otherInfoWindow: MoreDetailsView) {
        MoreDetailsInjector.otherInfoWindow = otherInfoWindow
        initializeNYTimesLocalStorage()
    }

    private fun initializeNYTimesLocalStorage() {
        NYTimesArtistInfoLocalStorageImpl = NYTimesArtistInfoLocalStorageImpl(otherInfoWindow, cursorToArtistMapper)
    }

}