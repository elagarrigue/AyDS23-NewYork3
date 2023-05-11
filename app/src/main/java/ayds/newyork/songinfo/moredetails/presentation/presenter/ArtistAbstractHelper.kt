package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import java.util.*

private const val HTML_START = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END = "</font></div></html>"
private const val LINE_BREAK = "\n"
private const val LINE_BREAK_ESCAPE_SEQ = "\\n"
private const val HTML_LINE_BREAK = "<br>"
private const val HTML_BOLD_TAG_OPEN = "<b>"
private const val HTML_BOLD_TAG_CLOSE = "</b>"
private const val APOSTROPHE = "'"
private const val SPACE = " "
private const val PREFIX = "[*]"
private const val NO_RESULTS = "No Results"
private const val NO_ABSTRACT = "No Abstract"
private const val EMPTY_STRING = ""

interface ArtistAbstractHelper {
    fun getInfo(artistInfo: ArtistInfo): String
}

class ArtistAbstractHelperImpl() : ArtistAbstractHelper {

    override fun getInfo(artistInfo: ArtistInfo): String {
        return when (artistInfo) {
            is NYTArtistInfo -> buildArtistInfoAbstract(artistInfo)
            else -> NO_RESULTS
        }
    }
    private fun buildArtistInfoAbstract(artistInfo: NYTArtistInfo): String {
        val abstract = getAbstract(artistInfo)
        val formattedAbstract = getFormattedAbstract(artistInfo.artist, abstract)
        return if (artistInfo.isLocallyStored) PREFIX.plus(SPACE).plus(formattedAbstract)
            else formattedAbstract
    }

    private fun getAbstract(artistInfo: NYTArtistInfo): String =
        if (artistInfo.abstract == EMPTY_STRING) NO_ABSTRACT
        else artistInfo.abstract

    private fun getFormattedAbstract(artistName: String, abstract: String): String {
        val text = abstract.replace(LINE_BREAK_ESCAPE_SEQ, LINE_BREAK)
        val textFormatted = textWithBold(artistName, text)
        return textToHtml(textFormatted)
    }

    private fun textWithBold(artistName: String, text: String): String {
        val textWithSpaces = text.replace(APOSTROPHE, SPACE)
        val textWithLineBreaks = textWithSpaces.replace(LINE_BREAK, HTML_LINE_BREAK)
        val termUpperCase = artistName.uppercase(Locale.getDefault())
        return textWithLineBreaks.replace(
            "(?i)$artistName".toRegex(),
            "$HTML_BOLD_TAG_OPEN$termUpperCase$HTML_BOLD_TAG_CLOSE"
        )
    }

    private fun textToHtml(text: String): String {
        return StringBuilder()
            .append(HTML_START)
            .append(HTML_FONT)
            .append(text)
            .append(HTML_END)
            .toString()
    }
}