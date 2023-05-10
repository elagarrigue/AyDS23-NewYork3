package ayds.newyork.songinfo.moredetails.presentation.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.dependencyinjector.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.presentation.presenter.*
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState.Companion.TITLE_IMAGE_URL
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.UtilsInjector.navigationUtils
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.observer.*

const val ARTIST_NAME_EXTRA = "artistName"

class MoreDetailsView : AppCompatActivity() {

    private lateinit var moreDetailsTextView: TextView
    private lateinit var titleImageView: ImageView
    private lateinit var openUrlButton: Button
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null
    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        updateTitleImageView()
        initArtistName()
        subscribeUiState()
        getInfo()
    }

    private fun getInfo() {
        presenter.getArtistInfo(artistName)
    }

    private fun subscribeUiState() {
        presenter.uiEventObservable.subscribe(observer)
    }

    private fun initModule(){
        MoreDetailsInjector.init(this)
        presenter = MoreDetailsInjector.getPresenter()
    }

    private fun initArtistName() {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        this.artistName = artistName.toString()
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

    private fun updateMoreDetailsText(artistInfo: String) {
        runOnUiThread {
            moreDetailsTextView.text = HtmlCompat.fromHtml(artistInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private val observer: Observer<MoreDetailsUIState> =
        Observer {
                value -> updateMoreDetailsView(value)
        }

    private fun updateMoreDetailsView(uiState: MoreDetailsUIState) {
        updateMoreDetailsText(uiState.info)
        updateUrl(uiState.url)
        runOnUiThread {
            openUrlButton.isEnabled = !uiState.url.isNullOrEmpty()
        }
    }

    private fun updateUrl(url: String) {
        openUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, url) }
    }
}