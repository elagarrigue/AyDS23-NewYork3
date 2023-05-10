package ayds.newyork.songinfo.moredetails.presentation.view


import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.dependencyinjector.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.presentation.presenter.*
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUiState.Companion.TITLE_IMAGE_URL
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.UtilsInjector.navigationUtils
import ayds.newyork.songinfo.utils.view.ImageLoader
import ayds.observer.*


class MoreDetailsView : AppCompatActivity() {

    private lateinit var moreDetailsTextView: TextView
    private lateinit var titleImageView: ImageView
    private lateinit var openUrlButton: Button
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null
    private lateinit var presenter: Presenter

    fun setPresenter(moreDetailsPresenter: Presenter){
        this.presenter = moreDetailsPresenter
    }

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
        presenter.getInfo(artistName)
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

    private fun updateMoreDetailsText(artistInfo: String?) {
        runOnUiThread {
            moreDetailsTextView.text = Html.fromHtml(artistInfo)
        }
    }

    private val observer: Observer<MoreDetailsUiState> =
        Observer {
                value -> updateMoreDetailsView(value)

            }

    private fun updateMoreDetailsView(uiState: MoreDetailsUiState) {
        updateMoreDetailsText(uiState.info)
        updateUrl(uiState.url)
    }

    private fun updateUrl(url: String) {
        openUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, url) }
    }


    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}