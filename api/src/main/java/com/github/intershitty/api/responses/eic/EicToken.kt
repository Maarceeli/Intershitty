package com.github.intershitty.api.responses.eic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EicToken (
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("refresh_token")
    val refreshToken: String,

    @SerialName("scope")
    val scope: String,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("expires_in")
    val expiresIn: Int
)