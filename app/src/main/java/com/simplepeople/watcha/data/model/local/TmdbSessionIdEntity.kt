package com.simplepeople.watcha.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simplepeople.watcha.domain.core.TmdbSessionId

@Entity(tableName = "tmdb_session_id")
data class TmdbSessionIdEntity(
    @PrimaryKey
    val email: String = "",
    val sessionId: String = "",
) {
    fun toDomain(): TmdbSessionId =
        TmdbSessionId(
            email = email,
            sessionId = sessionId
        )
}