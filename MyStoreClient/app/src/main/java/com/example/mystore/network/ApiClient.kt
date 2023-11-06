package com.example.mystore.network

import com.example.mystore.network.auth.ApiAuthQueries
import com.example.mystore.network.category.ApiCategoryQueries

object ApiClient {
//   private const val apiUrl = "http://narkobaron.ninja/"
     private const val apiUrl = "http://10.0.2.2:80/"

    val categoryService: ApiCategoryQueries
        get() = RetrofitClient.getClient(apiUrl).create(ApiCategoryQueries::class.java)

    val authService: ApiAuthQueries
        get() = RetrofitClient.getClient(apiUrl).create(ApiAuthQueries::class.java)
}
