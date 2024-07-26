<h1 align="center">Watcha</h1>

<p align="center">Watcha is a sample of modern Android development built with Kotlin, Jetpack Compose, MVVM pattern and Clean Architecture as its base, and additionally, on top of that: Flows, Coroutines, DI (Hilt), Room and Retrofit.</p>

> [!NOTE]
> Soon there will be available a release build so you can check this project downloading the app from Google Play Store.

<p align="center">
  <img src="previews/Presentation.gif" width="960"/>
</p>

## 📋 **Previous configuration**
```
✅ Create a TMDB API key
✅ Create a Google ID API key
✅ Store both API keys within local.properties file
✅ Configure a Firebase Auth access
```

## ⚙️ **Project structure: tech stack and dependencies**

- Minimum SDK: 31.
- [Kotlin](https://kotlinlang.org/docs/getting-started.html) based, taking advantage of its functional programming feature as well as [Coroutines](https://developer.android.com/kotlin/coroutines) and [Flow](https://developer.android.com/kotlin/flow) for asynchronous operations. 
- **UI/UX:**
  - [Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation): latest UI toolkit for building seamless modularized and reusable components.
  - [Compose navigation](https://developer.android.com/develop/ui/compose/navigation): facilitates navigation between screens.
    - [Nested navigation](https://developer.android.com/develop/ui/compose/navigation#nested-nav): Compose Navigation brings the opportunity to add nested graphs, making it easier to route user's destinations by features.
  - [Hilt](https://developer.android.com/develop/ui/compose/libraries#hilt) compose: allowing direct ViewModel DI into composables.
  - [Paging](https://developer.android.com/reference/kotlin/androidx/paging/compose/LazyPagingItems) compose: collecting data with LazyPagingItems and consume them in LazyLists.

- **Architecture and UI pattern:**
  - Clean Architecture: establish the project structure based in three main layers: UI layer, Domain layer and Data layer. Each one of these can contain more related sub-layers. 
  - MVVM pattern (Model - View - ViewModel) and elevated state holders: UI is split in three different concerns - UI logic, UI data management, and independent state holder classes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): main class for managing screen data in UI layer. It's responsible of caching data to persist after configuration changes and expose events to be consumed by composables, and it's also aware of its lifecycle, facilitating its memory unbinding after the composable which contains it is disposed.

- **Dependencies:**
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): adds dependency injection, easing up project structure and reducing code boilerplate.
  - [Room](https://developer.android.com/training/data-storage/room): creates and manages SQLite databases with simple and fast instructions.
  - [Retrofit](https://square.github.io/retrofit/): allows making HTTP calls to external resources, like RESTful APIs, to ask for data. 
  - GSON: used to parse data within HTTP calls between JSON and Dto Classes.
  - [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview): powerful library which manages loading large datasets from a local or remote source in an efficient way.
    - [PagingSource](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource): manages both PagingData data loading per page and its status from remote or local sources.
    - [RemoteMediator](https://developer.android.com/reference/kotlin/androidx/paging/RemoteMediator): similar to PagingSource, but additionally stores data in a local database, like Room, to make it the single source of truth.
  - [Coil](https://coil-kt.github.io/coil/): lightweight library that brings a powerful way to load Images based in Coroutines.
  - [Credential Manager](https://developer.android.com/identity/sign-in/credential-manager) (email + password, Google Sign In): as the AccountManager successor, this Jetpack API adds a modern way to authenticate users by id+password, federated sign-in solutions (Google Sign In) and passkeys.
  - [Firebase](https://firebase.google.com/docs/android/setup?hl=es) (Authentication): wide library with several modules that enhances the app quality. Authentication is one of these and adds a secure way to authenticate and store user credentials.

## 💊 **Testing: types and dependencies**

- Tests: Watcha includes some unit, integration, UI and E2E tests as example, but have in mind a few are outdated with the latest changes.
- **Dependencies and API:**
  - [Compose Testing](https://developer.android.com/develop/ui/compose/testing): adds rules that allows composables testing and interaction.
  - [Hilt Testing](https://developer.android.com/training/dependency-injection/hilt-testing): adds rules to bring DI. Besides that, brings the opportunity to replace existing Modules with fake ones to make testing easier.
  - JUnit: base unit testing class.
  - [MockK](https://mockk.io/): Mocking library specialized in Kotlin to create stubs and mocks.
  - Espresso ([Intents](https://developer.android.com/training/testing/espresso/intents)): core API for UI, integration and E2E tests. As Watcha is built with Compose, only its Intent extension is being used.
  - [Robolectric](https://robolectric.org/): it brings some limited Android SDK functionalities that can be beneficial for unit testing.
  - [Truth](https://truth.dev/): lightweight library which makes asserts easier and more legible.
  - [Turbine](https://developer.android.com/kotlin/flow/test): powerful library for testing Kotlin Coroutines and Flows.

## 🔧 **Extra features**

- **Caching system for offline access** by using Pager RemoteMediator combined with Room, therefore most commonly accessed data will be available even if the user's device has no connection.
- **Custom LoadStates per ViewModel for better UX.**
- **Daily scheduled data fetching with workers for storing it in the local SQLite database.**
- **App language selection, with its consequent API call language adaption.**
- **User synchronization** between its Watcha identity and TMDB identity with its TMDB session_id.
  - By now this link is established only for fetching its profile data, but it can be expanded as much as liked to other features like: lists management, movie/tv shows favorites, ratings, reviews, etc.


## 🧩 **Project architecture**

### **General overview**

<img src="previews/Project%20architecture%20diagram.png" width="100%"/>

Based on **[Clean Architecture](https://developer.android.com/topic/architecture)**, there are three main layers separated by concerns: **UI Layer, Domain Layer and Data Layer.** By doing so, it establishes a clean, robust, maintainable, reusable and scalable structure which makes it easier to be tested. Also, it defines a unidirectional event/data flow between these three layers, consuming events the deeper it goes and retrieving data to the outer layer.

### **UI Layer**

<img src="previews/UI%20Layer%20diagram.png" width="100%"/>

The outer layer, the **UI Layer**, has three main priorities:
- **Display data to user:** composables are responsible to display collected data to user.
- **React to data changes:** composables are always observing state holder data changes, so they recompose itself with new data.
- **Manage screen data:** each screen has its own ViewModel associated, which is responsible to manage the state holder data, expose events to be consumed by users and trigger operations in the Domain Layer.

### **Domain Layer**

<img src="previews/Domain%20layer%20diagram.png" width="100%"/>

**The Domain Layer**, the middle layer, contains all the project business logic, meaning its main purpose is executing data operations related to the business needs: mapping, sorting, filtering, etc. The most important characteristic is that this layer is platform free and must have only Java/Kotlin code to maintain its purity.

Here, Use cases, also known as Interactors, are responsible to exposing its events to UI Layer to be consumed and asking the Data Layer for data operations.

### **Data Layer**

<img src="previews/Data%20Layer%20diagram.png" width="100%"/>

**The Data Layer**, which is the lowest one, has only one focus: managing data from different sources, which includes performing CRUD operations, manage user data and so on. Under the hood, there can be several sources depending on the data definition. Watcha also applies the repository pattern, which compensates an extra layer with more flexibility and control over those services contracts.

Here repositories keep the event/data flow persistent as they expose events to be consumed by Domain Layer and performs data operations within its several associated services.

## 💻 **Public API**
Watcha uses [TMDB public API](https://developer.themoviedb.org/reference/intro/getting-started) as its main external data source, which is a RESTful API.

## 💜 Support this project 
If you find this repository illustrative and helpful, I kindly ask you to:
- Give this repository a star ⭐ 
- Follow me for more modern Android Development content 🤗

## 🎞️ **Extra: preview gallery**
Here you can take a quick overview about what Watcha does and what you can achieve by using Kotlin and Jetpack Compose as its base.

<table align="center" width="100%" border="0">
<tr>
  <td align="center">
    <img src="previews/Home.gif" width="75%"/>
    <p><b>Home</b></p>
  </td>
  <td align="center">
    <img src="previews/Movie%20screen.gif" width="75%"/>
    <p><b>Movie details</b></p>
  </td>
</tr>
<tr>
  <td align="center">
    <img src="previews/Favorites.gif" width="75%"/>
    <p><b>Favorite list</b></p>
  </td>
  <td align="center">
    <img src="previews/YouTube%20trailer.gif" width="75%"/>
    <p><b>YouTube trailer</b></p>
  </td>
</tr>
<tr>
  <td align="center">
    <img src="previews/Search.gif" width="75%"/>
    <p><b>Search</b></p>
  </td>
  <td align="center">
    <img src="previews/Language%20selection.gif" width="75%"/>
    <p><b>Language selection</b></p>
  </td>
</tr>
<tr>
  <td align="center">
    <img src="previews/Caching%20system.gif" width="75%"/>
    <p><b>Caching system</b></p>
  </td>
  <td align="center">
    <img src="previews/Error%20management.gif" width="75%"/>
    <p><b>Error management</b></p>
  </td>
</tr>
<tr>
  <td align="center">
    <img src="previews/Sign%20In%20and%20authorization.gif" width="75%"/>
    <p><b>Sign In and authorization</b></p>
  </td>
  <td align="center">
    <img src="previews/User%20session%20management.gif" width="75%"/>
    <p><b>User session management</b></p>
  </td>
</tr>
</table>