# README

## Explain how you ensure user is the right one starting the app

		To ensure this is the right user who is starting the app I decided to put in place a PIN to enter the app. 
		Indeed, when the user start the app he has to enter a PIN code (by default this code is 0000 at his first connection and he then has to change it).
		
## How do you securely save user's data on your phone ?

		The PIN is hashed (with md5) and only its hash is stored on the phone so no one can decrypt it.
		
## How did you hide the API url ?

		To hide the API url, I decided to use CMake to store my url in a C++ file (more difficult to decrypt than a Java/Kotlin class) and call a function from my activity to get the API url.
		
## Screenshots of your application

		I let you check the "Images" folder of my Github where you can find differents screenshots of my app
