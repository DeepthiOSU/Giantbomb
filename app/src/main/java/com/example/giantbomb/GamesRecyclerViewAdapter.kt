package com.example.giantbomb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giantbomb.network.Results
import java.util.*

class GamesRecyclerViewAdapter(
    private val context: Context,
    private val viewModel: GamesResultsViewModel,
    var onItemClick: ((Results) -> Unit)? = null
) : RecyclerView.Adapter<GamesRecyclerViewAdapter.ViewHolder>(), Filterable {

    private var filteredGames = viewModel.gameResults.value?.toMutableList() ?: mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameName: TextView = itemView.findViewById(R.id.name)
        val gameImage: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.gameName.text = filteredGames[position].name
        Glide.with(context)
            .load(filteredGames[position].image.original_url)
            .fitCenter()
            .into(holder.gameImage)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(filteredGames[position])
        }
    }

    override fun getItemCount(): Int {
        return filteredGames.size
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint.toString()
            val queryResults =
                if (query.isEmpty()) {
                    viewModel.gameResults.value?.toMutableList() ?: mutableListOf()
                } else {
                    viewModel.gameResults.value?.filter {
                        val formattedQuery = query.lowercase(Locale.US)
                        it.name.contains(formattedQuery)
                    }?.toMutableList() ?: mutableListOf()
                }
            return FilterResults().apply { values = queryResults }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredGames = results?.values?.let {
                it as? MutableList<Results>
            } ?: mutableListOf()
            notifyDataSetChanged()
        }
    }
}
