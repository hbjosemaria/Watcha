package com.simplepeople.watcha.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.TimeZone

interface CacheRepository {

    val cacheVariableNameSuffix: String

    suspend fun clearMovieCacheExpiration()
    suspend fun loadMovieCacheExpiration(categoryId: Int): Long?
    suspend fun saveMovieCacheExpiration(categoryId: Int, cacheExpiration: Long)
    suspend fun forceCacheExpiration()
}

class CacheRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : CacheRepository {

    override val cacheVariableNameSuffix = "last_timeout_cache_category"

    override suspend fun clearMovieCacheExpiration() {
        dataStore.edit { preferences ->
            preferences.asMap().keys.map { preference ->
                if (preference.name.startsWith(cacheVariableNameSuffix)) {
                    preferences.remove(preference)
                }
            }
        }
    }

    override suspend fun forceCacheExpiration() {
        val cacheTimeout = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cacheTimeout.add(Calendar.DAY_OF_YEAR, -1)

        dataStore.edit { preferences ->
            preferences.asMap().keys.map { preference ->
                if (preference.name.startsWith(cacheVariableNameSuffix)) {
                    preferences[longPreferencesKey(preference.name)] = cacheTimeout.timeInMillis
                }
            }
        }
    }

    override suspend fun loadMovieCacheExpiration(categoryId: Int): Long? =
        dataStore.data.map { preferences ->
            preferences[longPreferencesKey(cacheVariableNameSuffix.plus(categoryId))]
        }.first()

    override suspend fun saveMovieCacheExpiration(categoryId: Int, cacheExpiration: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(cacheVariableNameSuffix.plus(categoryId))] =
                cacheExpiration
        }
    }


}
