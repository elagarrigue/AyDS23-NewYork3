package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.utils.UtilsInjector
import ayds.newyork.songinfo.utils.view.ImageLoader
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"
private const val PREFIX = "[*]"
private const val NO_RESULTS = "No Results"
private const val HTML_START = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_END = "</font></div></html>"

class OtherInfoWindow : AppCompatActivity() {

    private lateinit var moreDetailsTextView: TextView
    private lateinit var titleImageView: ImageView
    private lateinit var openUrlButton: Button
    private lateinit var dataBase: DataBase
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader
    private var artistName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        initDataBase()
        updateTitleImageView()
        initArtistName()
        getArtistInfo()
    }

    private fun initArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
    }

    private fun initProperties() {
        moreDetailsTextView = findViewById(R.id.textMoreDetails)
        titleImageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun getArtistInfo() {
        Thread {
            val artistInfo = searchArtistInfo()
            if (artistInfo != null) {
                if (artistInfo.url != null)
                    initListeners(artistInfo.url)
                updateMoreDetailsText(artistInfo.abstract)
            }
        }.start()
    }

    private fun searchArtistInfo(): ArtistInfo? {
        var artistInfo = getInfoFromDataBase()
        when {
            artistInfo != null -> markArtistInfoAsLocal(artistInfo)
            else -> {
                artistInfo = getInfoFromAPI()
                artistInfo?.let {
                        dataBase.saveArtistInfo(artistInfo)
                }
            }
        }
        return artistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo: ArtistInfo) {
        artistInfo.isLocallyStored = true
    }

    private fun getTextFromAbstract(abstract: String?): String {
        return if (abstract != null)
            getFormattedTextFromAbstract(abstract)
        else
            NO_RESULTS
    }

    private fun getFormattedTextFromAbstract(abstract : String) : String {
        var text = abstract.replace("\\n", "\n")
        val textFormatted = textWithBold(text)
        text = textToHtml(textFormatted)
        return text
    }

    private fun textWithBold(text: String): String {
        val textWithSpaces = text.replace("'", " ")
        val textWithLineBreaks = textWithSpaces.replace("\n", "<br>")
        val termUpperCase = artistName?.uppercase(Locale.getDefault())
        return textWithLineBreaks.replace("(?i)$artistName".toRegex(), "<b>$termUpperCase</b>")
    }

    private fun getInfoFromDataBase() : ArtistInfo? {
        val artistInfo: ArtistInfo? = if (artistName != null) dataBase.getInfo(artistName!!) else null
        if (artistInfo != null)
            artistInfo.abstract = PREFIX + "${artistInfo.abstract}"
        return artistInfo
    }

    private fun getInfoFromAPI(): ArtistInfo? {
        return try {
            val callResponse: Response<String> = newYorkTimesAPI.getArtistInfo(artistName).execute()
            val jobj = Gson().fromJson(callResponse.body(), JsonObject::class.java)
            val response = jobj[RESPONSE].asJsonObject
            val docs = response[DOCS].asJsonArray
            val abstract = if (docs.size() == 0) null else getTextFromAbstract(docs.get(0).asJsonObject.get(ABSTRACT).asString)
            val url = if (docs.size() == 0) null else docs.get(0).asJsonObject.get(WEB_URL).asString
            return artistName?.let {
                ArtistInfo(
                    it, abstract, url
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun initListeners(urlString: String) {
        openUrlButton.setOnClickListener {
            openURL(urlString)
        }
    }

    private fun openURL(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }

    private fun updateTitleImageView() {
        runOnUiThread {
            imageLoader.loadImageIntoView(TITLE_IMAGE_URL, titleImageView)
        }
    }

    private fun updateMoreDetailsText(text: String?) {
        runOnUiThread {
            moreDetailsTextView.text = Html.fromHtml(text)
        }
    }

    private fun textToHtml(text: String): String {
        return StringBuilder()
            .append(HTML_START)
            .append(HTML_FONT)
            .append(text)
            .append(HTML_END)
            .toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val TITLE_IMAGE_URL =  "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
        private const val NEW_YORK_TIMES_URL = "https://api.nytimes.com/svc/search/v2/"
        private val newYorkTimesRetrofit = Retrofit.Builder()
            .baseUrl(NEW_YORK_TIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val newYorkTimesAPI: NYTimesAPI = newYorkTimesRetrofit.create(NYTimesAPI::class.java)
    }
}