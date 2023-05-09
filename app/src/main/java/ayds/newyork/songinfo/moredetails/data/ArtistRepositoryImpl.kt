package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

class ArtistRepositoryImpl : ArtistRepository {
    private fun searchArtistInfo(): ArtistInfo? {
        var artistInfo = getInfoFromDataBase()
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
}