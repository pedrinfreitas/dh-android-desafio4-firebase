package br.com.eupedro.desafio_firebase.business

import android.net.Uri
import android.util.Log
import br.com.eupedro.desafio_firebase.model.GameList
import br.com.eupedro.desafio_firebase.repository.GameRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("UNREACHABLE_CODE")
class GameBusiness {
    private val gameRepository by lazy {
        GameRepository()
    }

    private val auth by lazy {
        Firebase.auth
    }

    suspend fun getAllGames(): List<GameList> {
        auth.currentUser?.let {
            try {
                return gameRepository.getAllGames(it.uid)
            } catch (e: Exception) {
                Log.i("Teste", e.message.toString())
                throw e
            }
        }
        return emptyList()
    }

    suspend fun getGame(gameRef: String): GameList? {
        auth.currentUser?.let {
            try {
                return gameRepository.getGame(it.uid, gameRef)
            } catch (e: Exception) {
                Log.i("Teste", e.message.toString())
                throw e
            }
        }
        return null
    }

    suspend fun saveGame(
        gameName: String,
        gameCreateAt: String,
        gameDescription: String,
        gameImage: Uri?
    ) {
        try {
            val gameList = GameList(
                auth.currentUser?.uid ?: "",
                gameName,
                gameDescription,
                gameCreateAt,
                gameImage
            )
            gameRepository.saveGame(gameList, auth.currentUser?.uid ?: "")
        } catch (e: Exception) {
            throw e
        }
    }

}