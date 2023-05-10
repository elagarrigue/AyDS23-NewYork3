package ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException

private const val P_RESPONSE = "response"
private const val NO_RESULTS = "No Results"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url" //no debería tener el valor de una url?

interface NYTimesToArtistInfoResolver {
    fun updateInfoArtist(callResponse: Response<String>, nameArtist: String?): String

    fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo?
}

internal class JsonToArtistInfoResolver : NYTimesToArtistInfoResolver{
    override fun updateInfoArtist(callResponse: Response<String>, nameArtist: String?): String {
        val response = getJson(callResponse)[P_RESPONSE].asJsonObject
        val abstract = getAsJsonObject(response)
        return abstract?.asString?.replace("\\n", "\n") ?: NO_RESULTS
    }

    override fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo? {
        return try {
            serviceData?.getFirstItem()?.let { item ->
                ArtistInfo(
                    item.getArtist(), item.getAbstract(), item.getUrl()
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun JsonObject.getArtist(): String {
    }

    private fun JsonObject.getAbstract(): String {

    }

    private fun JsonObject.getUrl(): String {

    }

    private fun String?.getFirstItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        val response = jobj.get(P_RESPONSE).asJsonObject
        val docs = response[DOCS].asJsonArray
        return docs[0].asJsonObject
    }

    private fun getJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun getAsJsonObject(response: JsonObject): JsonElement? {
        return response["docs"].asJsonArray[0].asJsonObject["abstract"]
    }

    private fun getInfoFromDataBase(): ArtistInfo? {
        return if (artistName != null) NYTimesArtistInfoLocalStorageImpl.getArtistInfo(artistName!!) else null
    }

    private fun jsonToArtistInfo(jobj: JsonObject?): ArtistInfo? {
        if (jobj == null)
            return null
        val response = jobj.get(P_RESPONSE).asJsonObject
        val docs = response[DOCS].asJsonArray
        val abstract = if (docs.size() == 0) getTextFromAbstract(null) else getTextFromAbstract(
            docs.get(0).asJsonObject.get(ABSTRACT).asString
        )
        val url = if (docs.size() == 0) null else docs.get(0).asJsonObject.get(WEB_URL).asString
        return artistName?.let {
            ArtistInfo(
                it, abstract, url
            )
        }
    }
}