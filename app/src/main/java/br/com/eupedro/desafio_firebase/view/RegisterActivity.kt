package br.com.eupedro.desafio_firebase.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import br.com.eupedro.desafio_firebase.R
import br.com.eupedro.desafio_firebase.utils.validateEmailValue
import br.com.eupedro.desafio_firebase.utils.validateFieldValue
import br.com.eupedro.desafio_firebase.utils.validateRequiredField
import br.com.eupedro.desafio_firebase.viewModel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private val btRegisterSave : AppCompatButton by lazy {
        findViewById(R.id.btRegisterSave)
    }

    private val edRegisterName: TextInputEditText by lazy {
        findViewById(R.id.edRegisterName)
    }

    private val edRegisterEmail: TextInputEditText by lazy {
        findViewById(R.id.edRegisterEmail)
    }

    private val edRegisterPwd: TextInputEditText by lazy {
        findViewById(R.id.edRegisterPwd)
    }

    private val tlRegisterName: TextInputLayout by lazy {
        findViewById(R.id.tlRegisterName)
    }

    private val tlRegisterEmail: TextInputLayout by lazy {
        findViewById(R.id.tlRegisterEmail)
    }

    private val tlRegisterPwd: TextInputLayout by lazy {
        findViewById(R.id.tlRegisterPwd)
    }

    private val tlRegisterRePwd: TextInputLayout by lazy {
        findViewById(R.id.tlRegisterRePwd)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupObservables()

    }

    private fun setupObservables() {

        btRegisterSave.setOnClickListener {
            val password = edRegisterPwd.text.toString()

            // validaçoes dos campos
            val checkResult = tlRegisterName.validateRequiredField(R.string.name)
                .and(tlRegisterEmail.validateRequiredField(R.string.email))
                .and(tlRegisterPwd.validateRequiredField(R.string.password))
                .and(tlRegisterRePwd.validateRequiredField(R.string.repeat_password))
                .and(tlRegisterRePwd.validateFieldValue("Passwords mismatch", password))
                .and(tlRegisterEmail.validateEmailValue())

            if(checkResult) {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                userViewModel.registerUser(email, password, name)
            }
        }

//        userViewModel.erroMsg.observe(this) {
//            Toast.makeText(baseContext, it,
//                Toast.LENGTH_SHORT).show()
//        }
//
        userViewModel.userRegister.observe(this) {
            if(it) {
                // termina a atividade e volta ao login, que quando tiver usuario logado segue para a home
                finish()
            }
        }

    }

}