package com.example.mystore.activities.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mystore.R
import com.example.mystore.activities.BaseActivity
import com.example.mystore.models.user.RegisterModel
import com.example.mystore.network.ApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : BaseActivity() {
    private var userImage: String? = null

    private lateinit var emailField: TextInputLayout
    private lateinit var firstNameField: TextInputLayout
    private lateinit var lastNameField: TextInputLayout
    private lateinit var usernameField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var repeatPasswordField: TextInputLayout
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailField = findViewById(R.id.emailField)
        firstNameField = findViewById(R.id.firstNameField)
        lastNameField = findViewById(R.id.lastNameField)
        usernameField = findViewById(R.id.unameField)
        passwordField = findViewById(R.id.passwordField)
        repeatPasswordField = findViewById(R.id.repeatPasswordField)
        imageView = findViewById(R.id.ivSelectImage)

        val imagePickBtn = findViewById<Button>(R.id.userImgBtn)
        imagePickBtn.setOnClickListener { _ ->
            pickMedia.launch(PickVisualMediaRequest())
        }

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener(::onRegisterBtnClicked)

        listenToConnectionStatusWithDefaultCallback()
    }

    // Receiver
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it == null) {
            return@registerForActivityResult
        }

        userImage = getPathFromURI(it)
        Picasso.get().load(it).into(imageView)
    }

    // Maybe needs to be redacted
    private fun onRegisterBtnClicked(contextView: View) {
        var data: MultipartBody.Part? = null
        if (userImage != null) {
            val file = File(userImage.toString())
            val img = RequestBody.create(MediaType.get("image/*"), file)
            data = MultipartBody.Part.createFormData("image", file.name, img)
        }

        val model = RegisterModel(
            email = emailField.editText?.text.toString(),
            userName = usernameField.editText?.text.toString(),
            password = passwordField.editText?.text.toString(),
            firstName = firstNameField.editText?.text.toString(),
            lastName = lastNameField.editText?.text.toString(),
        )

        val errors = AuthValidator.validateRegister(
            model.email, model.firstName, model.lastName, model.userName,
            model.password, repeatPasswordField.editText?.text.toString()
        )
        if (!errors.isValid) {
            emailField.error = errors.email
            usernameField.error = errors.username
            firstNameField.error = errors.firstName
            lastNameField.error = errors.lastName
            usernameField.error = errors.username
            passwordField.error = errors.password
            repeatPasswordField.error = errors.repeatPassword

            return
        }

        ApiClient.authService.register(model.toMap(), data).enqueue(
            object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    startActivity(Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    ))
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("Register", t.message.toString())
                    Snackbar.make(
                        findViewById<View>(android.R.id.content).rootView,
                        t.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
    }
}