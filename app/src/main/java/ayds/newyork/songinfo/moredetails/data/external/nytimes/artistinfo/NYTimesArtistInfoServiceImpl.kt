package ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo

import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.moredetails.data.external.nytimes.NYTimesArtistInfoService
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo

class NYTimesArtistInfoServiceImpl(
    private val nyTimesAPI: NYTimesAPI,
    private val nyTimesToArtistInfoResolver: NYTimesToArtistInfoResolver,
): NYTimesArtistInfoService {

    override fun getArtistInfo(artist: String): ArtistInfo?{
        val callResponse = getArtistInfoFromService(artist)
        val info = nyTimesToArtistInfoResolver.updateInfoArtist(callResponse, artist);
        val url = "" //TODO : Generar url
        return ArtistInfo(artist,info,url,false);
    }

    private fun getArtistInfoFromService(artistName: String?) = nyTimesAPI.getArtistInfo(artistName).execute()
}