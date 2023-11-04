package com.example.mystore.activities

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
                TODO()
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
}