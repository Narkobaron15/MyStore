package com.example.mystore.network.auth

import com.example.mystore.sharedprefs.SessionManager
import com.example.mystore.application.HomeApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    private val sessionManager = SessionManager(HomeApplication.getInstance())

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        sessionManager.fetchAuthToken().let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}