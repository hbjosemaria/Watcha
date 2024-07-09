package com.simplepeople.watcha.data.model.external

import com.google.gson.annotations.SerializedName

data class TokenDto (
    @SerializedName("request_token")
    val requestToken: String = ""
)