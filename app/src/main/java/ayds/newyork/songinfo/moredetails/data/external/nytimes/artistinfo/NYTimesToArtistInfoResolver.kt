package ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response

private const val P_RESPONSE = "response"
private const val NO_RESULTS = "No Results"
interface NYTimesToArtistInfoResolver{
    fun updateInfoArtist(callResponse: Response<String>,nameArtist: String?): String
}

internal class JsonToArtistInfoResolver : NYTimesToArtistInfoResolver{
    override fun updateInfoArtist(callResponse: Response<String>, nameArtist: String?): String {
        val response = getJson(callResponse)[P_RESPONSE].asJsonObject
        val abstract = getAsJsonObject(response)
        return abstract?.asString?.replace("\\n", "\n") ?: NO_RESULTS
    }
    private fun getJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun getAsJsonObject(response: JsonObject): JsonElement? {
        return response["docs"].asJsonArray[0].asJsonObject["abstract"]
    }

}