package ayds.newyork.songinfo.moredetails.data.external.proxies

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.lastfmservice.ArtistService
import ayds.lastfmservice.Artist.LastFMArtist
import ayds.newyork.songinfo.moredetails.domain.entities.Source

internal class LastFMProxyImpl(
    private val lastFMArtistInfoService: ArtistService
): Proxy {

    override fun getArtistInfo(artist: String) =
        lastFMArtistInfoService.getArtist(artist)?.toCard(artist)

    private fun LastFMArtist.toCard(artistName:String):Card =
        Card(
            artistName = artistName,
            description = this.info,
            infoUrl = this.url,
            source = Source.LastFM,
            sourceLogoUrl = this.urlImageLastFM,
        )
}