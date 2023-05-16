package ayds.newyork.songinfo.moreDetails.presentation
import ayds.newyork.songinfo.home.controller.HomeControllerImpl
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelper
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.newyork.songinfo.moredetails.presentation.presenter.Presenter
import ayds.observer.Observable
import ayds.observer.Observer
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
class MoreDetailsPresenterTest {
    private val artistRepository: ArtistRepository = mockk()
    private val artistAbstractHelper: ArtistAbstractHelper = mockk()

    private lateinit var uiStateObservable: Subject<MoreDetailsUIState>
    private lateinit var uiState: MoreDetailsUIState
    private val observer: Observer<MoreDetailsUIState> = mockk(relaxUnitFun = true)
    private val onActionSubject = Subject<MoreDetailsUIState>()

    private val presenter by lazy {
        MoreDetailsPresenterImpl(artistRepository, artistAbstractHelper)
    }

    @Before
    fun setup() {
        uiStateObservable = onActionSubject
        uiState = MoreDetailsUIState()

    }

    @Test
    fun `getArtistInfo should update UI state with artist info`() {
        val artistInfo = NYTArtistInfo("artist","abstract", "url")
        val expectedUiState = MoreDetailsUIState("abstract", "url", true)

        every { artistRepository.searchArtistInfo("artist") } returns artistInfo
        every { artistAbstractHelper.getInfo(artistInfo) } returns "abstract"

        presenter.getArtistInfo("artist")

        verify { onActionSubject.notify(uiState) }
    }

    @Test
    fun `getArtistInfo should update UI state as empty when EmptyArtistInfo`() {
        val artistInfo = EmptyArtistInfo
        val expectedUiState = MoreDetailsUIState("", "", false)

        every { artistRepository.searchArtistInfo("artist") } returns artistInfo
        every { artistAbstractHelper.getInfo(artistInfo) } returns ""

        presenter.getArtistInfo("artist")

        verify { onActionSubject.notify(expectedUiState) }
    }
}