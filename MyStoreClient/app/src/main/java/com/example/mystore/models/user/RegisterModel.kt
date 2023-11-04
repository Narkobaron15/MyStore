package com.example.mystore.models.user

import okhttp3.MediaType
import okhttp3.RequestBody

data class RegisterModel(
    var email: String = "",
    var userName: String = "",
    var password: String = "",
    var firstName: String = "",
    var lastName: String = "",
) {
    fun toMap(): MutableMap<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()
        map["Email"] = createPartFromString(email)
        map["UserName"] = createPartFromString(userName)
        map["Password"] = createPartFromString(password)
        map["FirstName"] = createPartFromString(firstName)
        map["LastName"] = createPartFromString(lastName)
        return map
    }

    private fun createPartFromString(str: String): RequestBody {
        return RequestBody.create(MediaType.get("text/plain"), str)
    }
}