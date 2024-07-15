package com.simplepeople.watcha.data.repository

import com.google.firebase.Firebase
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.simplepeople.watcha.data.model.external.RequestTokenDto
import com.simplepeople.watcha.data.model.external.SessionDto
import com.simplepeople.watcha.data.model.external.TokenDto
import com.simplepeople.watcha.data.model.local.TmdbSessionIdEntity
import com.simplepeople.watcha.data.service.Room.TmdbSessionIdDao
import com.simplepeople.watcha.data.service.TmdbExternalAuthService
import javax.inject.Inject

interface ExternalAuthRepository {
    suspend fun getRequestToken(): RequestTokenDto
    suspend fun createSession(tokenDto: TokenDto): SessionDto
}

interface LocalAuthRepository {
    suspend fun saveSessionId(sessionId: TmdbSessionIdEntity): Long
    suspend fun getSessionId(email: String? = Firebase.auth.currentUser?.email): String?
}

class ExternalAuthRepositoryImpl @Inject constructor(
    private val apiService: TmdbExternalAuthService,
) : ExternalAuthRepository {
    override suspend fun getRequestToken(): RequestTokenDto =
        apiService.getRequestToken()

    override suspend fun createSession(tokenDto: TokenDto): SessionDto {
        return apiService.createSession(tokenDto)
    }
}

class LocalAuthRepositoryImpl @Inject constructor(
    private val roomService: TmdbSessionIdDao,
    private val firebaseAuth: FirebaseAuth,
) : LocalAuthRepository {
    override suspend fun saveSessionId(sessionId: TmdbSessionIdEntity): Long {
        return firebaseAuth.currentUser?.email?.let { email ->
            roomService.saveSessionId(
                sessionId.copy(
                    email = email
                )
            )
        } ?: throw FirebaseAuthException(
            FirebaseError.ERROR_USER_NOT_FOUND.toString(),
            "User is not signed in to save its session id"
        )
    }

    override suspend fun getSessionId(email: String?): String? =
        roomService.getSessionId(email)

}
