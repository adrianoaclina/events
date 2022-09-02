package br.com.events.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.events.home.R
import br.com.events.home.domain.entity.Event
import br.com.events.home.presentation.homefragment.HomeDelegate
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeListAdapter(
    private val homeDelegate: HomeDelegate,
    private val items: MutableList<Event> = mutableListOf()
) : RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    private val mData: ArrayList<Event> = items as ArrayList<Event>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_event,
                parent, false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount() = items.size

    fun refresh(events: List<Event>){
        items.clear()
        items.addAll(events)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(event: Event) {
            val clItem : ConstraintLayout = itemView.findViewById(R.id.cl_item_event)
            val ivImage: ImageView = itemView.findViewById(R.id.iv_logo_item_event)
            val tvTitle: TextView = itemView.findViewById(R.id.tv_title_item_event)
            val tvDescription: TextView = itemView.findViewById(R.id.tv_description_item_event)
            Picasso.get().load(event.image).into(ivImage)
            tvTitle.text = event.title
            tvDescription.text = event.description
            val moeda = DecimalFormat.getCurrencyInstance(Locale("pt", "BR"))
            clItem.setOnClickListener {
                homeDelegate.navToEventDetails(event.id, event.title, moeda.format(event.price))
            }
        }

    }

}