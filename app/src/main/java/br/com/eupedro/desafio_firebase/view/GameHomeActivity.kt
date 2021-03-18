package br.com.eupedro.desafio_firebase.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.eupedro.desafio_firebase.R
import br.com.eupedro.desafio_firebase.utils.Constants.Permissions.KEY_INTENT_GAME
import br.com.eupedro.desafio_firebase.utils.GamesAdapter
import br.com.eupedro.desafio_firebase.viewModel.GameViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class GameHomeActivity : AppCompatActivity() {

    private val rvHomeGameList:RecyclerView by lazy {
        findViewById(R.id.rvHomeGameList)
    }

    private val searchView: androidx.appcompat.widget.SearchView by lazy {
        findViewById(R.id.searchView)
    }

    private val fabHomeAdicionar : FloatingActionButton by lazy {
        findViewById(R.id.fabHomeAdicionar)
    }

    private val progressbar : ProgressBar by lazy {
        findViewById(R.id.progressbar)
    }

    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_home)

        supportActionBar?.hide()

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
//                Log.i("Pesquisa", query)
                searchView.setQuery(query, true)
            }
        }

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        iniComponents()
        setupObervables()
    }

    override fun onResume() {
        super.onResume()
        iniComponents()
    }

    private fun iniComponents() {
        progressbar.visibility= VISIBLE
        gameViewModel.getAllGames()

        gameViewModel.games.observe(this) { list ->
            rvHomeGameList.apply {
                layoutManager = GridLayoutManager(
                    this@GameHomeActivity, 2,
                    GridLayoutManager.VERTICAL, false
                )
                adapter = GamesAdapter(list,progressbar) { position ->
                    val intent = Intent(this@GameHomeActivity, GameDetailActivity::class.java)
                    intent.putExtra(KEY_INTENT_GAME, position)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setupObervables(){

        // configuração da busca
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)

        searchView.setOnCloseListener {
//            Log.i("Pesquisa", "pesquisa foi limpada")
            true
        }

        searchView.setOnQueryTextListener(object: OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val adapter =rvHomeGameList.adapter as GamesAdapter
                    if(it.length >= 1) {
                        adapter.filter.filter(it)
                    } else {
                        adapter.filter.filter(null)
                    }

                }
                return false
            }
        })

        // botão adicionar
        fabHomeAdicionar.setOnClickListener {
            val intent = Intent(this, GameRegisterActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
//            Log.i("Pesquisa", query ?: "sem texto")
            query?.let {
                searchView.setQuery(query, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Firebase.auth.signOut()
    }

}
