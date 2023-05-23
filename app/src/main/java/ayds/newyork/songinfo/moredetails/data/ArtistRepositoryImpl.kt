package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.external.nytimes.artistinfo.NYTimesArtistInfoService
import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

class ArtistRepositoryImpl(
    private val artistLocalStorage: NYTimesArtistInfoLocalStorage,
    private val newYorkTimesArtistInfoService: NYTimesArtistInfoService
) : ArtistRepository {

    override fun searchArtistInfo(artist: String): ArtistInfo {
        var artistInfo = artistLocalStorage.getArtistInfo(artist)
        when {
            artistInfo != null -> markArtistInfoAsLocal(artistInfo)
            else -> {
                try {
                    artistInfo = newYorkTimesArtistInfoService.getArtistInfo(artist)
                    artistInfo?.let {
                        artistLocalStorage.insertArtistInfo(it)
                    }
                } catch (e: Exception) {
                    artistInfo = null
                }
            }
        }
        return artistInfo ?: EmptyArtistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo: NYTArtistInfo) {
        artistInfo.isLocallyStored = true
    }
}