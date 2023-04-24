package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
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
import java.util.*

class OtherInfoWindow : AppCompatActivity() {

    private lateinit var moreDetailsTextView: TextView
    private lateinit var titleImageView: ImageView
    private lateinit var dataBase: DataBase
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initProperties()
        initDataBase()
    }

    private fun initProperties() {
        moreDetailsTextView = findViewById(R.id.textMoreDetails)
        titleImageView = findViewById(R.id.imageView)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
        getArtistInfo(intent.getStringExtra(ARTIST_NAME_EXTRA))
    }

    private fun getArtistInfo(artistName: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val NYTimesAPI = retrofit.create(NYTimesAPI::class.java)
        Thread {
            var text = DataBase.getInfo(dataBase, artistName)
            if (text != null) { // exists in db
                text = "[*]$text"
            } else { // get from service
                val callResponse: Response<String>
                try {
                    callResponse = NYTimesAPI.getArtistInfo(artistName).execute()
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val response = jobj["response"].asJsonObject
                    val _abstract = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val url = response["docs"].asJsonArray[0].asJsonObject["web_url"]
                    if (_abstract == null) {
                        text = "No Results"
                    } else {
                        text = _abstract.asString.replace("\\n", "\n")
                        text = textToHtml(text, artistName)
                        DataBase.saveArtist(dataBase, artistName, text)
                    }
                    val urlString = url.asString
                    findViewById<View>(R.id.openUrlButton).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        startActivity(intent)
                    }
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            val finalText = text
            runOnUiThread {
                moreDetailsTextView.text = Html.fromHtml(finalText)
            }
            updateTitleImageView()
        }.start()
    }

    private fun updateTitleImageView() {
        runOnUiThread {
            imageLoader.loadImageIntoView(TITLE_IMAGE_URL, titleImageView)
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val TITLE_IMAGE_URL =  "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
        fun textToHtml(text: String, term: String?): String {
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
    }
}