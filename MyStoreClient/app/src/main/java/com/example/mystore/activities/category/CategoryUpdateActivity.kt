package com.example.mystore.activities.category

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mystore.R
import com.example.mystore.activities.BaseActivity
import com.example.mystore.activities.MainActivity
import com.example.mystore.models.category.CategoryModel
import com.example.mystore.models.category.CategoryUpdateModel
import com.example.mystore.network.ApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryUpdateActivity : BaseActivity() {
    private lateinit var catName: TextInputLayout
    private lateinit var catImageView: ImageView
    private lateinit var catDescription: TextInputLayout
    private lateinit var updateBtn: Button
    private lateinit var imagePickBtn: Button

    private lateinit var cat : CategoryModel
    private var catImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_update)

        catName = findViewById(R.id.nameField)
        catImageView = findViewById(R.id.updateImage)
        catDescription = findViewById(R.id.descriptionField)
        updateBtn = findViewById(R.id.updateBtn)
        imagePickBtn = findViewById(R.id.addImageBtn)

        val bundle = intent.extras
        if (bundle != null) {
            cat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    bundle.getSerializable("model", CategoryModel::class.java)!!
                  else bundle.getSerializable("model") as CategoryModel

            Picasso.get().load(cat.image.toString()).into(catImageView)
            catName.editText?.setText(cat.name)
            catDescription.editText?.setText(cat.description)
        }

        imagePickBtn.setOnClickListener { _ ->
            pickMedia.launch(PickVisualMediaRequest())
        }

        updateBtn.setOnClickListener(::updateBtnOnClick)
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
        Picasso.get().load(it).into(catImageView)
    }

    private fun updateBtnOnClick(contextView: View) {
        val formData = getPicFormDataFromPath(catImage)
        val model = CategoryUpdateModel(
            cat.id,
            catName.editText?.text.toString().trim(),
            catDescription.editText?.text.toString().trim()
        )

        val errors = CategoryValidator.isValid(model.name, model.description)
        if (!errors.isValid) {
            catName.error = errors.name
            catDescription.error = errors.description
            return
        }

        ApiClient.categoryService.updateCategory(model.toMap(), formData)
            .enqueue(object: Callback<CategoryModel> {
            override fun onResponse(
                call: Call<CategoryModel>,
                response: Response<CategoryModel>
            ) {
                if (!response.isSuccessful) return

                val intent = Intent(
                    this@CategoryUpdateActivity,
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