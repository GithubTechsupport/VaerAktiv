# VærAktiv - README

This is the readme file for the application. This file provides an overview of the project, inclusion instructions on how to download and run Væraktiv. It also outlines the key dependencies, location and warnings present in the code.

## Documentation

Documentation on functions and classes can be viewed within the codebase.

## Running the app

### 1.Install Androider studio
- Go to [https://developer.android.com/studio](https://developer.android.com/studio) and install Android Studio on your device. Instructions for downloading and setting it out are available on the site.

### 2. Clone the project to your computer
- Go to **Finder** (or file Explorer) and create a new folder where you want to store the project. Name it something relevant.
- Open a **terminal window**
- Navigate to the folder you just created using the `cd` command. For example:

	cd path/to/your/folder

- To se what folders or files Are in your current location, use `ls`:

- Now you clone the project repository by following the instructions in the next section.

### 3. Clone the repository via Github
if you Are unfamilliar with Clintons a GitHub repository, follow these steps: 
- Go to the repository [https://github.uio.no/IN2000-V25/team-31](https://github.uio.no/IN2000-V25/team-31)
- Click on the green button and copy the HTTPS link provided
- In your terminal window inside your folder, run:

	git clone https://github.uio.no/IN2000-V25/team-31

- This will download the project to your folder 

### 4. Run the application in Andorid studios
- Open Android studios
- Select **Open** or **Open Project**
- Navigate to the **team-31** folder you cloned and click open.
- Once the project is loaded, locate the device manager on the right side.
- Click the + button to create a new viritual device
- Choose a device with **API level 26** or higher (Android 8.0+)
- Follow the prompt to set ut the emulator.
- After setting ut the emulator, click on the green Run button in the topp-right corner of Android studio.
- Now you have launch the emulator and the app will start running.


## Dependencies

In addition to the other dependencies bellow, the application rekkes on to key-based dependencies. These keys have been added to the repository, but it is critical that typen keys are valid and function properly for the application to run as expected.
 
- **Strava key** 
		- This key handel and computing router for the choosen activity
- **Chat-GPT key** 
		- This key processes prompte and generator aktivitets. 

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

- Internett
	- An internet connection is required to run the application. Witouth it, data cannot be retriever and the application will not function.

## Permissions

- The app requires location permissions (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, and `ACCESS_BACKGROUND_LOCATION`) to provide weather data and map features based on the user's location.
- Additionally the app requires internet permissions (`INTERNET` and `ACCESS_NETWORK_STATE`) to access external APIs and services.

## Notes

- The app is intended for educational purposes and may require additional setup for production use.

## Warnings

- Functions "getFromLocationName" and "getFromLocation" in `GeocoderClass.kt` become deprecated in API level 33, and to remove the warning needs API level 33, which only 48% of android users have.

- All functions used by hilt get a never used warning in some versions av Android Studio. These functions are located in: `AppModule.kt`, `ViewModelModule.kt` `StravaModule.kt`, `AiClientModule.kt`, `NetworkClient.kt` and `PlacesClient.kt`, but they are infact used by hilt. The IDE just can't detect this sometimes.
