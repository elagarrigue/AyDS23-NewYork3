package ayds.newyork.songinfo.moredetails.data.external.proxies

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService
import ayds.ny3.newyorktimes.external.NYTArtistInfo

internal class NewYorkTimesProxyImpl(
    private val newYorkTimesArtistInfoService: NYTimesArtistInfoService
): Proxy {

    override fun getArtistInfo(artist: String) =
        newYorkTimesArtistInfoService.getArtistInfo(artist)?.toCard()

    private fun NYTArtistInfo.toCard(): Card {
        return Card(
            description = abstract,
            artistName = artist,
            infoUrl = url,
            source = Source.NYTimes,
            sourceLogoUrl = nytLogoUrl,
        )
    }
}