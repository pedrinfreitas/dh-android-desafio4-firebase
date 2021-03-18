package br.com.eupedro.desafio_firebase.model

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameList(
    @DocumentId
    val uid: String? = null,
    val gameName: String? = null,
    val gameDescription: String? = null,
    val gameCreateAt: String? = null,
    @Exclude @set:Exclude @get:Exclude
    var gameImage: Uri? = null
) : Parcelable