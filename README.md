# The Skybound Archipelago

An Android app built with Kotlin and Jetpack Compose. It includes a story reader, character generator, archipelago map, illustration atelier, courier satchel, and codex.

## Development setup

The Android app is in `app/`. It uses Android SDK 36 (API 36.1) and supports Android 7.0 (API 24) and newer. The Gradle wrapper configuration specifies Gradle 9.3.1, but this repository does not include the wrapper launch scripts. Install Gradle 9.3.1 and make `gradle` available on your `PATH` before running the commands below.

Live Gemini-powered character, image, music, and video generation uses a Gemini API key. To enable it, create a root-level `.env` file from the example and replace the placeholder:

```sh
cp .env.example .env
```

Set `GEMINI_API_KEY` in `.env` to your key. Do not commit `.env` or share the key. Without a configured key, the app falls back to procedural character generation, archived illustrations, and generated audio/video alternatives.

The Google Services Gradle plugin is configured to warn if `google-services.json` is absent; it is not required just to build this project.

## Build and test

Run these commands from the repository root:

```sh
gradle :app:assembleDebug
gradle :app:testDebugUnitTest
```

The project also has an instrumented test; run it with an emulator or connected Android device:

```sh
gradle :app:connectedDebugAndroidTest
```

## Release builds

The release build expects a signing keystore. Set `KEYSTORE_PATH` if the keystore is not at the repository root as `my-upload-key.jks`, and provide `STORE_PASSWORD` and `KEY_PASSWORD` in the build environment. The configured key alias is `upload`. Keep the keystore and passwords out of version control.
