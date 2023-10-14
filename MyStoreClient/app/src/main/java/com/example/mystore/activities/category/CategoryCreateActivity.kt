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

    companion object {
        private val NOT_EMPTY_REGEX = Regex("^[0-9a-zA-Z ]{3,}$")
        private val URL_REGEX = Regex("""(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]""")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_create)

        catName = findViewById(R.id.nameField)
        catImage = findViewById(R.id.imageField)
        catDescription = findViewById(R.id.descriptionField)
    }

    fun sendBtnOnClick(contextView: View) {
        val isValid = validate()
        if (!isValid) return

        val name = catName.editText?.text.toString().trim()
        val image = catImage.editText?.text.toString().trim()
        val description = catDescription.editText?.text.toString().trim()

        val model = CategoryCreateModel(name, description, image)

        ApiCommon.categoryService.createCategory(model).enqueue(object: Callback<CategoryModel> {
            override fun onResponse(call: Call<CategoryModel>, response: Response<CategoryModel>) {
                if (!response.isSuccessful) return

                Snackbar.make(
                    contextView,
                    "Successfully created a category",
                    Snackbar.LENGTH_LONG
                )/*.setAction()*/.show()

                val intent = Intent(this@CategoryCreateActivity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<CategoryModel>, t: Throwable) {
                Log.e("CategoryCreateActivity", t.message.toString())
            }
        })
    }

    private fun validate() : Boolean {
        val name = catName.editText?.text.toString().trim()
        val image = catImage.editText?.text.toString().trim()

        catName.error =
            if (name.isBlank()) "Name is required"
            else if (!NOT_EMPTY_REGEX.matches(name)) "Name must be at least 3 characters long"
            else null

        catImage.error =
            if (image.isEmpty()) "Image is required"
            else if (!URL_REGEX.matches(image)) "This should be a valid URL"
            else null

        return catName.error == null && catImage.error == null
    }
}