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
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest {
    private val cardRepository: CardRepository = mockk()
    private val artistAbstractHelper: ArtistAbstractHelper = mockk()
    private var uiState: MoreDetailsUIState = MoreDetailsUIState()

    private val presenter by lazy {
        MoreDetailsPresenterImpl(cardRepository, artistAbstractHelper, uiState)
    }

    @Test
    fun `calling getArtistInfo should notify UI state update with artist info`() {
        val card = Card(
            "description",
            "artistName",
            "infoUrl",
            Source.NYTimes,
            "sourceLogoUrl"
        )
        val cardList = listOf(card)
        val presenterTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)
        val expectedUiState = MoreDetailsUIState(cardList)
        every { cardRepository.searchArtistInfo("artist") } returns cardList
        every { artistAbstractHelper.getInfo(card) } returns "description"

        presenter.uiStateObservable.subscribe {
            presenterTester(it)
        }
        presenter.getArtistInfo("artist")

        verify { presenterTester(expectedUiState) }
    }

    @Test
    fun `calling getArtistInfo should notify update UI state as empty when there are no cards`() {
        val cardList = emptyList<Card>()
        val presenterTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)
        val expectedUiState = MoreDetailsUIState(cardList)
        every { cardRepository.searchArtistInfo("artist") } returns cardList

        presenter.uiStateObservable.subscribe {
            presenterTester(it)
        }
        presenter.getArtistInfo("artist")

        verify { presenterTester(expectedUiState) }
    }
}