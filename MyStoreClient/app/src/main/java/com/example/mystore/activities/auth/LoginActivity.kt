package com.example.mystore.activities.auth

import com.example.mystore.sharedprefs.SessionManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.mystore.R
import com.example.mystore.activities.BaseActivity
import com.example.mystore.activities.MainActivity
import com.example.mystore.application.HomeApplication
import com.example.mystore.models.user.LoginModel
import com.example.mystore.models.user.TokensModel
import com.example.mystore.network.ApiClient
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// https://medium.com/android-news/token-authorization-with-retrofit-android-oauth-2-0-747995c79720

class LoginActivity : AppCompatActivity() {
    private val errorMessage = "Invalid credentials"
    private val sessionManager = SessionManager(HomeApplication.getAppContext())

    private lateinit var usernameField : TextInputLayout
    private lateinit var pwdField : TextInputLayout
    private lateinit var registerTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.loginBtn).setOnClickListener(::login)

        usernameField = findViewById(R.id.unameField)
        pwdField = findViewById(R.id.passwordField)
        registerTextView = findViewById(R.id.registerTextView)

        val resetErrors = { _: CharSequence?, _: Any, _: Any, _: Any ->
            usernameField.error = null
            pwdField.error = null
        }

        usernameField.editText?.doOnTextChanged(resetErrors)
        pwdField.editText?.doOnTextChanged(resetErrors)

        registerTextView.isClickable = true
        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

//        listenToConnectionStatusWithDefaultCallback()
    }

    private fun login(view: View) {
        val username = usernameField.editText?.text.toString().trim()
        val pwd = pwdField.editText?.text.toString().trim()
        val loginModel = LoginModel(username, pwd)

        val errors = AuthValidator.validateLogin(username, pwd)
        if (!errors.isValid) {
            usernameField.error = errors.email
            pwdField.error = errors.password
            return
        }

        ApiClient.authService.login(loginModel).enqueue(
            object : Callback<TokensModel> {
                override fun onResponse(
                    call: Call<TokensModel>,
                    response: Response<TokensModel>
                ) {
                    if (response.isSuccessful) {
                        val tokens = response.body()

                        sessionManager.saveAuthToken(tokens!!.accessToken!!)
                        sessionManager.saveRefreshToken(tokens.refreshToken!!)

                        Intent(this@LoginActivity, MainActivity::class.java).also {
                            startActivity(it)
                        }
                    } else {
                        onFailure(call, Throwable(response.message()))
                    }
                }

                override fun onFailure(call: Call<TokensModel>, t: Throwable) {
                    Log.e("LoginActivity", t.message.toString())
                    usernameField.error = errorMessage
                    pwdField.error = errorMessage
                }
            }
        )
    }
}
