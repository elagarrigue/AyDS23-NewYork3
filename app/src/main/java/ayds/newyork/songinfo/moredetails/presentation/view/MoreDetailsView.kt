package ayds.newyork.songinfo.moredetails.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.dependencyinjector.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.observer.Observer

const val ARTIST_NAME_EXTRA = "artistName"

class MoreDetailsView: AppCompatActivity() {

    private lateinit var artistName: String
    private lateinit var presenter: MoreDetailsPresenter
    private val observer: Observer<MoreDetailsUIState> = Observer { value ->
        updateMoreDetailsView(value)
    }
    private lateinit var viewPager: ViewPager2
    private lateinit var carouselAdapter: CarouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carousel_layout)

        initModule()
        initProperties()
        initArtistName()
        subscribeUiState()
        initCarousel()
        getInfo()
    }

    private fun initCarousel() {
        carouselAdapter = CarouselAdapter(emptyList(), this)
        viewPager.adapter = carouselAdapter
    }

    private fun getInfo() {
        presenter.getArtistInfo(artistName)
    }

    private fun subscribeUiState() {
        presenter.uiStateObservable.subscribe(observer)
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
        presenter = MoreDetailsInjector.getPresenter()
    }

    private fun initArtistName() {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        this.artistName = artistName ?: ""
    }

    private fun initProperties() {
        viewPager = findViewById(R.id.viewPager)
    }

    private fun updateAdapter(cards: List<Card>) {
        carouselAdapter.cards = cards
        carouselAdapter.notifyItemChanged(0)
    }

    private fun updateMoreDetailsView(uiState: MoreDetailsUIState) {
        runOnUiThread {
            updateAdapter(uiState.cardList)
        }
    }
}