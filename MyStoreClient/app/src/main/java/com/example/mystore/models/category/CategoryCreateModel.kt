package com.example.mystore.models.category

import com.example.mystore.models.Helper.createPartFromString
import okhttp3.RequestBody

data class CategoryCreateModel (
    var name: String,
    var description: String,
) {
    fun toMap(): MutableMap<String, RequestBody> = mutableMapOf(
        "name" to createPartFromString(name),
        "description" to createPartFromString(description),
    )
}