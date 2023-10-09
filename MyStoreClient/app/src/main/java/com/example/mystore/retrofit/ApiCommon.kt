package com.example.mystore.retrofit

object ApiCommon {
    private const val apiUrl = "https://spu123.itstep.click/"

    val service: ApiQueries
        get() = RetrofitClient.getClient(apiUrl).create(ApiQueries::class.java)
}
