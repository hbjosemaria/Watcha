package com.simplepeople.watcha.data.model.external

import com.google.gson.annotations.SerializedName

data class RequestTokenDto(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("expires_at")
    val expiresAt: String = "",
    @SerializedName("request_token")
    val requestToken: String = "",
)