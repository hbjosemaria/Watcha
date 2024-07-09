# Watcha
Simple Android app using TMDB API.

It uses:

- Kotlin
- Jetpack Compose
- MVVM
- Clean architecture
- Compose navigation
  * Nested navigation  
- Room
- Hilt
- Coil
- Retrofit
- Credential Manager (email + password, Google Sign In)
- Firebase (Authentication)
- Paging 3
  * PagingSource
  * RemoteMediator
- Testing:
  * Unit tests
  * Instrumentation tests (UI, integration) -> AndroidComposeRules
  * Libraries: JUnit, MockK, Espresso, Robolectric, Truth, Hilt (DI), Turbine.
- Cache system for offline access
- Custom LoadStates per ViewModel for better UX
- App language selection
- User sync with TMDB api with its session_id.
