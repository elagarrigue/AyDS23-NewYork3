package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.local.nytimes.NYTimesArtistInfoLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

class ArtistRepositoryImpl(

    private val artistLocalStorage: NYTimesArtistInfoLocalStorage

) : ArtistRepository {
    override fun searchArtistInfo(artist: String): ArtistInfo? {
        var artistInfo = artistLocalStorage.getArtistInfo(artist)
        when {
            artistInfo != null -> markArtistInfoAsLocal(artistInfo)
            else -> {
                artistInfo = getInfoFromAPI()
                artistInfo?.let {
                    NYTimesArtistInfoLocalStorageImpl.insertArtistInfo(artistInfo)
                }
            }
        }
        return artistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo: ArtistInfo) {
        artistInfo.isLocallyStored = true
    }
}