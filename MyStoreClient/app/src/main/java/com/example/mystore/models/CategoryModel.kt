package com.example.mystore.models

import java.io.Serializable

data class CategoryModel(
    var id: Int? = null,
    var name: String? = null,
    var image: String? = null,
    var description: String? = null
) : Serializable