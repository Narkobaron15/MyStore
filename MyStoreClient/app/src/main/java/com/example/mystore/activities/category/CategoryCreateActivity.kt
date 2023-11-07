package com.example.mystore.activities.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mystore.R
import com.example.mystore.activities.BaseActivity
import com.example.mystore.activities.MainActivity
import com.example.mystore.models.category.CategoryCreateModel
import com.example.mystore.models.category.CategoryModel
import com.example.mystore.network.ApiClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryCreateActivity : BaseActivity() {
    private var catImage: String? = null

    private lateinit var catName: TextInputLayout
    private lateinit var catDescription: TextInputLayout
    private lateinit var imagePickBtn : MaterialButton
    private lateinit var addBtn: MaterialButton
    private lateinit var imgView: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_create)

        catName = findViewById(R.id.nameField)
        catDescription = findViewById(R.id.descriptionField)
        imagePickBtn = findViewById(R.id.addImageBtn)
        addBtn = findViewById(R.id.addBtn)
        imgView = findViewById(R.id.ivSelectImage)

        imagePickBtn.setOnClickListener { _ ->
            pickMedia.launch(PickVisualMediaRequest())
        }

        addBtn.setOnClickListener(::addBtnOnClick)

        listenToConnectionStatusWithDefaultCallback()
    }

    // Receiver
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it == null) {
            return@registerForActivityResult
        }

        catImage = getPathFromURI(it)
        Picasso.get().load(it).into(imgView)
    }

    private fun addBtnOnClick(contextView: View) {
        val formData = getPicFormDataFromPath(catImage)
        val model = CategoryCreateModel(
            name = catName.editText?.text.toString().trim(),
            description = catDescription.editText?.text.toString().trim(),
        )

        val errors = CategoryValidator.isValid(model.name, model.description)
        if (!errors.isValid) {
            catName.error = errors.name
            catDescription.error = errors.description
            return
        }

        ApiClient.categoryService.createCategory(model.toMap(), formData)
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
                Snackbar.make(
                    contextView,
                    "Something went wrong",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}