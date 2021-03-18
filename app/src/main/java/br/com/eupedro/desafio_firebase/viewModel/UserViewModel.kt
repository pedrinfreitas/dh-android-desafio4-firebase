package br.com.eupedro.desafio_firebase.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.eupedro.desafio_firebase.business.userBusiness
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    val erroMsg: MutableLiveData<String> = MutableLiveData()
    val userRegister: MutableLiveData<Boolean> = MutableLiveData()
    val userLogin: MutableLiveData<FirebaseUser> = MutableLiveData()
    var userFirebase: FirebaseUser? = null

    private val userBusiness by lazy {
        userBusiness()
    }

    init {
        userRegister.value = false
        validateUser()
    }

    fun registerUser(
        email: String, password: String,
        name: String
    ) {
        viewModelScope.launch {
            try {
                val user = userBusiness.registerUser(email, password)
                user?.let {
                    userFirebase = it
                    userRegister.postValue(userBusiness.updateUser(user, name))
                }
            } catch (e: Exception) {
                erroMsg.postValue(e.message)
            }
        }
    }

    fun validateUser() {
        userBusiness.getCurrentUser()?.let {
            userLogin.postValue(it)
        }
    }

    fun loginEmailSenha(email: String, senha: String) {
        viewModelScope.launch {
            try {
                userLogin.postValue(userBusiness.loginEmailSenha(email, senha))
            } catch (e: Exception) {
                erroMsg.postValue(e.message)
            }
        }
    }

}