package br.com.eupedro.desafio_firebase.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import br.com.eupedro.desafio_firebase.R
import br.com.eupedro.desafio_firebase.model.GameList
import br.com.eupedro.desafio_firebase.utils.Constants.Permissions.KEY_INTENT_GAME
import br.com.eupedro.desafio_firebase.utils.Constants.Permissions.PERMISSION_CODE
import br.com.eupedro.desafio_firebase.utils.validateRequiredField
import br.com.eupedro.desafio_firebase.viewModel.GameViewModel
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class GameRegisterActivity : AppCompatActivity() {

    private lateinit var gameViewModel: GameViewModel

    private val ivAvatar: ImageView by lazy {
        findViewById(R.id.ivAvatar)
    }

    private val fabAvatar: FloatingActionButton by lazy {
        findViewById(R.id.fabAvatar)
    }

    private val btRegisterSave: AppCompatButton by lazy {
        findViewById(R.id.btRegisterSave)
    }

    private val tlGameRegisterName: TextInputLayout by lazy {
        findViewById(R.id.tlGameRegisterName)
    }

    private val edGameRegisterName: TextInputEditText by lazy {
        findViewById(R.id.edGameRegisterName)
    }

    private val tlGameRegisterCreateAt: TextInputLayout by lazy {
        findViewById(R.id.tlGameRegisterCreateAt)
    }

    private val edGameRegisterCreateAt: TextInputEditText by lazy {
        findViewById(R.id.edGameRegisterCreateAt)
    }

    private val tlRegisterDescription: TextInputLayout by lazy {
        findViewById(R.id.tlRegisterDescription)
    }

    private val edRegisterDescription: TextInputEditText by lazy {
        findViewById(R.id.edRegisterDescription)
    }

    private val pbRegisterProgress: CircularProgressIndicator by lazy {
        findViewById(R.id.pbRegisterProgress)
    }

    private var gameRef: GameList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_register)

        supportActionBar?.hide()

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        gameRef = intent.getParcelableExtra<GameList>(KEY_INTENT_GAME)

        setupView()
        setupObservables()

    }

    private fun setupView() {
        gameRef?.let { game ->
            edGameRegisterName.setText(game.gameName)
            tlGameRegisterName.isEnabled = false
            edRegisterDescription.setText(game.gameDescription)
            edGameRegisterCreateAt.setText(game.gameCreateAt)
            ivAvatar.tag = "EDITANDO"

            Glide.with(this@GameRegisterActivity)
                .load(game.gameImage)
                .into(ivAvatar)
        }
    }

    private fun setupObservables() {
        fabAvatar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        btRegisterSave.setOnClickListener {
            pbRegisterProgress.show()
            if (ivAvatar.tag == null) {
                Toast.makeText(baseContext, "Image Required", Toast.LENGTH_SHORT).show()
                pbRegisterProgress.hide()
                return@setOnClickListener
            }

            val checkValidate = tlGameRegisterName.validateRequiredField(R.string.name)
                .and(tlRegisterDescription.validateRequiredField(R.string.description))
                .and(tlGameRegisterCreateAt.validateRequiredField(R.string.created_at))

            if (checkValidate) {
                val gameName = edGameRegisterName.text.toString()
                val gameCreateAt = edGameRegisterCreateAt.text.toString()
                val gameDescription = edRegisterDescription.text.toString()
                var gameImage: Uri? = null
                if (!ivAvatar.tag.equals("EDITANDO"))
                    gameImage = ivAvatar.tag as Uri
                gameViewModel.saveGame(gameName, gameCreateAt, gameDescription, gameImage)
            }

        }

        gameViewModel.errMessage.observe(this) { erroMsg ->
            Toast.makeText(
                baseContext, erroMsg,
                Toast.LENGTH_SHORT
            ).show()
        }

        gameViewModel.saveGame.observe(this) {
            if (it) {
                finish()
            }
            pbRegisterProgress.hide()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { img ->
                    ivAvatar.setImageURI(img)
                    ivAvatar.tag = img
                }
            }
        }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}