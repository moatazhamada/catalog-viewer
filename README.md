# Catalog Viewer

A simple Android application demonstrating a catalog viewer with search and favorites functionality, built with Jetpack Compose following MVVM architecture and Clean Architecture principles.

> **Challenge Requirements**: See [docs/CHALLENGE_REQUIREMENTS.md](docs/CHALLENGE_REQUIREMENTS.md) for the original challenge document.

**Note**: This project utilized AI tools as a development aid to accelerate boilerplate generation and explore implementation patterns. However, all code has been thoroughly reviewed, tested, and validated. Architecture decisions, design choices, and code quality were carefully evaluated to ensure the implementation meets professional standards and best practices.

## Additional Features & Enhancements

This implementation includes several features beyond the core requirements of the challenge:

- **Tablet/Foldable Support**: Two-pane layout for expanded screens (tablets and foldable devices) that displays the list and detail views side-by-side
- **Code Quality Tools**: Integrated ktlint, detekt, and spotless for automated code formatting and static analysis
- **Enhanced Architecture**: Full Clean Architecture implementation with proper layer separation (Data, Domain, Presentation)
- **Comprehensive Error Handling**: User-friendly error states with retry functionality
- **Reactive State Management**: StateFlow-based reactive UI updates

These additions were included to:
1. **Demonstrate Technical Interview Topics**: Showcase examples of concepts and best practices discussed during the technical interview, such as responsive UI design, code quality tooling, and architectural patterns
2. **Compensate for Delayed Delivery**: Provide additional value to make up for the delay in submitting the challenge

**Note on Scope**: I also considered adding Firebase integration (e.g., for remote data fetching or cloud-based favorites synchronization), but decided it would be excessive for a simple challenge like this. The current implementation focuses on demonstrating core Android development skills and architectural patterns without unnecessary complexity.

The core requirements (list view, search, detail view, favorites with persistence) are fully implemented and functional. The additional features enhance the codebase quality and demonstrate a deeper understanding of Android development best practices.

## How to Run

### Option 1: Using Gradle (Command Line)
```bash
# Build and install the app
./gradlew installDebug

# Or build and run tests
./gradlew test
```

### Option 2: Using Android Studio
1. Open the project in Android Studio
2. Click the "Run" button or press `Shift+F10`
3. Select a device or emulator

The app will install and launch automatically.

## Design Choices & Trade-offs

### Kotlinx Serialization
I chose **Kotlinx Serialization** over alternatives like Gson or Moshi for several reasons. As the official Kotlin serialization library, it provides compile-time code generation which results in better performance and type safety compared to runtime reflection-based solutions. It's also multiplatform-ready, which is beneficial for future expansion. The library integrates seamlessly with Kotlin coroutines and Flow, aligning perfectly with the reactive architecture used throughout the app. While Gson might be simpler for Java projects, Kotlinx Serialization is the recommended choice for modern Kotlin-first Android development.

### DataStore Preferences
For persistence, I selected **DataStore Preferences** over the traditional SharedPreferences API. DataStore is Google's modern replacement that provides several advantages: it offers a type-safe API, built-in support for Kotlin coroutines and Flow (enabling reactive data observation), better error handling, and is designed to be more reliable with atomic operations. While SharedPreferences is simpler and has been around longer, DataStore represents the future direction of Android data persistence and is recommended for all new projects. The migration path is straightforward, and the benefits in terms of type safety and reactive programming make it the clear choice for this application.

### MVVM + Clean Architecture
The application follows **MVVM (Model-View-ViewModel)** architecture combined with **Clean Architecture** principles, organizing code into distinct layers: Data, Domain, and Presentation. This separation of concerns makes the codebase more maintainable, testable, and scalable. The Repository pattern abstracts data sources, allowing easy swapping between local and remote data without affecting the ViewModel layer. Use cases in the Domain layer encapsulate business logic, following the Single Responsibility Principle from SOLID. This architecture, while adding some initial complexity, pays dividends in terms of code organization, testability, and long-term maintainability. The ViewModel uses StateFlow for reactive state management, which integrates better with Kotlin coroutines than LiveData and provides a more modern, type-safe approach to UI state.

