package com.example.mystore.network.category

import com.example.mystore.models.category.CategoryModel
import com.example.mystore.models.category.CategoryUpdateModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiCategoryQueries {
    @GET("/categories")
    fun getCategories(): Call<MutableList<CategoryModel>>

    @GET("/categories/{id}")
    fun getCategory(@Path("id") id: Int): Call<CategoryModel>

    @Multipart
    @POST("/categories/create")
    fun createCategory(
        @PartMap model: MutableMap<String, RequestBody>,
        @Part image: MultipartBody.Part? = null
    ): Call<CategoryModel>

    @Multipart
    @PUT("/categories/update")
    fun updateCategory(
        @PartMap model: MutableMap<String, RequestBody>,
        @Part image: MultipartBody.Part? = null
    ): Call<CategoryModel>

    @DELETE("/categories/delete/{id}")
    fun deleteCategory(@Path("id") id: Int): Call<Unit>
}