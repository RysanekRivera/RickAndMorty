<div align="center", style="display: flex; overflow-x: auto; white-space: nowrap;"><img src="https://github.com/RysanekRivera/RickAndMorty/blob/master/app/src/main/res/drawable/rick%20and%20morty%20icon.png" alt="Image 1" style="width: 100px; height: 110px;"></div>

# <p align="center">Rick And Morty</p>

<br>

<div align="center">
  <img src="https://github.com/RysanekRivera/RickAndMorty/blob/master/feature/feature-characters/src/main/res/drawable/rickandmorty.gif?raw=true" alt="Gif" />
</div>

<br>

__Rick And Morty__ is a robust Android application that demonstrates best practices in modern Android development. It fetches data from the [Rick And Morty API](https://rickandmortyapi.com/), and displays two tabs. The first with a list of characters that paginates over all available characters. The second, is a search tab which returns results by name. It features an intuitive and user-friendly interface, including:
- __Dynamic Search__: Easily find your favorite Rick and Morty character with a responsive search bar that fetches real-time results—only when an internet connection is available.
- __State Management__: Built on MVVM architecture, the app uses ViewModel and a custom UiState to manage UI state in a predictable, lifecycle-aware way—ensuring clean updates and avoiding memory leaks.
- __Accessibility__: The app is built with accessibility in mind, offering full support for ___TalkBack___. UI elements are properly labeled and structured to ensure a smooth and inclusive experience for users with visual impairments.
- __Event Handling__: User interactions such as lauching of SnackBars, Alert Dialogs are managed independently through the ViewModel layer, ensuring that business logic is decoupled from the UI.
- __Reactive Asynchronous Programming__: The app leverages Kotlin Coroutines and StateFlow for efficient, non-blocking data fetching and UI updates, ensuring a smooth and responsive user experience. This approach enables the app to handle network latency and large data sets gracefully, while maintaining seamless UI performance.
- __UI Testing Composable Functions in Isolation__: UI tests for composable functions in isolation were added to ensure performance at both, the micro and macro levels, adhering to the latest industry standards.
- __Unit Testing__: Unit tests were implemented to validate business logic, ensure reliability and minimize the potential for defects, all while adhering to contemporary best practices.

The app is built using a modular and scalable architecture to ensure maintainability and testability. It leverages modern Android development tools and practices, including:

- __Jetpack Compose__: For building a declarative UI, making it easy to create and manage UI components.
- __Compose Navigation__: To manage app navigation, ensuring a clean separation between UI and navigation logic.
- __MVVM Pattern__: Separating business logic from UI to facilitate testing and maintainability.
- __Multi-Module Architecture__: For better separation of concerns, faster build times, and more flexible code management.
- __Clean Architecture__: Following Uncle Bob's principles, separating the application into multiple layers (data, domain, and presentation) to achieve a clean and maintainable codebase.
- __Dagger-Hilt__: For dependency injection, reducing boilerplate code and improving the testability of the app.

This project reflects industry standards for building scalable, maintainable, and testable Android applications. By adhering to modern development practices and utilizing a clean architecture, making it easily extensible and adaptable to future changes.

<br>

## Images

<p align="center">
  <img src="https://github.com/RysanekRivera/RickAndMorty/blob/master/app/src/main/res/drawable/rick%20and%20morty%201.png" width="45%" />
</p>

<p align="center">
  <img src="https://github.com/RysanekRivera/RickAndMorty/blob/master/app/src/main/res/drawable/rick%20and%20morty%202.png" width="45%" />
  &nbsp;
  <img src="https://github.com/RysanekRivera/RickAndMorty/blob/master/app/src/main/res/drawable/rick%20and%20morty%205.png" width="45%" />
</p>

<p align="center">
  <img src="https://github.com/RysanekRivera/RickAndMorty/blob/master/app/src/main/res/drawable/rick%20and%20morty%203.png" width="45%" />
  &nbsp;
  <img src="https://github.com/RysanekRivera/RickAndMorty/blob/master/app/src/main/res/drawable/rick%20and%20morty%204.png" width="45%" />
</p>


