package com.simplepeople.watcha.data.model.external

import com.google.gson.annotations.SerializedName

data class SessionDto (
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("session_id")
    val sessionId: String = "",
    @SerializedName("failure")
    val failure: Boolean = true,
    @SerializedName("status_code")
    val statusCode: Int = 0,
    @SerializedName("status_message")
    val statusMessage: String = ""
)