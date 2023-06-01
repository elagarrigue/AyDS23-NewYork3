package ayds.newyork.songinfo.moreDetails.data

import ayds.newyork.songinfo.moredetails.data.CardRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoBroker
import ayds.newyork.songinfo.moredetails.data.local.nytimes.CardLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.newyork.songinfo.moredetails.domain.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CardRepositoryImplTest {

    private val cardLocalStorage: CardLocalStorage = mockk(relaxUnitFun = true)
    private val artistInfoBroker: ArtistInfoBroker = mockk(relaxUnitFun = true)

    private val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(cardLocalStorage, artistInfoBroker)
    }
    private lateinit var card: Card
    private lateinit var cardList: List<Card>

    @Before
    fun initCardList() {
        card = Card(
            "abstract",
            "artist",
            "url",
            Source.NYTimes,
            "logo_url",
        )
        cardList = listOf(card)
    }

    @Test
    fun `given existing artist name should mark card as local and return it`() {
        every { cardLocalStorage.getArtistInfo("artist") } returns cardList

        val result = cardRepository.searchArtistInfo("artist")

        assertEquals(cardList, result)
        assertTrue(card.isLocallyStored)
    }

    @Test
    fun `given non existing artist name should get card from service, store and return it`() {
        every { cardLocalStorage.getArtistInfo("artist") } returns null
        every { artistInfoBroker.getArtistInfo("artist") } returns cardList

        val result = cardRepository.searchArtistInfo("artist")

        assertEquals(cardList, result)
        assertFalse(card.isLocallyStored)
        verify(exactly = 1) { cardLocalStorage.insertArtistInfo(card) }
    }

    @Test
    fun `given non existing artist should return emptyCard`() {
        every { cardLocalStorage.getArtistInfo("artist") } returns null
        every { artistInfoBroker.getArtistInfo("artist") } returns emptyList()

        val result = cardRepository.searchArtistInfo("artist")

        assertEquals(emptyList<Card>(), result)
        assertFalse(card.isLocallyStored)
        verify(exactly = 0) { cardLocalStorage.insertArtistInfo(any()) }
    }

    @Test
    fun `given service exception should return emptyCard`() {
        every { cardLocalStorage.getArtistInfo("artist") } returns null
        every { artistInfoBroker.getArtistInfo("artist") } throws mockk<Exception>()

        val result = cardRepository.searchArtistInfo("artist")

        assertEquals(emptyList<Card>(), result)
        assertFalse(card.isLocallyStored)
        verify(exactly = 0) { cardLocalStorage.insertArtistInfo(any()) }
    }
}