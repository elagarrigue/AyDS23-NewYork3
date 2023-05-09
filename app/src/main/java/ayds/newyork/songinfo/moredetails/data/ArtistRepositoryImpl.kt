package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.external.nytimes.NYTimesArtistInfoService
import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

class ArtistRepositoryImpl(

    private val artistLocalStorage: NYTimesArtistInfoLocalStorage,
    private val nytimesApi: NYTimesArtistInfoService

) : ArtistRepository {
    override fun searchArtistInfo(artist: String): ArtistInfo? {
        var artistInfo = artistLocalStorage.getArtistInfo(artist)
        when {
            artistInfo != null -> markArtistInfoAsLocal(artistInfo)
            else -> {
                artistInfo = nytimesApi.getArtistInfo(artist)
                artistInfo?.let {
                    artistLocalStorage.insertArtistInfo(artistInfo)
                }
            }
        }
        return artistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo: ArtistInfo) {
        artistInfo.isLocallyStored = true
    }
}