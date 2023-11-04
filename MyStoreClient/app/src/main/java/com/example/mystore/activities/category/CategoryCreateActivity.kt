package com.example.mystore.activities.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mystore.R
import com.example.mystore.activities.BaseActivity
import com.example.mystore.activities.MainActivity
import com.example.mystore.models.category.CategoryCreateModel
import com.example.mystore.models.category.CategoryModel
import com.example.mystore.network.ApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryCreateActivity : BaseActivity() {
    private lateinit var catName: TextInputLayout
    private var catImage: String? = null
    private lateinit var catDescription: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_create)

        catName = findViewById(R.id.nameField)
        catDescription = findViewById(R.id.descriptionField)
    }

    // Receiver
    private val getResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            catImage = it.data?.getStringExtra("input")
            Log.d("CategoryCreateActivity", it.data?.data.toString())
        }
    }

    fun imgBtnOnClick(contextView: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        getResult.launch(intent)
    }

    fun sendBtnOnClick(contextView: View) {
        val name = catName.editText?.text.toString().trim()
        val image = "" //catImage.editText?.text.toString().trim()
        val description = catDescription.editText?.text.toString().trim()

        val errors = CategoryValidator.isValid(name, description)
        if (!errors.isValid) {
            catName.error = errors.name
            catDescription.error = errors.description
            return
        }

        val model = CategoryCreateModel(name, description, image)

        ApiClient.categoryService.createCategory(model)
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