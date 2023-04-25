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
    }

    private fun initProperties() {
        moreDetailsTextView = findViewById(R.id.textMoreDetails)
        titleImageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
        getArtistInfo(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            var text = DataBase.getInfo(dataBase, artistName)
            val isInDataBase = text != null
            if (isInDataBase) {
                text = "[*]$text"
            } else {
                val callResponse: Response<String>
                try {
                    callResponse = newYorkTimesAPI.getArtistInfo(artistName).execute()
                    val jobj = Gson().fromJson(callResponse.body(), JsonObject::class.java)
                    val response = jobj[RESPONSE].asJsonObject
                    val abstract = response[DOCS].asJsonArray[0].asJsonObject[ABSTRACT]
                    val url = response[DOCS].asJsonArray[0].asJsonObject[WEB_URL]
                    if (abstract == null) {
                        text = "No Results"
                    } else {
                        text = abstract.asString.replace("\\n", "\n")
                        text = textToHtml(text, artistName)
                        DataBase.saveArtist(dataBase, artistName, text)
                    }
                    val urlString = url.asString
                    openUrlButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        startActivity(intent)
                    }
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            updateMoreDetailsText(text)
        }.start()
    }

    private fun updateTitleImageView() {
        runOnUiThread {
            imageLoader.loadImageIntoView(TITLE_IMAGE_URL, titleImageView)
        }
    }

    private fun updateMoreDetailsText(text: String) {
        runOnUiThread {
            moreDetailsTextView.text = Html.fromHtml(text)
        }
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
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