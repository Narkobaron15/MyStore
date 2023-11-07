package com.example.mystore.models.user

data class LoginModel (
    var username : String = "",
    var password : String = "",
    var rememberMe : Boolean = true,
)