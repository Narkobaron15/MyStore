package com.example.mystore.network.auth

import com.example.mystore.models.user.LoginModel
import com.example.mystore.models.user.RegisterModel
import com.example.mystore.models.user.TokensModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuthQueries {
    @POST("/auth/login")
    fun login(@Body model: LoginModel): Call<TokensModel>

    @POST("/auth/refresh")
    fun refresh(@Body model: TokensModel): Call<TokensModel>

    @POST("/auth/logout")
    fun register(@Body model: RegisterModel): Call<Unit>
}