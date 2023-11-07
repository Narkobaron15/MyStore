package com.example.mystore.models

import okhttp3.MediaType
import okhttp3.RequestBody

object Helper {
    fun createPartFromString(str: String): RequestBody {
        return RequestBody.create(MediaType.get("text/plain"), str)
    }
}