package com.example.mystore.network

import com.example.mystore.network.auth.ApiAuthQueries
import com.example.mystore.network.category.ApiCategoryQueries

object ApiClient {
    private const val apiUrl = "https://narkobaron.ninja/"

    val categoryService: ApiCategoryQueries
        get() = RetrofitClient.getClient(apiUrl).create(ApiCategoryQueries::class.java)

    val authService: ApiAuthQueries
        get() = RetrofitClient.getClient(apiUrl).create(ApiAuthQueries::class.java)
}
