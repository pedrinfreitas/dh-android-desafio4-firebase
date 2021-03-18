package br.com.eupedro.desafio_firebase.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import br.com.eupedro.desafio_firebase.R
import br.com.eupedro.desafio_firebase.model.GameList
import br.com.eupedro.desafio_firebase.utils.Constants.Permissions.KEY_INTENT_GAME
import br.com.eupedro.desafio_firebase.viewModel.GameViewModel
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GameDetailActivity : AppCompatActivity() {

    private var gameRef: String? = null

    private lateinit var gameViewModel: GameViewModel

    private val btnDetalhesVoltar: AppCompatImageButton by lazy {
        findViewById(R.id.btnDetalhesVoltar)
    }

    private val btnDetalhesEditar: FloatingActionButton by lazy {
        findViewById(R.id.btnDetalhesEditar)
    }

    private val ivDetalhesCapa: AppCompatImageView by lazy {
        findViewById(R.id.ivDetalhesCapa)
    }

    private val tvDetalhesTitulo: TextView by lazy {
        findViewById(R.id.tvDetalhesTitulo)
    }

    private val tvDetalhesSubtitulo: TextView by lazy {
        findViewById(R.id.tvDetalhesSubtitulo)
    }

    private val tvDetalhesDescricao: TextView by lazy {
        findViewById(R.id.tvDetalhesDescricao)
    }

    private val tvDetalhesAno: TextView by lazy {
        findViewById(R.id.tvDetalhesAno)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        supportActionBar?.hide()

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        gameRef = intent.getStringExtra(KEY_INTENT_GAME)
        setupObservables()
    }

    override fun onResume() {
        super.onResume()
        gameRef?.let {
            gameViewModel.getGame(it)
        }
    }

    private fun setupView(gameList: GameList) {
        gameList?.let { gameList ->
            Glide.with(this@GameDetailActivity)
                .load(gameList.gameImage)
                .into(ivDetalhesCapa)

            tvDetalhesTitulo.text = gameList.gameName
            tvDetalhesSubtitulo.text = gameList.gameName
            tvDetalhesDescricao.text = gameList.gameDescription
            tvDetalhesAno.text = gameList.gameCreateAt
        }
    }


    private fun setupObservables() {
        btnDetalhesEditar.setOnClickListener {
            val intent = Intent(this@GameDetailActivity, GameRegisterActivity::class.java)
            intent.putExtra(KEY_INTENT_GAME, gameViewModel.game.value)
            startActivity(intent)
        }

        btnDetalhesVoltar.setOnClickListener {
            finish()
        }

        gameViewModel.game.observe(this) {

            it?.let { game ->
                setupView(game)
            }
        }
    }

}