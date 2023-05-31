package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.Card

data class MoreDetailsUIState(
    val nYTimesCard: Card,
    val wikipediaCard: Card,
    val lastFMCard: Card
)
/*
    companion object {
        const val TITLE_IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
    }
}
*/