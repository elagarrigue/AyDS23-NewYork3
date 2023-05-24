package ayds.newyork.songinfo.moredetails.data.external

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.ny3.newyorktimes.external.NYTimesArtistInfoService

interface NewYorkTimesProxy {
    fun getArtistInfo(artist: String): Card?
}

class NewYorkTimesProxyImpl(
    private val newYorkTimesArtistInfoService: NYTimesArtistInfoService
): NewYorkTimesProxy {

    override fun getArtistInfo(artist: String) =
        newYorkTimesArtistInfoService.getArtistInfo(artist)?.convert()

    private fun ayds.ny3.newyorktimes.external.NYTArtistInfo.convert(): Card? {
        return Card(
            description = abstract,
            infoUrl = url,
            source = NYT_STRING,
            sourceLogoUrl = NYT_IMAGE
        )
    }

    companion object {
        const val NYT_STRING = "New York Times"
        const val NYT_IMAGE = "https: //encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
    }
}