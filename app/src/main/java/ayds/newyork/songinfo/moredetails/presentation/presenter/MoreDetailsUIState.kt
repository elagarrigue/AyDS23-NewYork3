package ayds.newyork.songinfo.moredetails.presentation.presenter

data class MoreDetailsUIState(
    val abstract: String = "",
    val url: String = "",
    val actionsEnabled: Boolean = false
) {

    companion object {
        const val TITLE_IMAGE_URL =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
    }
}