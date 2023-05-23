package ayds.newyork.songinfo.moreDetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelper
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelperImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArtistAbstractHelperTest {
    private lateinit var artistAbstractHelper: ArtistAbstractHelper

    @Before
    fun setUp(){
        artistAbstractHelper = ArtistAbstractHelperImpl()
    }

    @Test
    fun `given a non-local artist with abstract returns the corresponding description`() {
        val artistInfo = ArtistInfo.NYTArtistInfo("nombre", "abstract nombre", "url", false)
        val result = artistAbstractHelper.getInfo(artistInfo)
        val expected = "<html><div width=400><font face=\"arial\">abstract <b>NOMBRE</b></font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given a local artist with abstract returns the corresponding description`() {
        val artistInfo = ArtistInfo.NYTArtistInfo("nombre", "abstract", "url", true)
        val result = artistAbstractHelper.getInfo(artistInfo)
        val expected = "[*] <html><div width=400><font face=\"arial\">abstract</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given an empty artistInfo returns the corresponding description`() {
        val artistInfo = ArtistInfo.EmptyArtistInfo
        val result = artistAbstractHelper.getInfo(artistInfo)
        val expected = "No Results"
        assertEquals(expected, result)
    }

    @Test
    fun `given a non-local artist without abstract returns the corresponding description`() {
        val artistInfo = ArtistInfo.NYTArtistInfo("nombre", "", "url", false)
        val result = artistAbstractHelper.getInfo(artistInfo)
        val expected = "<html><div width=400><font face=\"arial\">No Abstract</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given a local artist without abstract returns the corresponding description`() {
        val artistInfo = ArtistInfo.NYTArtistInfo("nombre", "", "url", true)
        val result = artistAbstractHelper.getInfo(artistInfo)
        val expected = "[*] <html><div width=400><font face=\"arial\">No Abstract</font></div></html>"
        assertEquals(expected, result)
    }
}