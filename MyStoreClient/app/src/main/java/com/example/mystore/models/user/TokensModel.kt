package com.example.mystore.models.user

data class TokensModel(
    var accessToken: String? = null,
    var refreshToken: String? = null
)