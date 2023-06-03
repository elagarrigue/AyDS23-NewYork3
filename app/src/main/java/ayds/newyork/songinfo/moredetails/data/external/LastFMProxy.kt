package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.lastfmservice.ArtistService
import ayds.lastfmservice.Artist.LastFMArtist
import ayds.newyork.songinfo.moredetails.domain.entities.Source

class LastFMProxyImpl(
    private val artistService: ArtistService
): Proxy {

    override fun getArtistInfo(artist: String): Card? {
        val lastFMArtist = artistService.getArtist(artist)
        return lastFMArtist?.toCard(artist)
    }

    private fun LastFMArtist.toCard(artistName:String):Card =
            Card(
                artistName=artistName,
                description=this.info,
                infoUrl=this.url,
                source= Source.LastFM,
                sourceLogoUrl=this.urlImageLastFM,
            )
}