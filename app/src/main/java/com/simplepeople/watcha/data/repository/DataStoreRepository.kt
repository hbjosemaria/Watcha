package com.simplepeople.watcha.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.simplepeople.watcha.data.modules.DataStoreVariableType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DataStoreRepository {
    suspend fun <T> saveData(dataType: DataStoreVariableType, dataName: String, data: T)
    suspend fun <T> loadData(dataName: List<Pair<DataStoreVariableType, String>>): List<Pair<String, T?>>
}

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {

    override suspend fun <T> saveData(
        dataType: DataStoreVariableType,
        dataName: String,
        data: T,
    ) {
        dataStore.edit { preferences ->
            when (dataType) {
                DataStoreVariableType.BooleanType -> {
                    preferences[booleanPreferencesKey(dataName)] = data as Boolean
                }

                DataStoreVariableType.IntType -> {
                    preferences[intPreferencesKey(dataName)] = data as Int
                }

                DataStoreVariableType.LongType -> {
                    preferences[longPreferencesKey(dataName)] = data as Long
                }

                DataStoreVariableType.StringType -> {
                    preferences[stringPreferencesKey(dataName)] = data as String
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> loadData(
        dataName: List<Pair<DataStoreVariableType, String>>,
    ): List<Pair<String, T?>> {
        val loadedDataList: MutableList<Pair<String, T?>> = mutableListOf()
        dataStore.data.map { preferences ->
            dataName.forEach { (dataType, dataName) ->
                val data = when (dataType) {
                    DataStoreVariableType.BooleanType -> {
                        preferences[booleanPreferencesKey(dataName)]
                    }

                    DataStoreVariableType.IntType -> {
                        preferences[intPreferencesKey(dataName)]
                    }

                    DataStoreVariableType.LongType -> {
                        preferences[longPreferencesKey(dataName)]
                    }

                    DataStoreVariableType.StringType -> {
                        preferences[stringPreferencesKey(dataName)]
                    }
                } as T?
                loadedDataList.add(Pair(dataName, data))
            }
        }.first()
        return loadedDataList
    }
}