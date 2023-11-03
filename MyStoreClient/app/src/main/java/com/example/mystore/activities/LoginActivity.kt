package com.example.mystore.activities

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.mystore.R
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
    private val sessionManager = SessionManager(HomeApplication.getAppContext())
    private val errorMessage = "Invalid credentials"

    private lateinit var loginField : TextInputLayout
    private lateinit var pwdField : TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.loginBtn).setOnClickListener(::login)

        loginField = findViewById(R.id.usernameField)
        pwdField = findViewById(R.id.passwordField)

        val resetErrors = { _: CharSequence?, _: Any, _: Any, _: Any ->
            loginField.error = null
            pwdField.error = null
        }

        loginField.editText?.doOnTextChanged(resetErrors)
        pwdField.editText?.doOnTextChanged(resetErrors)
    }

    private fun login(view: View) {
        Log.d("LoginActivity", "lol xD")

        val login = loginField.editText?.text.toString().trim()
        val pwd = pwdField.editText?.text.toString().trim()
        val loginModel = LoginModel(login, pwd)

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
                    loginField.error = errorMessage
                    pwdField.error = errorMessage
                }
            }
        )
    }
}
