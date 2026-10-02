package com.github.intershitty.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Interceptor

val apiInterceptors = mutableListOf<Interceptor>()

val client by lazy {
    HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                },
                contentType = ContentType.Any
            )
        }

        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }

        engine {
            apiInterceptors.forEach {
                addInterceptor(it)
            }
        }
    }
}