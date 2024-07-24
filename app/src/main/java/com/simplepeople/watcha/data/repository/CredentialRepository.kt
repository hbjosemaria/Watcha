package com.simplepeople.watcha.data.repository

import android.content.Context
import android.util.Base64
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.simplepeople.watcha.BuildConfig
import kotlinx.coroutines.tasks.await
import java.security.SecureRandom
import javax.inject.Inject

interface CredentialRepository {
    suspend fun signIn(credentialResponse: GetCredentialResponse)
    suspend fun getCredential(): GetCredentialResponse?
    suspend fun createPasswordCredential(email: String, password: String): Boolean?
    suspend fun logOut()
    suspend fun deleteAccount()
}

class CredentialRepositoryImpl @Inject constructor(
    private val credentialManager: CredentialManager,
    private val firebaseAuth: FirebaseAuth,
    private val context: Context,
) : CredentialRepository {

    override suspend fun signIn(
        credentialResponse: GetCredentialResponse,
    ) {
        when (val credential = credentialResponse.credential) {
            is PasswordCredential -> {
                val email = credential.id
                val password = credential.password
                try {
                    firebaseAuth.signInWithEmailAndPassword(email, password).await()
                } catch (e: Exception) {
                    throw Exception("Could not sign it with Email and Password in Firebase")
                }
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken
                        val firebaseGoogleCredential =
                            GoogleAuthProvider.getCredential(idToken, null)
                        firebaseAuth.signInWithCredential(firebaseGoogleCredential).await()
                    } catch (e: GoogleIdTokenParsingException) {
                        throw Exception("Could not sign in with Google in Firebase")
                    }
                } else {
                    throw Exception("Not supported federated credential")
                }
            }

            else -> {
                throw Exception("Not supported credential")
            }
        }
    }

    override suspend fun getCredential(): GetCredentialResponse? {
        val passwordOption = GetPasswordOption()
        val googleIdOption = getGoogleIdOptionInstance()
        val credentialRequest = GetCredentialRequest(
            credentialOptions = listOf(
                passwordOption,
                googleIdOption
            )
        )
        return try {
            credentialManager.getCredential(
                context = context,
                request = credentialRequest
            )
        } catch (e: GetCredentialException) {
            null
        }
    }

    override suspend fun createPasswordCredential(
        email: String,
        password: String,
    ): Boolean? {
        val passwordRequest = CreatePasswordRequest(
            id = email,
            password = password
        )
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                credentialManager.createCredential(
                    context = context,
                    request = passwordRequest
                )
                return true
            } ?: return false
        } catch (e: CreateCredentialException) {
            return null
        } catch (e: FirebaseAuthUserCollisionException) {
            return null
        }
    }

    override suspend fun logOut() {
        firebaseAuth.signOut()
        val clearCredentialRequest = ClearCredentialStateRequest()
        credentialManager.clearCredentialState(
            clearCredentialRequest
        )
    }

    override suspend fun deleteAccount() {
        firebaseAuth.currentUser?.delete()
        val clearCredentialRequest = ClearCredentialStateRequest()
        credentialManager.clearCredentialState(
            clearCredentialRequest
        )
    }

    private fun getGoogleIdOptionInstance(): GetGoogleIdOption {
        val nonceBytes = ByteArray(60)
        SecureRandom().nextBytes(nonceBytes)
        val nonce = Base64.encodeToString(nonceBytes, Base64.URL_SAFE)
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.GOOGLE_ID_WEB_CLIENT)
            .setAutoSelectEnabled(true)
            .setNonce(nonce)
            .build()
    }
}


