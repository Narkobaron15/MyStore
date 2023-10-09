package com.example.mystore.model

data class CategoryModel (
    var id: Int? = null,
    var name: String? = null,
    var image: String? = null,
    var description: String? = null
) {
    constructor() : this(0, "", "", "")
}
