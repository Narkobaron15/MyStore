package com.example.mystore.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.R
import com.example.mystore.activities.category.CategoryCreateActivity
import com.example.mystore.activities.category.CategoryUpdateActivity
import com.example.mystore.adapters.CategoryAdapter
import com.example.mystore.models.CategoryModel
import com.example.mystore.network.ApiCommon
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    // Register an activity launcher for Wi-Fi menu
    private val activityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler)

        listenToConnectionStatus()
        if (!isNetworkAvailable()) showNoInternetSnackbar()
    }

    private fun fetchCategories(recyclerView: RecyclerView) {
        ApiCommon.categoryService.getCategories()
            .enqueue(object : Callback<MutableList<CategoryModel>> {
                override fun onResponse(
                    call: Call<MutableList<CategoryModel>>,
                    response: Response<MutableList<CategoryModel>>
                ) {
                    val categories = response.body() ?: listOf()
                    recyclerView.adapter = CategoryAdapter(categories)
                }

                override fun onFailure(
                    call: Call<MutableList<CategoryModel>>,
                    t: Throwable) {
                    showNoInternetSnackbar()
                }
            })
    }

    fun fabOnClick(contextView: View) {
        Intent (this, CategoryCreateActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun enableWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Create an intent to launch the Settings.Panel.ACTION_WIFI activity.
            val intent = Intent(Settings.Panel.ACTION_WIFI)
            // Launch the activity.
            activityResultLauncher.launch(intent)
        }
    }

    private fun showNoInternetSnackbar() {
        val contextView = findViewById<View>(android.R.id.content).rootView

        Snackbar.make(
            contextView,
            "Error occurred while fetching categories (check your connection)",
            Snackbar.LENGTH_LONG
        ).setAction("Enable Wi-Fi") {
            enableWifi()
        }.show()
    }

    // Create a listener for network status
    private fun listenToConnectionStatus() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

        val connectivityManager = getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        connectivityManager.requestNetwork(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    fetchCategories(recyclerView)
                }

                override fun onLost(network: Network) {
                    showNoInternetSnackbar()
                }
            })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(
            CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}