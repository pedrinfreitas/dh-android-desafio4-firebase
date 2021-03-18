package br.com.eupedro.desafio_firebase.repository

import android.net.Uri
import android.util.Log
import br.com.eupedro.desafio_firebase.model.GameList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.tasks.await

class GameRepository {

    val db by lazy {
        FirebaseFirestore.getInstance()
    }

    private val storageRef by lazy {
        Firebase.storage.reference
    }

    suspend fun getAllGames(uid: String): List<GameList> {
        try {
            val data = db.collection("users")
                .document(uid)
                .collection("games")
                .get()
                .await()

            val games = data?.toObjects<GameList>() ?: emptyList()
            games.map { game ->
                game.gameImage =
                    getGameImage(uid, game.gameName?.toLowerCase()?.replace(" ", "_") ?: "")
            }

            return games

        } catch (e: Exception) {
            Log.i("Teste1", e.message.toString())
            throw Exception("Error getting documents")
        }
    }

    suspend fun getGame(uid: String, gameRef: String): GameList? {
        try {
            val data = db.collection("users")
                .document(uid)
                .collection("games")
                .document(gameRef)
                .get()
                .await()

            val game = data?.toObject<GameList>()
            game?.gameImage =
                getGameImage(uid, game?.gameName?.toLowerCase()?.replace(" ", "_") ?: "")

            return game

        } catch (e: Exception) {
            Log.i("Teste1", e.message.toString())
            throw Exception("Error getting documents")
        }
    }

    suspend fun getGameImage(uid: String, gameName: String): Uri? {
        return try {
            val fileRef = storageRef.child("${uid}/${gameName}.jpg")
            fileRef.downloadUrl.await()
        } catch (e: Exception) {
            null
        }

    }

    suspend fun saveGame(gameList: GameList, uid: String) {
        try {
            db.collection("users")
                .document(uid)
                .collection("games")
                .document(gameList.gameName ?: "")
                .set(gameList)
                .await()

            var metadata = storageMetadata {
                contentType = "image/jpg"
            }
            val fileRef = storageRef.child(
                "/${uid}/${gameList.gameName?.toLowerCase()?.replace(" ", "_")}.jpg" ?: ""
            )
            gameList.gameImage?.let {
                fileRef.putFile(it, metadata).await()
            }
        } catch (e: Exception) {
            throw(Exception("Falha ao gravar o jogo - ${e.localizedMessage}"))
        }
    }
}
