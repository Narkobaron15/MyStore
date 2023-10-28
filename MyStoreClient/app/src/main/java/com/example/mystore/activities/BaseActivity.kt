package com.example.mystore.activities

import android.content.Intent
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
}