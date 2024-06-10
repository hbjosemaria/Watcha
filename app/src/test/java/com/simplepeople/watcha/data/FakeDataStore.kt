package com.simplepeople.watcha.data

//object FakeDataStore {
//
//    private val testFileName = "test"
//    private var instance : DataStore<Preferences>? = null
//
//    fun provideDataStore (scope : CoroutineScope) : DataStore<Preferences> {
//        return if (instance != null)
//            instance!!
//        else {
//            instance = PreferenceDataStoreFactory.create(
//                scope = scope,
//                produceFile = {
//                    ApplicationProvider.getApplicationContext<Context>().preferencesDataStoreFile(testFileName)
//                }
//            )
//            return instance as DataStore<Preferences>
//        }
//
//    }
//
//}

//class FakeDataStore() : DataStore<Preferences> {
//
//    private val dataMap: MutableMap<String, Preferences> = mutableMapOf()
//
//    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
//        val previousValue = dataMap.values.lastOrNull()
//        val updatedValue = transform(previousValue)
//        dataMap[updatedValue.asMap().keys.toString()] = updatedValue
//    }
//
//    override val data: Flow<Preferences>
//        get() = flowOf(dataMap.values.last())
//}