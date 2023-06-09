package ayds.newyork.songinfo.moredetails.data.external.proxies

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

internal class WikipediaProxyImpl(
    private val wikipediaArtistInfoService: WikipediaTrackService
): Proxy {

    override fun getArtistInfo(artist: String) =
        wikipediaArtistInfoService.getInfo(artist)?.toCard(artist)

    private fun ArtistInfo.toCard(artistName: String): Card =
        Card(
            artistName = artistName,
            description = this.description,
            infoUrl = this.wikipediaURL,
            source = Source.Wikipedia,
            sourceLogoUrl = this.wikipediaLogoURL,
        )
}