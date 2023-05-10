package ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

interface NYTimesToArtistInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?, artistName: String): ArtistInfo?
}

private const val RESPONSE = "response"
private const val NO_RESULTS = "No Results"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"

internal class JsonToArtistInfoResolver : NYTimesToArtistInfoResolver{

    override fun getArtistInfoFromExternalData(serviceData: String?, artistName: String): ArtistInfo? {
        return try {
            serviceData?.getFirstItem()?.let { item ->
                ArtistInfo(
                    artistName, item.getAbstract(), item.getUrl()
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun String?.getFirstItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val response = jobj.get(RESPONSE).asJsonObject
        val docs = response[DOCS].asJsonArray
        return docs[0].asJsonObject
    }

    private fun JsonObject.getAbstract(): String = get(ABSTRACT).asString

    private fun JsonObject.getUrl(): String = get(WEB_URL).asString

    private fun getTextFromAbstract(abstract: String) = if (abstract != "") abstract else NO_RESULTS
}