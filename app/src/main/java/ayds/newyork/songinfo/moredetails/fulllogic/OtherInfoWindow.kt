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
import com.google.gson.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"

class OtherInfoWindow : AppCompatActivity() {

    private lateinit var moreDetailsTextView: TextView
    private lateinit var titleImageView: ImageView
    private lateinit var openUrlButton: Button
    private lateinit var dataBase: DataBase
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        initDataBase()
        updateTitleImageView()
        getArtistInfo(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun initProperties() {
        moreDetailsTextView = findViewById(R.id.textMoreDetails)
        titleImageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            var text = getInfoFromDataBase(artistName)
            if (text == null) {
                val docs = getInfoFromAPI(artistName)
                val abstract = docs?.get(0)?.asJsonObject?.get(ABSTRACT)?.asString
                val url = docs?.get(0)?.asJsonObject?.get(WEB_URL)
                text = getTextFromAbstract(abstract, artistName)
                DataBase.saveArtist(dataBase, artistName, text)
                if (url != null)
                    initListeners(url.asString)
            }
            updateMoreDetailsText(text)
        }.start()
    }

    private fun getTextFromAbstract(abstract: String?, artistName: String?): String? {
        return if (abstract != null)
            getFormattedTextFromAbstract(abstract, artistName)
        else
            "No Results"
    }

    private fun getFormattedTextFromAbstract(abstract : String, artistName: String?) : String?{
        var text = abstract.replace("\\n", "\n")
        val textFormatted = textWithBold(text, artistName)
        text = textToHtml(textFormatted)
        return text
    }

    private fun textWithBold(text: String, artistName: String?): String {
        val textWithSpaces = text.replace("'", " ")
        val textWithLineBreaks = textWithSpaces.replace("\n", "<br>")
        val termUpperCase = artistName?.uppercase(Locale.getDefault())
        return textWithLineBreaks.replace("(?i)$artistName".toRegex(), "<b>$termUpperCase</b>")
    }

    private fun getInfoFromDataBase(artistName: String?) : String? {
        var text: String? = DataBase.getInfo(dataBase, artistName)
        if (text != null)
            text = "[*]$text"
        return text
    }

    private fun getInfoFromAPI(artistName: String?): JsonArray? {
        return try {
            val callResponse: Response<String> = newYorkTimesAPI.getArtistInfo(artistName).execute()
            val jobj = Gson().fromJson(callResponse.body(), JsonObject::class.java)
            val response = jobj[RESPONSE].asJsonObject
            val docs = response[DOCS].asJsonArray
            return if (docs.size()==0) null else docs
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun initListeners(urlString: String) {
        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
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
            .append("<html><div width=400>")
            .append("<font face=\"arial\">")
            .append(text)
            .append("</font></div></html>")
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