package com.example.mystore.models.category

data class CategoryUpdateModel(
    var id: Int? = null,
    var name: String? = null,
    var image: String? = null,
    var description: String? = null
)
