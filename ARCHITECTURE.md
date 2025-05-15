

# Architecture

The VaerAktiv app follows a layered MVVM (Model-View-ViewModel) architecture, designed for high cohesion and low coupling. Each screen has its own ViewModel, which interacts with repositories and data sources to provide the necessary data and logic for the UI.

## Layers

### View Layer

- **UI**: Built with Jetpack Compose in the `ui/` package, this layer contains all user interface elements, including screens, components, and themes.
- **ViewModels**: Each screen has a corresponding ViewModel (e.g., `HomeScreenViewModel`, `FavoriteLocationViewModel`) that manages UI state, handles user actions, and communicates with the domain/data layers.

### Domain Layer

- **Business Logic**: Though discrete use cases aren't encapsulated in separate files, they are all contained within an aggregate repository `WeatherRepository`, which is also responsible for data caching. The reason for this design choice is to be able to add and change use cases without having to constantly update dependency injections. Though it goes against the principles of high cohesion and low coupling, we also felt that over-separating could add unnecessary complexity given the scale of the project.

### Data Layer

- **Data Sources**: Each data source (e.g., `LocationForecastDataSource`, `NowcastDataSource`, `SunriseDataSource`) is responsible for a single source of data, such as a network API, local database, or device sensor.

-  **Repositories**: Data sources have corresponding repositories, which further process and format the data. Repositories are then combined into an aggregate data repository called `WeatherRepository` which acts as the main interface between the ViewModel and the repositories, where data is also cached and further processed.

- **Models**: Data models are defined in the `model/` package, organized by feature (e.g., `aggregateModels/Location`, `ai/ActivitySuggestion`, `locationforecast/LocationForecastResponse`).

### Dependency Injection

- **Dagger Hilt**: Dependency injection is managed using Dagger Hilt, with modules defined in `di/AppModule` and `di/ViewModelModule`. Most dependencies are provided as singletons, supporting efficient resource use and easier testing.

### Data Flow

The app uses a unidirectional data flow (UDF) principle:
- Events and function calls flow down from the UI to the ViewModel and repositories.
- Data flows up from repositories to the ViewModel and then to the UI, often using `LiveData` or `StateFlow` for reactive updates.


### Persistence

- **File-based Storage**: User data such as favorite locations and preferences are stored locally as JSON files in the app's private storage using custom data source classes (e.g., `FavoriteLocationDataSource`, `PreferenceDataSource`).
- **DataStore**: Strava authentication tokens are persisted securely using Jetpack DataStore via the `DataStoreTokenStorage` implementation.

## Technologies

- **Jetpack Compose** for UI
- **Dagger Hilt** for dependency injection
- **Ktor** for network requests
- **Kotlin Coroutines** for asynchronous operations
- **StateFlow/LiveData** for reactive UI updates
- **Jetpack DataStore** for secure key-value persistence
- **File I/O (JSON)** for local storage of user data

## API Level

The app targets API level 26, ensuring compatibility with most Android devices in use today. No dependencies require a higher API level.

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
