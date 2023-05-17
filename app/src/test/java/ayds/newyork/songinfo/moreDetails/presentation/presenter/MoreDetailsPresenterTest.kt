package ayds.newyork.songinfo.moreDetails.presentation.presenter
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelper
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
class MoreDetailsPresenterTest {
    private val artistRepository: ArtistRepository = mockk()
    private val artistAbstractHelper: ArtistAbstractHelper = mockk()

    private lateinit var uiState: MoreDetailsUIState
    private val onActionSubject: Subject<MoreDetailsUIState> = Subject()

    private val presenter by lazy {
        MoreDetailsPresenterImpl(artistRepository,artistAbstractHelper)
    }

    @Test
    fun `getArtistInfo should update UI state with artist info`() {
        val artistInfo = NYTArtistInfo("artist","abstract", "url")
        val expectedUiState = MoreDetailsUIState("abstract", "url", true)

        every { artistRepository.searchArtistInfo("artist") } returns artistInfo
        every { artistAbstractHelper.getInfo(artistInfo) } returns "abstract"

        presenter.getArtistInfo("artist")

        Thread.sleep(1000)
        assertEquals(expectedUiState, presenter.uiState)
    }


    @Test
    fun `getArtistInfo should update UI state as empty when EmptyArtistInfo`() {
        val artistInfo = EmptyArtistInfo
        val expectedUiState = MoreDetailsUIState("", "", false)

        every { artistRepository.searchArtistInfo("artist") } returns artistInfo
        every { artistAbstractHelper.getInfo(artistInfo) } returns ""

        presenter.getArtistInfo("artist")
        Thread.sleep(1000)
        assertEquals(expectedUiState, presenter.uiState)
    }
}