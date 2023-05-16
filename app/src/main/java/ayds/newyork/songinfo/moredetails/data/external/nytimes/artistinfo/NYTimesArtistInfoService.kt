package ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import retrofit2.Response

interface NYTimesArtistInfoService {
    fun getArtistInfo(artist: String): NYTArtistInfo?
}

internal class NYTimesArtistInfoServiceImpl(
    private val newYorkTimesAPI: NYTimesAPI,
    private val newYorkTimesToArtistInfoResolver: NYTimesToArtistInfoResolver,
): NYTimesArtistInfoService {

    override fun getArtistInfo(artist: String): NYTArtistInfo? {
        val callResponse = getArtistInfoFromService(artist)
        return newYorkTimesToArtistInfoResolver.getArtistInfoFromExternalData(callResponse.body(), artist)
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> {
        return newYorkTimesAPI.getArtistInfo(artistName).execute()
    }
}