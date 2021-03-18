package br.com.eupedro.desafio_firebase.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import br.com.eupedro.desafio_firebase.R
import br.com.eupedro.desafio_firebase.utils.validateRequiredField
import br.com.eupedro.desafio_firebase.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private val btCreateAccount: AppCompatButton by lazy {
        findViewById(R.id.btCreateAccount)
    }

    private val btLogin: AppCompatButton by lazy {
        findViewById(R.id.btLogin)
    }

    private val tllogin_email: TextInputLayout by lazy {
        findViewById(R.id.tllogin_email)
    }

    private val tlPwd: TextInputLayout by lazy {
        findViewById(R.id.tlPwd)
    }

    private val edlogin_email: TextInputEditText by lazy {
        findViewById(R.id.edlogin_email)
    }

    private val edPwd: TextInputEditText by lazy {
        findViewById(R.id.edPwd)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupObservables()
    }

    override fun onResume() {
        super.onResume()
        // verifica se tem usuario logado
        userViewModel.validateUser()
    }

    fun setupObservables() {
        btCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btLogin.setOnClickListener {
            val checkValidates = tllogin_email.validateRequiredField(R.string.email)
                .and(tlPwd.validateRequiredField(R.string.password))

            if (checkValidates) {
                val email = edlogin_email.text.toString()
                val password = edPwd.text.toString()
                userViewModel.loginEmailSenha(email, password)
            }
        }

        userViewModel.userLogin.observe(this) { user ->
            user?.let {
                startActivity(Intent(this, GameHomeActivity::class.java))
                finish()
            }
        }
    }
}