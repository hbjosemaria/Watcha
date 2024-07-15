package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.data.model.local.TmdbSessionIdEntity

data class TmdbSessionId(
    val email: String = "",
    val sessionId: String = "",
) {
    fun toEntity(): TmdbSessionIdEntity =
        TmdbSessionIdEntity(
            email = email,
            sessionId = sessionId
        )
}