package ayds.newyork.songinfo.moreDetails.presentation.presenter
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.newyork.songinfo.moredetails.domain.repository.CardRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelper
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsUIState
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
class MoreDetailsPresenterTest {
    private val cardRepository: CardRepository = mockk()
    private val artistAbstractHelper: ArtistAbstractHelper = mockk()

    private lateinit var uiState: MoreDetailsUIState
    private val onActionSubject: Subject<MoreDetailsUIState> = Subject()

    private val presenter by lazy {
        MoreDetailsPresenterImpl(cardRepository, artistAbstractHelper,uiState)
    }

    @Test
    fun `getArtistInfo should update UI state with artist info`() {
        val card = Card("description", "artistName", "infoUrl", Source.NYTimes, "sourceLogoUrl")
        val cardList = listOf(card)
        val expectedUiState = MoreDetailsUIState(cardList)

        every { cardRepository.searchArtistInfo("artist") } returns cardList
        every { artistAbstractHelper.getInfo(card) } returns "abstract"

        presenter.getArtistInfo("artist")

        Thread.sleep(1000)
        assertEquals(expectedUiState, presenter.uiState)
    }


    @Test
    fun `getArtistInfo should update UI state as empty when emptylist`() {
        val card = Card("description", "artistName", "infoUrl", Source.NYTimes, "sourceLogoUrl")
        val cardList = emptyList<Card>()
        val expectedUiState = MoreDetailsUIState(cardList)

        every { cardRepository.searchArtistInfo("artist") } returns cardList
        every { artistAbstractHelper.getInfo(card) } returns ""

        presenter.getArtistInfo("artist")
        Thread.sleep(1000)
        assertEquals(expectedUiState, presenter.uiState)
    }
}