package com.example.mystore.network

object ApiCommon {
    private const val apiUrl = "https://mystoreapi.narkobaron.ninja/"

    val categoryService: ApiCategoryQueries
        get() = RetrofitClient.getClient(apiUrl).create(ApiCategoryQueries::class.java)
}
