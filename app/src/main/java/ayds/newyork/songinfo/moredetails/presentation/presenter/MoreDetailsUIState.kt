package ayds.newyork.songinfo.moredetails.presentation.presenter

data class MoreDetailsUIState(
    val info: String = "",
    val url: String = "",
    val searchTerm: String = "",
) {

    companion object {
        const val TITLE_IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
    }
}