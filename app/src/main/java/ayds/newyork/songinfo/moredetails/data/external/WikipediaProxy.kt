package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo
interface WikipediaProxy {
    fun getArtistInfo(artist: String): Card?
}

class WikipediaProxyImpl(
    private val artistService: WikipediaTrackService
): WikipediaProxy {

    override fun getArtistInfo(artist: String): Card? {
        val wikipediaArtist = artistService.getInfo(artist)
        return wikipediaArtist?.toCard(artist)
    }
    private fun ArtistInfo.toCard(artistName: String):Card =
            Card(
                artistName=artistName,
                description=this.description,
                infoUrl=this.wikipediaURL,
                source= Source.Wikipedia,
                sourceLogoUrl=this.wikipediaLogoURL,
            )

}