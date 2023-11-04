package com.example.mystore.activities.category

object CategoryValidator {
    private fun isValidName(name: String?): String? {
        if (name == null) {
            return Errors.REQUIRED
        }
        if (name.length < 3 || name.length > 50) {
            return Errors.NAME
        }
        return null
    }

    private fun isValidDescription(description: String?): String? {
        if (description == null) {
            return Errors.REQUIRED
        }
        if (description.length < 3 || description.length > 255) {
            return Errors.DESCRIPTION
        }
        return null
    }

    fun isValid(name: String?, description: String?): Errors
    = Errors(
        isValidName(name),
        isValidDescription(description),
    )

    class Errors(
        var name: String? = null,
        var description: String? = null
    ) {
        companion object {
            const val NAME = "Name must be between 3 and 50 characters long"
            const val DESCRIPTION = """
                Description must be between 
                3 and 255 characters long"""
            const val REQUIRED = "This field is required"
        }

        var isValid: Boolean = name == null && description == null
    }
}
