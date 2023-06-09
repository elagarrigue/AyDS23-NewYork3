package ayds.newyork.songinfo.moreDetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelper
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistAbstractHelperImpl
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArtistAbstractHelperTest {
    private lateinit var artistAbstractHelper: ArtistAbstractHelper

    @Before
    fun setUp() {
        artistAbstractHelper = ArtistAbstractHelperImpl()
    }

    @Test
    fun `given a non-local card with abstract returns the corresponding description`() {
        val infoCard = Card(
            "abstract nombre",
            "nombre",
            "infoURL",
            Source.NYTimes,
            "logoURL"
        )
        val result = artistAbstractHelper.getInfo(infoCard)
        val expected =
            "<html><div width=400><font face=\"arial\">abstract <b>NOMBRE</b></font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given a local card with abstract returns the corresponding description`() {
        val infoCard = Card(
            "abstract",
            "nombre",
            "infoURL",
            Source.NYTimes,
            "logoURL",
            true
        )
        val result = artistAbstractHelper.getInfo(infoCard)
        val expected = "[*] <html><div width=400><font face=\"arial\">abstract</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given a non-local card without abstract returns the corresponding description`() {
        val infoCard = Card(
            "",
            "nombre",
            "infoURL",
            Source.NYTimes,
            "logoURL"
        )
        val result = artistAbstractHelper.getInfo(infoCard)
        val expected = "<html><div width=400><font face=\"arial\">No Abstract</font></div></html>"
        assertEquals(expected, result)
    }

    @Test
    fun `given a local card without abstract returns the corresponding description`() {
        val infoCard = Card(
            "",
            "nombre",
            "infoURL",
            Source.NYTimes,
            "logoURL",
            true
        )
        val result = artistAbstractHelper.getInfo(infoCard)
        val expected = "[*] <html><div width=400><font face=\"arial\">No Abstract</font></div></html>"
        assertEquals(expected, result)
    }
}