<h1 align="center">Watcha</h1>

<p align="center">Watcha is a sample of modern Android development built with Kotlin, Jetpack Compose, MVVM pattern and Clean Architecture as its base, and additionally, on top of that: Flows, Coroutines, DI (Hilt), Room and Retrofit.</p>

<p align="center">
  <img src="previews/Presentation.gif" width="960"/>
</p>


## ⚙️ App structure: tech stack and dependencies


<img src="previews/Home.gif" align="right" width="295"/>

- Minimum SDK: 31.
- [Kotlin](https://kotlinlang.org/docs/getting-started.html) based, taking advantage of its functional programming feature as well as [Coroutines](https://developer.android.com/kotlin/coroutines) and [Flow](https://developer.android.com/kotlin/flow) for asynchronous operations. 
- UI/UX:
  * [Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation): latest UI toolkit for building seameless modularized and reusable components.
  * [Compose navigation](https://developer.android.com/develop/ui/compose/navigation): facilitates navigation between screens.
    + [Nested navigation](https://developer.android.com/develop/ui/compose/navigation#nested-nav): Compose Navigation brings the opportunity to add nested graphs, making it easier to route user's destinations by features.
  * [Hilt](https://developer.android.com/develop/ui/compose/libraries#hilt) compose: allowing direct ViewModel DI into composables.
  * [Paging](https://developer.android.com/reference/kotlin/androidx/paging/compose/LazyPagingItems) compose: collecting data with LazyPagingItems and consume them in LazyLists.

- Architecture and UI pattern:
  * Clean Architecture: establish the project structure based in three main layers: UI layer, Domain layer and Data layer. Each one of these can contain more related sub-layers. 
  * MVVM pattern (Model - View - ViewModel) and elevated stateholders: by splitting UI in three different concerns (UI logic, UI info management and POJO classes as stateholders).
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): main class for managing screen data in UI layer. It's responsible of caching data to persist after configuration changes and expose events to be consumed by composables, and it's also aware of its lifecycle, facilitating its memory unbinding after the composable which contains it is disposed.

- Dependencies: 
  * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): adds dependency injection, easing up project structure and reducing code boilerplate.
  * [Room](https://developer.android.com/training/data-storage/room): creates and manages SQLite databases with simple and fast instructions.
  * [Retrofit](https://square.github.io/retrofit/): allows making HTTP calls to external resources, like RESTful APIs, to ask for data. 
  * GSON: used to parse data within HTTP calls between JSON and Dto Classes.
  * [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview): powerfull library which manages loading large datasets from a local or remote source in a efficient way.
    + [PagingSource](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource): manages both PagingData data loading per page and its status from remote or local sources.
    + [RemoteMediator](https://developer.android.com/reference/kotlin/androidx/paging/RemoteMediator): similar to PagingSource, but additionally stores data in a local database, like Room, to make it the single source of truth.
  * [Coil](https://coil-kt.github.io/coil/): lightweight library that brings a powerfull way to load Images based in Coroutines.
  * [Credential Manager](https://developer.android.com/identity/sign-in/credential-manager) (email + password, Google Sign In): as the AccountManager successor, this Jetpack API adds a modern way to authenticate users by id+password, federated sign-in solutions (Google Sign In) and passkeys.
  * [Firebase](https://firebase.google.com/docs/android/setup?hl=es) (Authentication): wide library with several modules that enhances the app quality. Authentication is one of these and adds a secure way to authenticate and store user credentials.

## 💊 Testing: types and dependencies

- Tests: Watcha includes some unit, integration, UI and E2E tests as example, but have in mind a few are outdated with the latests changes.
- Dependencies and API: 
  * [Compose Testing](https://developer.android.com/develop/ui/compose/testing): adds rules that allows composables testing and interaction.
  * [Hilt Testing](https://developer.android.com/training/dependency-injection/hilt-testing): adds rules to bring DI. Besides that, brings the oportunity to replace existing Modules with fake ones to make testing easier.
  * JUnit: base unit testing class.
  * [MockK](https://mockk.io/): Mocking library especialized in Kotlin to create stubs and mocks.
  * Espresso ([Intents](https://developer.android.com/training/testing/espresso/intents)): core API for UI, integration and E2E tests. As Watcha is built with Compose, only its Intent extension is being used.
  * [Robolectric](https://robolectric.org/): it brings some limitated Android SDK functionalities that can be benefitial for unit testing.
  * [Truth](https://truth.dev/): lightweight library which makes asserts easier and more legible.
  * [Turbine](https://developer.android.com/kotlin/flow/test): powerfull library for testing Kotlin Coroutines and Flows.

## 🔧 Extra features

- Caching system for offline access: by using Pager RemoteMediator combined with Room, most commonly accessed data will be available even if the user's device has no connection.
- Custom LoadStates per ViewModel for better UX.
- Daily scheduled data fetching with workers for storing it in the local SQLite database.
- App language selection, with its consecuent API call language adaption.
- User synchronization between its Watcha identity and TMDB identity with its TMDB session_id.
  * By now this link is established only for fetching its profile data, but it can be expanded as much as liked to other features like: lists management, movie/tv shows favorites, ratings, reviews, etc.


## 🎞️ Preview gallery:

<table>
<tr>
  <td>
    <img src="previews/Movie%20screen.gif" align="center" width="295"/>
    <p align="center"><b>Movie details</b></p>
  </td>
  <td>
    <img src="previews/Favorites.gif" align="center" width="295"/>
    <p align="center"><b>Favorite list</b></p>
  </td>
  <td>
    <img src="previews/Movie%20trailers.gif" align="center" width="295"/>
    <p align="center"><b>Youtube trailers</b></p>
  </td>
  <td>
    <img src="previews/Language%20selection.gif" align="center" width="295"/>
    <p align="center"><b>Language selection</b></p>
  </td>
</tr>
<tr>
  <td>
    <img src="previews/Caching%20system%20and%20error%20management.gif" width="295"/>
    <p align="center"><b>Caching system and error management</b></p>
  </td>
  <td>
    <img src="previews/Sign%20In%20and%20authorization.gif" align="center" width="295"/>
    <p align="center"><b>Sign In and authorization</b></p>
  </td>
  <td>
    <img src="previews/User%20session%20management.gif" align="center" width="295"/>
    <p align="center"><b>User session management</b></p>
  </td>
</tr>
</table>

## 🧩 Project architecture

🛠️WIP🛠️
