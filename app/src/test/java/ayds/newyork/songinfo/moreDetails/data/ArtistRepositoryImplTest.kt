package ayds.newyork.songinfo.moreDetails.data

import ayds.newyork.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService
import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ArtistRepositoryImplTest {

    private val artistLocalStorage: NYTimesArtistInfoLocalStorage = mockk(relaxUnitFun = true)
    private val newYorkTimesArtistInfoService: NYTimesArtistInfoService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistRepositoryImpl by lazy {
        ArtistRepositoryImpl(artistLocalStorage, newYorkTimesArtistInfoService)
    }
    private lateinit var artistInfo: NYTArtistInfo

    @Before
    fun initArtistInfo() {
        artistInfo = NYTArtistInfo(
            "artist",
            "abstract",
            "url"
        )
    }

    @Test
    fun `given existing artist name should mark info as local and return it`() {
        every { artistLocalStorage.getArtistInfo("artist") } returns artistInfo

        val result = artistRepository.searchArtistInfo("artist")

        assertEquals(artistInfo, result)
        assertTrue(artistInfo.isLocallyStored)
    }

    @Test
    fun `given non existing artist name should get info from service, store and return it`() {
        val externalServiceArtistInfo = ayds.ny3.newyorktimes.external.NYTArtistInfo(
            "artist",
            "abstract",
            "url"
        )

        every { artistLocalStorage.getArtistInfo("artist") } returns null
        every { newYorkTimesArtistInfoService.getArtistInfo("artist") } returns externalServiceArtistInfo

        val result = artistRepository.searchArtistInfo("artist")

        assertEquals(artistInfo, result)
        assertFalse(artistInfo.isLocallyStored)
        verify { artistLocalStorage.insertArtistInfo(artistInfo) }
    }

    @Test
    fun `given non existing artist should return emptyArtistInfo`() {
        every { artistLocalStorage.getArtistInfo("artist") } returns null
        every { newYorkTimesArtistInfoService.getArtistInfo("artist") } returns null

        val result = artistRepository.searchArtistInfo("artist")

        assertEquals(EmptyArtistInfo, result)
        assertFalse(artistInfo.isLocallyStored)
        verify(exactly = 0) { artistLocalStorage.insertArtistInfo(any()) }
    }

    @Test
    fun `given service exception should return emptyArtistInfo`() {
        every { artistLocalStorage.getArtistInfo("artist") } returns null
        every { newYorkTimesArtistInfoService.getArtistInfo("artist") } throws mockk<Exception>()

        val result = artistRepository.searchArtistInfo("artist")

        assertEquals(EmptyArtistInfo, result)
        assertFalse(artistInfo.isLocallyStored)
        verify(exactly = 0) { artistLocalStorage.insertArtistInfo(any()) }
    }
}