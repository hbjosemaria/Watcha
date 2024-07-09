package com.simplepeople.watcha.domain.usecase

import com.simplepeople.watcha.data.repository.CredentialRepository
import javax.inject.Inject

class CredentialsUseCase @Inject constructor(
    private val credentialRepository: CredentialRepository,
) {
    suspend fun signIn(): Boolean? {
        val credentialResponse = credentialRepository.getCredential()
        credentialResponse?.let {
            try {
                credentialRepository.signIn(it)
                return true
            } catch (e: Exception) {
                return false
            }
        } ?: return null
    }

    suspend fun createPasswordCredential(
        email: String,
        password: String,
    ): Boolean? {
        return credentialRepository
            .createPasswordCredential(
                email = email,
                password = password
            )
    }

    suspend fun logOut() = credentialRepository
        .logOut()

}