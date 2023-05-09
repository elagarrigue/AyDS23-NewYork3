package ayds.newyork.songinfo.moredetails.presentation.view

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
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

interface ArtistAbstractHelper {
}

class ArtistAbstractHelperImpl() : ArtistAbstractHelper {
    private fun textToHtml(text: String): String {
        return StringBuilder()
            .append(HTML_START)
            .append(HTML_FONT)
            .append(text)
            .append(HTML_END)
            .toString()
    }

    private fun textWithBold(artistName: String, text: String): String {
        val textWithSpaces = text.replace(APOSTROPHE, SPACE)
        val textWithLineBreaks = textWithSpaces.replace(LINE_BREAK, HTML_LINE_BREAK)
        val termUpperCase = artistName?.uppercase(Locale.getDefault())
        return textWithLineBreaks.replace(
            "(?i)$artistName".toRegex(),
            "$HTML_BOLD_TAG_OPEN$termUpperCase$HTML_BOLD_TAG_CLOSE"
        )
    }

    fun getFormattedTextFromAbstract(artistName: String, abstract: String): String {
        val text = abstract.replace(LINE_BREAK_ESCAPE_SEQ, LINE_BREAK)
        val textFormatted = textWithBold(artistName, text)
        return textToHtml(textFormatted)
    }

    fun buildArtistInfoAbstract(artistInfo: ArtistInfo): String? {
        if (artistInfo.isLocallyStored) artistInfo.abstract = PREFIX.plus(SPACE).plus("${artistInfo.abstract}")
        return artistInfo.abstract
    }
}