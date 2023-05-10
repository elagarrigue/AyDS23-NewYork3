package ayds.newyork.songinfo.moredetails.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.dependencyinjector.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesAPI
import ayds.newyork.songinfo.moredetails.presentation.presenter.OtherInfoUiEvent
import ayds.newyork.songinfo.moredetails.presentation.presenter.OtherInfoUiState
import ayds.newyork.songinfo.moredetails.presentation.presenter.Presenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.PresenterImpl
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.observer.Observable
import ayds.observer.Subject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val NO_RESULTS = "No Results"

class MoreDetailsView : AppCompatActivity() {

    private lateinit var moreDetailsTextView: TextView
    private lateinit var titleImageView: ImageView
    private lateinit var openUrlButton: Button
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null

    private val onActionSubject = Subject<OtherInfoUiEvent>()
    val uiEventObservable: Observable<OtherInfoUiEvent> = onActionSubject
    var uiState: OtherInfoUiState = OtherInfoUiState()

    private val artistAbstractHelperImpl = ArtistAbstractHelperImpl()

    private lateinit var presenter: Presenter

    private fun initListeners() {
        openUrlButton.setOnClickListener {
            notifyOpenSongAction()
        }
    }

    private fun notifyOpenSongAction(){
        onActionSubject.notify(OtherInfoUiEvent.OpenInfoUrl)
    }

    fun openURL(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        // Provisorio. se encargaria el inyector
        presenter = PresenterImpl()
        presenter.setOtherInfoWindow(this)

        initModule()
        initProperties()
        initListeners()
        updateTitleImageView()
        initArtistName()
        notifyGetInfo()
    }

    private fun notifyGetInfo(){
        onActionSubject.notify(OtherInfoUiEvent.GetInfo)
    }

    private fun initModule(){
        MoreDetailsInjector.init(this)
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        uiState = uiState.copy(searchTerm = artistName.toString())
    }

    private fun initProperties() {
        moreDetailsTextView = findViewById(R.id.textMoreDetails)
        titleImageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun updateTitleImageView() {
        runOnUiThread {
            imageLoader.loadImageIntoView(TITLE_IMAGE_URL, titleImageView)
        }
    }

    fun updateMoreDetailsText(artistInfo: ArtistInfo) {
        runOnUiThread {
            moreDetailsTextView.text = Html.fromHtml(artistAbstractHelperImpl.buildArtistInfoAbstract(artistInfo))
        }
    }

    fun updateState(artistInfo: ArtistInfo) {
        uiState = uiState.copy(info = artistInfo.abstract.toString())
        uiState = uiState.copy(url = artistInfo.url.toString())
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}