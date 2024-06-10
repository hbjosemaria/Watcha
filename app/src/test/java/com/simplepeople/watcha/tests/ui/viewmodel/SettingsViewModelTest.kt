package com.simplepeople.watcha.tests.ui.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.simplepeople.watcha.data.repository.CacheRepository
import com.simplepeople.watcha.data.repository.CacheRepositoryImpl
import com.simplepeople.watcha.data.repository.DataStoreRepository
import com.simplepeople.watcha.data.repository.DataStoreRepositoryImpl
import com.simplepeople.watcha.domain.usecase.CacheUseCase
import com.simplepeople.watcha.domain.usecase.SettingsUseCase
import com.simplepeople.watcha.ui.settings.SettingsViewModel
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    private lateinit var settingsUseCase: SettingsUseCase
    private lateinit var cacheUseCase: CacheUseCase
    private lateinit var dataStoreRepository: DataStoreRepository
    private lateinit var cacheRepository: CacheRepository
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var dataStore : DataStore<Preferences>


    @Before
    fun setUp() = runBlocking {
//        dataStore = FakeDataStore()
        dataStore = mockk()
        dataStoreRepository = DataStoreRepositoryImpl(dataStore)
        cacheRepository = CacheRepositoryImpl(dataStore)
        settingsUseCase = SettingsUseCase(dataStoreRepository = dataStoreRepository)
        cacheUseCase = CacheUseCase(cacheRepository = cacheRepository)
        settingsViewModel = SettingsViewModel(settingsUseCase, cacheUseCase)
    }

    //Still pending TODO
    @Test
    fun `Load settings into state holder`() = runTest {
//        val settings = settingsUseCase.loadSettings()
//        assertThat(settings).isInstanceOf(Settings::class.java)
    }

    //TODO
    @Test
    fun `Update settings in viewModel and dataStore`() {
    }
}