package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.lastfmservice.ArtistService
import ayds.lastfmservice.Artist.LastFMArtist
import ayds.newyork.songinfo.moredetails.domain.entities.Source

interface LastFMProxy {
    fun getArtistInfo(artist: String): Card?
}

class LastFMProxyImpl(
    private val artistService: ArtistService
): LastFMProxy {

    override fun getArtistInfo(artist: String): Card? {
        val lastFMArtist = artistService.getArtist(artist)
        return lastFMArtist?.toCard()
    }
    private fun LastFMArtist.toCard():Card =
            Card(
                artistName=this.name,
                description=this.info,
                infoUrl=this.url,
                source= Source.LastFM,
                sourceLogoUrl=this.urlImageLastFM,
            )

}