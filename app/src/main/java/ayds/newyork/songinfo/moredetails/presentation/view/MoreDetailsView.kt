package ayds.newyork.songinfo.moredetails.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesAPI
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.CursorToArtistInfoMapperImpl
import ayds.newyork.songinfo.moredetails.data.local.nytimes.sqldb.NYTimesArtistInfoLocalStorageImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.OtherInfoUiEvent
import ayds.newyork.songinfo.moredetails.presentation.presenter.OtherInfoUiState
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.observer.Observable
import ayds.observer.Subject
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"
private const val NO_RESULTS = "No Results"

class MoreDetailsView : AppCompatActivity() {

    private lateinit var moreDetailsTextView: TextView
    private lateinit var titleImageView: ImageView
    private lateinit var openUrlButton: Button
    private lateinit var NYTimesArtistInfoLocalStorageImpl: NYTimesArtistInfoLocalStorageImpl
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null

    private val onActionSubject = Subject<OtherInfoUiEvent>()
    val uiEventObservable: Observable<OtherInfoUiEvent> = onActionSubject
    var uiState: OtherInfoUiState = OtherInfoUiState()

    private fun initListeners(urlString: String) {
        openUrlButton.setOnClickListener {
            notifyOpenSongAction()
            openURL(urlString)
        }
    }

    private fun notifyOpenSongAction(){
        onActionSubject.notify(OtherInfoUiEvent.OpenInfoUrl)
    }

    private fun openURL(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        updateTitleImageView()
        initArtistName()
        getArtistInfo()
    }

    private fun initModule(){
        MoreDetailsInjector.init(this)
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
    }

    private fun initProperties() {
        moreDetailsTextView = findViewById(R.id.textMoreDetails)
        titleImageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun initDataBase() {
        NYTimesArtistInfoLocalStorageImpl = NYTimesArtistInfoLocalStorageImpl(this, CursorToArtistInfoMapperImpl()) //TODO: inyeccion de dependencias
    }

    private fun getArtistInfo() {
        Thread {
            val artistInfo = searchArtistInfo()
            updateArtistInfo(artistInfo)
        }.start()
    }

    private fun updateArtistInfo(artistInfo: ArtistInfo?) {
        if (artistInfo?.url != null)
            initListeners(artistInfo.url)
        if (artistInfo?.abstract != null) {
            updateMoreDetailsText(artistInfo)
        }
    }

    private fun getTextFromAbstract(abstract: String?) =
        if (abstract != null && abstract != "") getFormattedTextFromAbstract(abstract) else NO_RESULTS

    private fun getInfoFromDataBase(): ArtistInfo? {
        return if (artistName != null) NYTimesArtistInfoLocalStorageImpl.getArtistInfo(artistName!!) else null
    }

    private fun getInfoFromAPI(): ArtistInfo? {
        return try {
            val callResponse: Response<String> = newYorkTimesAPI.getArtistInfo(artistName).execute()
            val jobj = Gson().fromJson(callResponse.body(), JsonObject::class.java)
            return jsonToArtistInfo(jobj)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun jsonToArtistInfo(jobj: JsonObject?): ArtistInfo? {
        if (jobj == null)
            return null
        val response = jobj.get(RESPONSE).asJsonObject
        val docs = response[DOCS].asJsonArray
        val abstract = if (docs.size() == 0) getTextFromAbstract(null) else getTextFromAbstract(
            docs.get(0).asJsonObject.get(ABSTRACT).asString
        )
        val url = if (docs.size() == 0) null else docs.get(0).asJsonObject.get(WEB_URL).asString
        return artistName?.let {
            ArtistInfo(
                it, abstract, url
            )
        }
    }

    private fun updateTitleImageView() {
        runOnUiThread {
            imageLoader.loadImageIntoView(TITLE_IMAGE_URL, titleImageView)
        }
    }

    private fun updateMoreDetailsText(artistInfo: ArtistInfo) {
        runOnUiThread {
            moreDetailsTextView.text = Html.fromHtml(buildArtistInfoAbstract(artistInfo))
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val TITLE_IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
        private const val NEW_YORK_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
        private val newYorkTimesRetrofit = Retrofit.Builder()
            .baseUrl(NEW_YORK_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val newYorkTimesAPI: NYTimesAPI = newYorkTimesRetrofit.create(
            NYTimesAPI::class.java)
    }
}