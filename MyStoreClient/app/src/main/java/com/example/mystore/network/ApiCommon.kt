package com.example.mystore.network

object ApiCommon {
    private const val apiUrl = "https://spu123.itstep.click/"

    val categoryService: ApiCategoryQueries
        get() = RetrofitClient.getClient(apiUrl).create(ApiCategoryQueries::class.java)
}
