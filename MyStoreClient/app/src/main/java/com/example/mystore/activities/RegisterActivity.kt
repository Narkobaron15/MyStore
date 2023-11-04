package com.example.mystore.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mystore.R
import com.example.mystore.models.user.RegisterModel
import com.example.mystore.network.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : BaseActivity() {
    private var catImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<Button>(0).setOnClickListener { _ ->
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            getResult.launch(intent)
        }
    }

    // Receiver
    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            catImage = getPathFromURI(it.data?.data!!)
            Log.d("CategoryCreateActivity", catImage!!)

            val file = File(catImage.toString())
            val img = RequestBody.create(MediaType.get("image/*"), file)
            val data = MultipartBody.Part.createFormData("image", file.name, img)
            val model = RegisterModel(
                email = "vova@gmail.com",
                userName = "uname",
                password = "@Localhost1",
                firstName = "Vova",
                lastName = "Melnyk",
            )

            ApiClient.authService.register(data, model.toMap()).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Log.d("Register", "Success")
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("Register", t.message.toString())
                }
            })
        }
    }
}