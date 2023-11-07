package com.example.mystore.models.category

import com.example.mystore.models.Helper.createPartFromString
import okhttp3.RequestBody

data class CategoryUpdateModel(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null
) {
    fun toMap(): MutableMap<String, RequestBody> = mutableMapOf(
        "id" to createPartFromString(id.toString()),
        "name" to createPartFromString(name.toString()),
        "description" to createPartFromString(description.toString()),
    )
}
