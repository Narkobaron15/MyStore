package com.example.mystore.network.auth

import com.example.mystore.models.user.LoginModel
import com.example.mystore.models.user.RegisterModel
import com.example.mystore.models.user.TokensModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiAuthQueries {
    @POST("/auth/login")
    fun login(@Body model: LoginModel): Call<TokensModel>

    @POST("/auth/refresh")
    fun refresh(@Body model: TokensModel): Call<TokensModel>

    @Multipart
    @POST("/auth/register")
    fun register(
        @PartMap model: MutableMap<String, RequestBody>,
        @Part image: MultipartBody.Part? = null
    ): Call<Unit>
}