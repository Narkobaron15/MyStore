package com.example.mystore.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mystore.R
import com.example.mystore.activities.category.CategoryCreateActivity
import com.google.android.material.snackbar.Snackbar


open class BaseActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.action_create -> {
                startActivity(Intent(this, CategoryCreateActivity::class.java))
                true
            }
            else -> {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Unknown menu item",
                    Snackbar.LENGTH_LONG
                ).show()

                true
            }
        }
    }

    // This method converts the image URI to the direct file system path of the image file
    protected fun getPathFromURI(contentUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            return filePath
        }
        return null
    }

    // Register an activity launcher for Wi-Fi menu
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    private fun enableWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Create an intent to launch the Settings.Panel.ACTION_WIFI activity.
            val intent = Intent(Settings.Panel.ACTION_WIFI)
            // Launch the activity.
            activityResultLauncher.launch(intent)
        }
    }

    protected fun showNoInternetSnackbar() {
        val contextView = findViewById<View>(android.R.id.content).rootView

        Snackbar.make(
            contextView,
            "Error occurred while fetching categories (check your connection)",
            Snackbar.LENGTH_LONG
        ).setAction("Enable Wi-Fi") {
            enableWifi()
        }.show()
    }

    protected fun showBackOnlineSnackbar() {
        val contextView = findViewById<View>(android.R.id.content).rootView

        Snackbar.make(
            contextView,
            "Back online :)",
            Snackbar.LENGTH_LONG
        ).show()
    }

    // Create a listener for network status
    protected fun listenToConnectionStatus(callback: NetworkCallback) {
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
            callback)

        if (connectivityManager.activeNetwork == null)
            showNoInternetSnackbar()
    }

    protected fun listenToConnectionStatusWithDefaultCallback() {
        listenToConnectionStatus(
            object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) = showNoInternetSnackbar()
            })
    }
}