package com.simplepeople.watcha.data.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREFERENCES = "preferences"
val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = PREFERENCES)

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        return PreferenceDataStoreFactory.create(
//            corruptionHandler = ReplaceFileCorruptionHandler(
//                produceNewData = { emptyPreferences() }
//            ),
//            migrations = listOf(SharedPreferencesMigration(context, PREFERENCES)),
//            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//            produceFile = { context.preferencesDataStoreFile(PREFERENCES) }
//        )
        return context.dataStore
    }
}

sealed class DataStoreVariableType {
    data object StringType : DataStoreVariableType()
    data object IntType : DataStoreVariableType()
    data object LongType : DataStoreVariableType()
    data object BooleanType : DataStoreVariableType()
}