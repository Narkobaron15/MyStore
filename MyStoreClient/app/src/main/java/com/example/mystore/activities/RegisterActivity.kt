package com.example.mystore.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mystore.R
import com.example.mystore.models.user.RegisterModel
import com.example.mystore.network.ApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : BaseActivity() {
    private var catImage: String? = null
    private val emailField: TextInputLayout = findViewById(R.id.emailField)
    private val firstNameField: TextInputLayout = findViewById(R.id.firstNameField)
    private val lastNameField: TextInputLayout = findViewById(R.id.lastNameField)
    private val usernameField: TextInputLayout = findViewById(R.id.usernameField)
    private val passwordField: TextInputLayout = findViewById(R.id.passwordField)
    private val repeatPasswordField: TextInputLayout = findViewById(R.id.repeatPasswordField)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val imagePickBtn = findViewById<Button>(0)
            imagePickBtn.setOnClickListener { _ ->
            pickMedia.launch(PickVisualMediaRequest())
        }

        val registerBtn = findViewById<Button>(R.id.registerBtn)
            registerBtn.setOnClickListener(::onRegisterBtnClicked)
    }

    // Receiver
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it == null) {
            Log.d("RegisterActivity", "Bad :(")
            return@registerForActivityResult
        }

        catImage = getPathFromURI(it)
        Log.d("CategoryCreateActivity", catImage!!)
    }

    // Maybe needs to be redacted
    private fun onRegisterBtnClicked(contextView: View) {
        val file = File(catImage.toString())
        val img = RequestBody.create(MediaType.get("image/*"), file)
        val data = MultipartBody.Part.createFormData("image", file.name, img)
        val model = RegisterModel(
            email = emailField.editText.toString(),
            userName = usernameField.editText.toString(),
            password = passwordField.editText.toString(),
            firstName = firstNameField.editText.toString(),
            lastName = lastNameField.editText.toString(),
        )

        ApiClient.authService.register(data, model.toMap()).enqueue(
            object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    Log.d("Register", "Success")

                    // Return to logging-in intent
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