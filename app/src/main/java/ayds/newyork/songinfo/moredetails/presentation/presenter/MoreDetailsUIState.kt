package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.Card

data class MoreDetailsUIState(
    var cardList: List<Card>
)