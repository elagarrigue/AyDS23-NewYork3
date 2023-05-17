package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.NYTArtistInfo
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService

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
                    artistInfo = newYorkTimesArtistInfoService.getArtistInfo(artist)?.convert()
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

    private fun ayds.ny3.newyorktimes.external.NYTArtistInfo.convert(): NYTArtistInfo {
        return NYTArtistInfo(
            artist = this.artist,
            abstract = this.abstract,
            url = this.url
        )
    }
}