### Hilt Dependency Injection
I chose **Hilt** over alternatives like Koin for dependency injection. Hilt, built on top of Dagger, provides compile-time code generation which ensures dependency issues are caught during compilation rather than at runtime, resulting in better performance and type safety. As part of the Android Jetpack suite, Hilt offers seamless integration with Android components (Activities, Fragments, ViewModels, Services) and provides native support for SavedStateHandle, which is essential for proper state management in ViewModels. While Koin is simpler and uses runtime dependency resolution (making it easier to learn), Hilt's compile-time approach is more suitable for larger projects and provides better scalability. Additionally, Hilt is officially maintained by Google, ensuring long-term support and alignment with Android's evolving ecosystem. The initial setup complexity is offset by the benefits of compile-time safety, better performance, and official support.

## Features

- **List View**: Displays all catalog items with title, category, price, and rating
- **Search**: Real-time filtering by title (case-insensitive, partial matching)
- **Detail View**: Full item details on a separate screen with modern, card-based design
- **Favorites**: Toggle favorite items with persistence across app restarts
- **Error Handling**: User-friendly error states with retry functionality
- **Empty States**: Clear messaging when no items match search criteria
- **Light & Dark Mode**: Automatic theme switching based on system preferences with Material 3 dynamic colors (Android 12+)
- **Tablet Support**: Two-pane layout for tablets and foldable devices

## Architecture

- **UI Layer**: Jetpack Compose screens and components
- **ViewModel Layer**: State management with StateFlow
- **Domain Layer**: Use cases for business logic, repository interfaces
- **Data Layer**: 
  - **Data Sources**: Abstract interfaces for data fetching (local, remote, Room, etc.)
  - **Repositories**: Implement domain interfaces, use data sources, map to domain models
  - **Mappers**: Convert between data and domain models
- **Navigation**: Navigation Compose with type-safe arguments
- **Dependency Injection**: Hilt for compile-time safe dependency management

### Data Layer Architecture

The data layer follows a clean separation pattern:

1. **Data Sources** (`data/datasource/`): Abstract interfaces and implementations for fetching data
   - `CatalogDataSource`: Interface for catalog data (local JSON, remote API, Room DB, etc.)
   - `LocalCatalogDataSource`: Current implementation reading from raw resources
   - Easy to add `RemoteCatalogDataSource`, `RoomCatalogDataSource`, etc.

2. **Repositories** (`data/repository/`): Implement domain repository interfaces
   - Use data sources to fetch data
   - Map data models to domain models using mappers
   - Handle errors and business logic

3. **Mappers** (`data/mapper/`): Convert between data and domain models
   - Keeps data and domain layers independent
   - Allows changing data models without affecting domain layer

**Benefits:**
- **Easy Data Source Swapping**: Change `LocalCatalogDataSource` to `RemoteCatalogDataSource` in Hilt module - no other changes needed!
- **Testability**: Can easily mock data sources for testing
- **Flexibility**: Can combine multiple data sources (e.g., cache + remote)
- **Maintainability**: Clear separation of concerns

## Testing

Run unit tests with:
```bash
./gradlew test
```

The project includes comprehensive unit tests covering:

- **ViewModel Layer**: Search filtering logic (case-insensitive, partial matching)
- **Data Layer**: 
  - JSON parsing and deserialization (`CatalogRepositoryTest`)
  - Data to domain model mapping (`CatalogMapperTest`)
  - Favorites toggle and check logic (`FavoritesDataSourceTest`)

All tests follow the Given-When-Then pattern and use descriptive test names.

## Code Quality

The project includes automated code quality tools:

- **Spotless**: Automatic code formatting (run `./gradlew spotlessApply`)
- **Ktlint**: Code style checking and formatting
- **Detekt**: Static code analysis for Kotlin
- **LeakCanary**: Memory leak detection (debug builds only)
  - Automatically detects memory leaks during development
  - Provides heap dumps and leak traces
  - Only active in debug builds, not included in release

Run code quality checks:
```bash
./gradlew spotlessApply  # Format code
./gradlew detekt         # Run static analysis
```

LeakCanary will automatically detect memory leaks when running debug builds. Check the LeakCanary notification or logs for detected leaks.

