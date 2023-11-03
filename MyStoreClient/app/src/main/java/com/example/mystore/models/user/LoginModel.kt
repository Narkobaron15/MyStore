package com.example.mystore.models.user

data class LoginModel (
    var email : String = "",
    var password : String = "",
    var rememberMe : Boolean = false,
)