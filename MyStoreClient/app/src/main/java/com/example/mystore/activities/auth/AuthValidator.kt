package com.example.mystore.activities.auth

object AuthValidator {
    // error messages
    private const val EMPTY_EMAIL = "Please enter your email"
    private const val INVALID_EMAIL = "Email is invalid"
    private const val EMPTY_FNAME = "Please enter your first name"
    private const val EMPTY_LNAME = "Please enter your last name"
    private const val EMPTY_USERNAME = "Please enter your username"
    private const val EMPTY_PWD = "Please enter your password"
    private const val WEAK_PWD = "Password should be stronger"
    private const val EMPTY_REPEAT_PWD = "Please repeat your password"
    private const val DIFF_PASSWORDS = "Passwords don't match"

    // regexes
    private val PWD_REGEX by lazy {
        """(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^\w\s]).{8,}""".toRegex()
    }
    private val EMAIL_REGEX by lazy {
        """^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$""".toRegex()
    }

    data class LoginErrors(
        val email: String? = null,
        val password: String? = null,
    ) {
        val isValid: Boolean = email == null && password == null
    }

    data class RegisterErrors(
        val email: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val username: String? = null,
        val password: String? = null,
        val repeatPassword: String? = null,
    ) {
        val isValid =   email == null && firstName == null &&
                        lastName == null && username == null &&
                        password == null && repeatPassword == null
    }

    fun validateLogin(username: String, password: String) = LoginErrors(
        email =     if (username.isEmpty()) EMPTY_USERNAME
                    else null,
        password =  if (password.isEmpty()) EMPTY_PWD
                    else if (!validatePassword(password)) WEAK_PWD
                    else null,
    )

    fun validateRegister(
        email: String,
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        repeatPassword: String
    ) = RegisterErrors(
        email =             if (email.isEmpty()) EMPTY_EMAIL
                            else if (!validateEmail(email)) INVALID_EMAIL
                            else null,
        firstName =         if (firstName.isEmpty()) EMPTY_FNAME
                            else null,
        lastName =          if (lastName.isEmpty()) EMPTY_LNAME
                            else null,
        username =          if (username.isEmpty()) EMPTY_USERNAME
                            else null,
        password =          if (password.isEmpty()) EMPTY_PWD
                            else null,
        repeatPassword =    if (repeatPassword.isEmpty()) EMPTY_REPEAT_PWD
                            else if (password != repeatPassword) DIFF_PASSWORDS
                            else if (!validatePassword(password)) WEAK_PWD
                            else null,
    )
    private fun validatePassword(password: String)
        = PWD_REGEX.matches(password)
    private fun validateEmail(email: String): Boolean
        = EMAIL_REGEX.matches(email)
}