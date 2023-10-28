package com.example.mystore.activities.category

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mystore.R
import com.example.mystore.activities.BaseActivity
import com.example.mystore.activities.MainActivity
import com.example.mystore.models.CategoryCreateModel
import com.example.mystore.models.CategoryModel
import com.example.mystore.models.CategoryUpdateModel
import com.example.mystore.network.ApiCommon
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryUpdateActivity : BaseActivity() {
    private lateinit var catName: TextInputLayout
    private lateinit var catImage: TextInputLayout
    private lateinit var catDescription: TextInputLayout

    private lateinit var cat : CategoryModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_update)

        catName = findViewById(R.id.nameField)
        catImage = findViewById(R.id.imageField)
        catDescription = findViewById(R.id.descriptionField)

        val bundle = intent.extras
        if (bundle != null) {
            cat =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    bundle.getSerializable("model", CategoryModel::class.java)!!
                else bundle.getSerializable("model") as CategoryModel

            catName.editText?.setText(cat.name)
            catImage.editText?.setText(cat.image)
            catDescription.editText?.setText(cat.description)
        }
    }

    fun putBtnOnClick(contextView: View) {
        val model = CategoryUpdateModel(
            cat.id,
            catName.editText?.text.toString().trim(),
            catImage.editText?.text.toString().trim(),
            catDescription.editText?.text.toString().trim()
        )

        val errors = CategoryValidator.isValid(model.name, model.description, model.image)
        if (!errors.isValid) {
            catName.error = errors.name
            catDescription.error = errors.description
            catImage.error = errors.image
            return
        }

        ApiCommon.categoryService.updateCategory(model)
            .enqueue(object: Callback<CategoryModel> {
            override fun onResponse(
                call: Call<CategoryModel>,
                response: Response<CategoryModel>
            ) {
                Log.w("CategoryUpdateActivity", response.message())
                if (!response.isSuccessful) return

                val intent = Intent(
                    this@CategoryUpdateActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
            }

            override fun onFailure(call: Call<CategoryModel>, t: Throwable) {
                Log.e("CategoryUpdateActivity", t.message.toString())
                Snackbar.make(
                    contextView,
                    "Something went wrong",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}