package br.com.eupedro.desafio_firebase.business

import android.util.Log
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class userBusiness {
    private val auth by lazy {
        Firebase.auth
    }

    suspend fun registerUser(email: String, password: String): FirebaseUser? {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Log.i("Teste", "createUserWithEmail:success")
            return authResult.user
        } catch (e: Exception) {
            Log.w("Teste", "createUserWithEmail:failure", e)
            val message = when (e) {
                is FirebaseAuthUserCollisionException -> "Email já cadastrado"
                is FirebaseAuthWeakPasswordException -> e.localizedMessage
                else -> "Falha ao registrar o usuário"
            }
            throw(Exception(message))
        }
    }

    suspend fun updateUser(
        user: FirebaseUser, nome: String
    ): Boolean {

        try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build()

            user.updateProfile(profileUpdates).await()
            Log.i("Teste", "User profile updated.")
            return true
        } catch (e: Exception) {
            throw(Exception("Falha ao gravar dados do usuário"))
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun loginEmailSenha(email: String, senha: String): FirebaseUser? {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, senha)
                .await()

            Log.i("Teste", "signInWithEmail:success")
            return authResult.user

        } catch (e: Exception) {
            Log.w("Teste", "signInWithEmail:failure", e)

            val message = when (e) {
                is FirebaseAuthInvalidCredentialsException -> "Senha inválida"
                is FirebaseAuthInvalidUserException -> "Usuário inválido"
                else -> "Falha na autenticação"
            }
            throw(Exception(message))
        }
    }
}