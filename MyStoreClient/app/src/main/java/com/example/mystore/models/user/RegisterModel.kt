package com.example.mystore.models.user

data class RegisterModel(
    var email: String = "",
    var userName: String = "",
    var password: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var image: String? = null,
)