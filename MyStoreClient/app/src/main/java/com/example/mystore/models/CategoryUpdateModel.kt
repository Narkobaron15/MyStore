package com.example.mystore.models

data class CategoryUpdateModel(
    var id: Int? = null,
    var name: String? = null,
    var image: String? = null,
    var description: String? = null
)
