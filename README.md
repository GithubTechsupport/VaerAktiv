
# VærAktiv

## Documentation

Documentation is available in the `docs` folder in the project's root directory.    
Open the `index.html` file in a browser to view the documentation.

You can generate the documentation by cloning the project and running `./gradlew dokkaHtml` from the root directory.

Alternatively, you can read the documentation files directly in a text editor.

## Running the app

1. Clone the repository and open the root folder in Android Studio.
2. The app requires an Android emulator or device with a minimum API level of 23 (Android 6.0).
3. The app requires location permissions to function correctly. You will be prompted to grant location access when running the app.
5. Run the app from Android Studio using the "Run" button or by selecting your device/emulator.

## Dependencies

The app uses several libraries and frameworks:

- [Ktor](https://ktor.io/)
    - Used for making API calls to external weather services (e.g., MET).
    - Handles HTTP requests and responses, and works with serialization for JSON data.

- [Gson](https://github.com/google/gson)
    - Used for serializing and deserializing JSON, especially for saving favorite locations.

- [Google Places API](https://developers.google.com/maps/documentation/places/android-sdk/overview)
    - Used for location autocomplete and place details.

- [Google Play Services Location](https://developer.android.com/training/location)
    - Used for accessing device location.

- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
    - Used for dependency injection to manage app components and their lifecycles.

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
    - Used for building the app's UI with a modern, declarative approach.

- [Navigation Compose](https://developer.android.com/develop/ui/compose/navigation)
    - Used for navigating between different screens in the app.

- [AndroidX DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
    - Used for storing user preferences and onboarding completion status.

- [OSMDroid](https://github.com/osmdroid/osmdroid) and [OSMBonusPack](https://github.com/MKergall/osmbonuspack)
    - Used for displaying and interacting with maps in the app.

- [Material3](https://m3.material.io/)
    - Used for Material Design UI components.

- [Kotlinx Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
    - Used for asynchronous programming and background tasks.

## Permissions

- The app requires location permissions (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, and `ACCESS_BACKGROUND_LOCATION`) to provide weather data and map features based on the user's location.
- Additionally the app requires internet permissions (`INTERNET` and `ACCESS_NETWORK_STATE`) to access external APIs and services.

## Notes

- Make sure to add your own API keys for Google Places and other services in the `secrets.properties` file as described in the project documentation.
- The app is intended for educational purposes and may require additional setup for production use.

## Warnings

- Function "observeOnce" is never used in `HomeScreenViewModel.kt` - The code this function belongs to is temporarily commented out to work with virtual emulators. 
- Parameter 'lifecycleOwner' is never used in `HomeScreenViewModel.kt` - The code this parameter belongs to is temporarily commented out to work with virtual emulators. 
- Property "deviceLocation" is never used in `HomeScreenViewModel.kt` - Used in `HomeScreen.kt`
- Functions "getFromLocationName" and "getFromLocation" in `GeocoderClass.kt` become deprecated in API level 33, and to remove the warning needs API level 33, which only 48% of android users have.
