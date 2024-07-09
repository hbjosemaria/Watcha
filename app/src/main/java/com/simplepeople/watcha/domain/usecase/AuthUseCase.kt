package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.model.external.TokenDto
import com.simplepeople.watcha.data.model.local.TmdbSessionIdEntity
import com.simplepeople.watcha.data.repository.ExternalAuthRepository
import com.simplepeople.watcha.data.repository.LocalAuthRepository
import com.simplepeople.watcha.data.repository.TmdbSession
import com.simplepeople.watcha.data.services.TmdbApiUrl
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val externalAuthRepository: ExternalAuthRepository,
    private val localAuthRepository: LocalAuthRepository,
) {
    suspend fun getRequestToken(): String =
        externalAuthRepository.getRequestToken().requestToken

    fun buildRequestTokenUrl(requestToken: String): String {
        return TmdbApiUrl.AUTH_URL.url
            .plus(requestToken)
            .plus("?redirect_to=${TmdbApiUrl.AUTH_REDIRECT_URL.url}")
    }

    fun checkIfTokenIsAuthorized(url: String) : Boolean {
        return url.startsWith(TmdbApiUrl.AUTH_REDIRECT_URL.url)
    }

    suspend fun createSession(requestToken: String) : Boolean {
        val token = TokenDto(
            requestToken = requestToken
        )
        val session = externalAuthRepository.createSession(token)
        return if (session.success) {
            TmdbSession.setSessionId(session.sessionId)
            saveSessionId(session.sessionId)
        } else false
    }

    private suspend fun saveSessionId(sessionId: String) : Boolean {
        val result = localAuthRepository.saveSessionId(
            TmdbSessionIdEntity(
                sessionId = sessionId
            )
        )
        return result > -1L
    }

    suspend fun loadSessionId() : Boolean {
        localAuthRepository.getSessionId()?.let {sessionId ->
            TmdbSession.setSessionId(sessionId)
            return true
        } ?: return false
    }

}