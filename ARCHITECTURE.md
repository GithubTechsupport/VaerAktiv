
# Architecture

The VaerAktiv app follows a layered MVVM (Model-View-ViewModel) architecture, designed for high cohesion and low coupling. Each screen has its own ViewModel, which interacts with repositories and data sources to provide the necessary data and logic for the UI.

## Layers

### View Layer

- **UI**: Built with Jetpack Compose in the `ui/` package, this layer contains all user interface elements, including screens, components, and themes.
- **ViewModels**: Each screen has a corresponding ViewModel (e.g., `HomeScreenViewModel`, `FavoriteLocationViewModel`) that manages UI state, handles user actions, and communicates with the domain/data layers.

### Domain Layer

- **Business Logic**: Some complex logic is encapsulated in use cases or repositories, especially in the `ai/` and `weather/` packages. This layer is responsible for transforming and combining data from multiple sources as needed.

### Data Layer

- **Repositories**: Classes like `WeatherRepositoryDefault`, `StravaRepository`, and `FavoriteLocationRepository` act as the main interface between the ViewModel and the data sources. They aggregate, cache, and process data.
- **Data Sources**: Each data source (e.g., `LocationForecastDataSource`, `NowcastDataSource`, `SunriseDataSource`) is responsible for a single source of data, such as a network API, local database, or device sensor.
- **Models**: Data models are defined in the `model/` package, organized by feature (e.g., `aggregateModels/Location.kt`, `ai/ActivitySuggestion.kt`, `locationforecast/LocationForecastResponse.kt`).

### Dependency Injection

- **Dagger Hilt**: Dependency injection is managed using Dagger Hilt, with modules defined in `di/AppModule.kt` and `di/ViewModelModule.kt`. Most dependencies are provided as singletons, supporting efficient resource use and easier testing.

### Data Flow

The app uses a unidirectional data flow (UDF) principle:
- Events and function calls flow down from the UI to the ViewModel and repositories.
- Data flows up from repositories to the ViewModel and then to the UI, often using `LiveData` or `StateFlow` for reactive updates.

### Persistence

- **Room Database**: User data such as favorite locations is stored locally using Room, ensuring persistence across app restarts.

## Technologies

- **Jetpack Compose** for UI
- **Dagger Hilt** for dependency injection
- **Ktor** for network requests
- **Room** for local database storage
- **Kotlin Coroutines** for asynchronous operations
- **StateFlow/LiveData** for reactive UI updates

## API Level

The app targets API level 24, ensuring compatibility with most Android devices in use today. No dependencies require a higher API level.

## Folder Structure

- `data/` — Data sources, repositories, and feature-specific logic
- `di/` — Dependency injection modules
- `model/` — Data models, organized by feature
- `network/` — Network clients and utilities
- `ui/` — Jetpack Compose UI components, screens, and ViewModels
- `utils/` — Utility classes and helpers

## Unidirectional Data Flow

The architecture ensures that:
- UI events trigger ViewModel actions
- ViewModels request data from repositories
- Repositories fetch/process data from data sources
- Data is exposed to the UI via observable state

This approach ensures UI consistency and maintainability.
  
---  

*For a visual overview, see the architecture diagram (if available).*