package com.example.mystore.activities

import com.example.mystore.sharedprefs.SessionManager
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.R
import com.example.mystore.activities.auth.LoginActivity
import com.example.mystore.activities.category.CategoryCreateActivity
import com.example.mystore.adapters.CategoryAdapter
import com.example.mystore.application.HomeApplication
import com.example.mystore.models.category.CategoryModel
import com.example.mystore.models.user.TokensModel
import com.example.mystore.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (sessionManager.fetchAuthToken() == null) {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        refreshTokens()

        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        listenToConnectionStatus(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) = fetchCategories(recyclerView)
                override fun onLost(network: Network) = showNoInternetSnackbar()
        })
    }

    fun fabOnClick(contextView: View)
        = startActivity(Intent(this, CategoryCreateActivity::class.java))

    private fun fetchCategories(recyclerView: RecyclerView) {
        ApiClient.categoryService.getCategories()
            .enqueue(object : Callback<MutableList<CategoryModel>> {
                override fun onResponse(
                    call: Call<MutableList<CategoryModel>>,
                    response: Response<MutableList<CategoryModel>>
                ) {
                    val categories = response.body() ?: listOf()
                    Log.w("CATEGORIES", response.body().toString())
                    Log.d("CATEGORIES", categories.toString())
                    recyclerView.adapter = CategoryAdapter(categories)
                }

                override fun onFailure(
                    call: Call<MutableList<CategoryModel>>,
                    t: Throwable) {
                    showNoInternetSnackbar()
                }
            })
    }

    private fun refreshTokens() {
        if (sessionManager.fetchRefreshToken() == null) {
            return
        }

        val tokens = TokensModel(
            sessionManager.fetchAuthToken()!!,
            sessionManager.fetchRefreshToken()!!
        )

        ApiClient.authService.refresh(tokens)
            .enqueue(object : Callback<TokensModel> {
                override fun onResponse(
                    call: Call<TokensModel>,
                    response: Response<TokensModel>
                ) {
                    if (response.isSuccessful) {
                        val tokens = response.body()

                        sessionManager.saveAuthToken(tokens!!.accessToken!!)
                        sessionManager.saveRefreshToken(tokens.refreshToken!!)
                    } else {
                        onFailure(call, Throwable(response.message()))
                    }
                }

                override fun onFailure(call: Call<TokensModel>, t: Throwable) {
                    Log.e("REFRESH TOKEN", t.message.toString())
                }
            })
    }
}