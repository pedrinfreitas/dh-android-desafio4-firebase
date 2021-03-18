package br.com.eupedro.desafio_firebase.utils

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import br.com.eupedro.desafio_firebase.R
import br.com.eupedro.desafio_firebase.model.GameList
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList


class GamesAdapter(
    private val gameList: List<GameList>,
    private val progressbar: ProgressBar,
    private val onItemMenuClicked: (String) -> Unit,
) : RecyclerView.Adapter<GamesAdapter.ViewHolder>(), Filterable {

    var listGames = ArrayList<GameList>()

    init {
        listGames = ArrayList(gameList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_recycler_games, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listGames[position], progressbar, onItemMenuClicked)

    }

    override fun getItemCount(): Int {
        return listGames.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var ivGamePhoto: ImageView
        private lateinit var tvGameName: TextView
        private lateinit var tvGameCreateAt: TextView

        fun bind(list: GameList, progressbar: ProgressBar, onItemMenuClicked: (String) -> Unit) =
            with(itemView) {
                tvGameName = findViewById<TextView>(R.id.tvGameName)
                tvGameCreateAt = findViewById<TextView>(R.id.tvGameCreateAt)
                ivGamePhoto = findViewById<ImageView>(R.id.ivGamePhoto)

                Glide.with(itemView.context).load(list.gameImage).into(ivGamePhoto)
                tvGameName.text = list.gameName
                tvGameCreateAt.text = list.gameCreateAt

                ivGamePhoto.setOnClickListener {
                    onItemMenuClicked(list.gameName ?: "")
                }

                progressbar.visibility = GONE

            }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filtro = constraint?.toString()?.toLowerCase(Locale.ROOT)
                if (filtro.isNullOrEmpty()) {
                    listGames = ArrayList(gameList)
                } else {
                    val resultList = ArrayList<GameList>()
                    for (game in gameList) {
                        if (game.gameName?.toLowerCase(Locale.ROOT)?.contains(filtro) == true) {
                            resultList.add(game)
                        }
                    }
                    listGames = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listGames
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listGames = results?.values as ArrayList<GameList>
                notifyDataSetChanged()
            }
        }
    }
}
