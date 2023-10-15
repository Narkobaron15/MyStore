package com.example.mystore.activities.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mystore.R
import com.example.mystore.activities.MainActivity
import com.example.mystore.models.CategoryCreateModel
import com.example.mystore.models.CategoryModel
import com.example.mystore.network.ApiCommon
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryCreateActivity : AppCompatActivity() {
    private lateinit var catName: TextInputLayout
    private lateinit var catImage: TextInputLayout
    private lateinit var catDescription: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_create)

        catName = findViewById(R.id.nameField)
        catImage = findViewById(R.id.imageField)
        catDescription = findViewById(R.id.descriptionField)
    }

    fun sendBtnOnClick(contextView: View) {
        val name = catName.editText?.text.toString().trim()
        val image = catImage.editText?.text.toString().trim()
        val description = catDescription.editText?.text.toString().trim()

        val errors = CategoryValidator.isValid(name, description, image)
        if (!errors.isValid) {
            catName.error = errors.name
            catDescription.error = errors.description
            catImage.error = errors.image
            return
        }

        val model = CategoryCreateModel(name, description, image)

        ApiCommon.categoryService.createCategory(model)
            .enqueue(object: Callback<CategoryModel> {
            override fun onResponse(
                call: Call<CategoryModel>,
                response: Response<CategoryModel>
            ) {
                if (!response.isSuccessful) return

                val intent = Intent(
                    this@CategoryCreateActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
            }

            override fun onFailure(call: Call<CategoryModel>, t: Throwable) {
                Log.e("CategoryCreateActivity", t.message.toString())
                Snackbar.make(
                    contextView,
                    "Something went wrong",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}