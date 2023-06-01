package ayds.newyork.songinfo.moredetails.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.utils.UtilsInjector
import com.squareup.picasso.Picasso

class CarouselAdapter(
    var cards: List<Card>,
    private val moreDetailsView: MoreDetailsView
): RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val card = cards[position]
        setImageView(holder.imageView, card.sourceLogoUrl)
        setTextView(holder.textSource, card.source.name)
        setTextViewHtml(holder.textMoreDetails, card.description)
        updateListenerUrl(holder.urlButton, card.infoUrl)
    }

    private fun setImageView(imageView: ImageView, image: String) {
        val picasso =  Picasso.get()
        val requestCreator = picasso.load(image)
        requestCreator.into(imageView)
    }

    private fun updateListenerUrl(urlButton: Button, infoUrl: String) {
        urlButton.setOnClickListener { UtilsInjector.navigationUtils.openExternalUrl(moreDetailsView, infoUrl) }
    }

    private fun setTextViewHtml(textView: TextView, text: String) {
        textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setTextView(textView: TextView, text: String) {
        textView.text = text
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    inner class CarouselViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textSource: TextView = itemView.findViewById(R.id.textSource)
        val textMoreDetails: TextView = itemView.findViewById(R.id.textMoreDetails)
        val urlButton: Button = itemView.findViewById(R.id.openUrlButton)
    }
}