# CST 438 Project 01

This is an Android application built with Kotlin . It uses
the public FBI Wanted API to display wanted-person information and includes
local accounts so users can create an account and log in on their device.

[Video](https://drive.google.com/file/d/1tBoP-s6Nj0cluMzspi0wkvB32nvbepDb/view?usp=drive_link).


## What You Can Do

- Create an account and log in.
- View a Suspect of the Day from the FBI Wanted API.
- Choose another random suspect.
- Browse a general feed of FBI subject categories.
- Visit the Search, Personal Page, and Settings screens.
- Log out and return to the Login screen.

## What You Need

- Android Studio
- An Android emulator or physical Android device running API 34 or newer
- An internet connection for FBI data and images

## How to Run the App

1. Clone or download this repository.
2. Open the project folder in Android Studio.
3. Wait for Android Studio to finish the Gradle sync.
4. Start an Android emulator or connect a physical Android device.
5. Select the `app` configuration and press **Run**.
6. On the Login screen, select **Create Account** to make a local account.

No FBI API key is needed, and you do not need to set up a database manually.
The app creates its local database automatically.

## Important Note

This is a classroom demonstration project. Accounts are stored only on the
device where they are created, so removing the app or clearing its data will
remove those local accounts. Not all features work as intended as they are right
as this is a work in progress.
