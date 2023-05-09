package ayds.newyork.songinfo.moredetails.model.data.local.nytimes

import ayds.newyork.songinfo.moredetails.model.domain.entities.ArtistInfo

interface NYTimesArtistInfoLocalStorage {

    fun updateInfoByName(query: String, artistId: String)

    fun insertInfo(query: String, info: ArtistInfo)

    fun getInfoByName(name: String): ArtistInfo?

    fun getInfoById(id: String): ArtistInfo?
}