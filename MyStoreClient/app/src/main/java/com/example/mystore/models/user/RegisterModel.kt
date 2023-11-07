package com.example.mystore.models.user

import com.example.mystore.models.Helper.createPartFromString
import okhttp3.MediaType
import okhttp3.RequestBody

data class RegisterModel(
    var email: String = "",
    var userName: String = "",
    var password: String = "",
    var firstName: String = "",
    var lastName: String = "",
) {
    fun toMap(): MutableMap<String, RequestBody> = mutableMapOf(
        "email" to createPartFromString(email),
        "userName" to createPartFromString(userName),
        "password" to createPartFromString(password),
        "firstName" to createPartFromString(firstName),
        "lastName" to createPartFromString(lastName),
    )
}